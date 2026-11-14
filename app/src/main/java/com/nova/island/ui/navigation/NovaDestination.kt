package com.nova.island.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NovaDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    Home("home", "Home", Icons.Rounded.Home),
    Features("features", "Features", Icons.Rounded.AutoAwesome),
    Appearance("appearance", "Appearance", Icons.Rounded.Palette),
    Settings("settings", "Settings", Icons.Rounded.Settings),
    About("about", "About", Icons.Rounded.Info),
}
