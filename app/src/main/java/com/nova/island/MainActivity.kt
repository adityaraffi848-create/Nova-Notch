package com.nova.island

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.nova.island.permission.PermissionManager
import com.nova.island.service.IslandServiceController
import com.nova.island.ui.main.MainViewModel
import com.nova.island.ui.navigation.NovaRoot
import com.nova.island.ui.theme.NovaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.factory((application as NovaApp).container.settingsRepository)
    }

    private val overlayGranted = mutableStateOf(false)
    private var pendingEnable = false

    // Hasil izin notifikasi tidak mengubah fungsi island (hanya terlihat atau tidaknya
    // notifikasi service), jadi tidak ada tindakan lanjutan di callback.
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )

        setContent {
            val settings by viewModel.settings.collectAsStateWithLifecycle()
            val overlayOk by overlayGranted

            NovaTheme(amoled = settings.amoledTheme) {
                NovaRoot(
                    settings = settings,
                    viewModel = viewModel,
                    overlayGranted = overlayOk,
                    onIslandEnabledChange = ::onIslandToggle,
                    onRequestOverlayPermission = ::openOverlaySettings,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val granted = PermissionManager.canDrawOverlays(this)
        overlayGranted.value = granted

        if (pendingEnable) {
            pendingEnable = false
            if (granted) enableIsland()
        } else if (!granted && viewModel.settings.value.islandEnabled) {
            // Izin dicabut dari pengaturan sistem: matikan island supaya statusnya jujur.
            disableIsland()
        }
    }

    private fun onIslandToggle(enable: Boolean) {
        if (!enable) {
            disableIsland()
            return
        }
        if (!PermissionManager.canDrawOverlays(this)) {
            pendingEnable = true
            Toast.makeText(
                this,
                "Izinkan \"Tampil di atas aplikasi lain\" untuk NOVA ISLAND",
                Toast.LENGTH_LONG,
            ).show()
            openOverlaySettings()
            return
        }
        enableIsland()
    }

    private fun enableIsland() {
        lifecycleScope.launch {
            // Tulis status dulu, baru jalankan service, supaya service tidak membaca nilai lama.
            viewModel.commitIslandEnabled(true)
            if (!IslandServiceController.start(applicationContext)) {
                viewModel.commitIslandEnabled(false)
                Toast.makeText(
                    this@MainActivity,
                    "Island gagal dijalankan. Coba nyalakan lagi dari layar aplikasi.",
                    Toast.LENGTH_LONG,
                ).show()
                return@launch
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !PermissionManager.hasNotificationPermission(this@MainActivity)
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun disableIsland() {
        lifecycleScope.launch {
            viewModel.commitIslandEnabled(false)
            IslandServiceController.stop(applicationContext)
        }
    }

    private fun openOverlaySettings() {
        startActivity(PermissionManager.overlaySettingsIntent(this))
    }
}
