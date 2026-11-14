package com.nova.island.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.nova.island.data.settings.IslandSettings
import com.nova.island.ui.components.IslandCapsule
import com.nova.island.ui.theme.NovaTheme
import kotlin.math.roundToInt

/**
 * Menempel dan melepas island di WindowManager. Hanya ada satu ComposeView:
 * [show] aman dipanggil berkali-kali (hanya memperbarui state), jadi tidak ada overlay ganda.
 */
class IslandOverlayController(private val context: Context) {

    private val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var composeView: ComposeView? = null
    private var lifecycleOwner: OverlayLifecycleOwner? = null
    private var layoutParams: WindowManager.LayoutParams? = null

    private val settingsState = mutableStateOf(IslandSettings())
    private val expandedState = mutableStateOf(false)

    val isShowing: Boolean get() = composeView != null

    fun show(settings: IslandSettings) {
        settingsState.value = settings
        if (composeView == null) attach(settings) else updatePosition(settings)
    }

    fun hide() {
        composeView?.let { view ->
            runCatching { windowManager.removeView(view) }
        }
        composeView = null
        layoutParams = null
        lifecycleOwner?.destroy()
        lifecycleOwner = null
        expandedState.value = false
    }

    private fun attach(settings: IslandSettings) {
        val owner = OverlayLifecycleOwner().also { it.create() }

        val view = ComposeView(context).apply {
            setViewTreeLifecycleOwner(owner)
            setViewTreeViewModelStoreOwner(owner)
            setViewTreeSavedStateRegistryOwner(owner)
            setContent {
                val current by settingsState
                NovaTheme(amoled = current.amoledTheme) {
                    IslandCapsule(
                        settings = current,
                        expanded = expandedState.value,
                        onToggle = { expandedState.value = !expandedState.value },
                        expandedSubtitle = "Island aktif",
                    )
                }
            }
        }
        owner.resume()

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT,
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = dpToPx(settings.topOffsetDp)
            title = "NovaIsland"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }

        windowManager.addView(view, params)

        composeView = view
        lifecycleOwner = owner
        layoutParams = params
    }

    private fun updatePosition(settings: IslandSettings) {
        val view = composeView ?: return
        val params = layoutParams ?: return
        val newY = dpToPx(settings.topOffsetDp)
        if (params.y != newY) {
            params.y = newY
            windowManager.updateViewLayout(view, params)
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * context.resources.displayMetrics.density).roundToInt()
}
