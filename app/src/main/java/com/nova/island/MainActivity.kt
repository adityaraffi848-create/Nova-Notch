package com.nova.island

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nova.island.ui.main.MainViewModel
import com.nova.island.ui.navigation.NovaRoot
import com.nova.island.ui.theme.NovaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )

        val container = (application as NovaApp).container

        setContent {
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModel.factory(container.settingsRepository)
            )
            val settings by viewModel.settings.collectAsStateWithLifecycle()

            NovaTheme(amoled = settings.amoledTheme) {
                NovaRoot(settings = settings, viewModel = viewModel)
            }
        }
    }
}
