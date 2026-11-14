package com.nova.island.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nova.island.data.settings.IslandSettings
import com.nova.island.ui.main.MainViewModel
import com.nova.island.ui.screens.about.AboutScreen
import com.nova.island.ui.screens.appearance.AppearanceScreen
import com.nova.island.ui.screens.features.FeaturesScreen
import com.nova.island.ui.screens.home.HomeScreen
import com.nova.island.ui.screens.settings.SettingsScreen
import com.nova.island.ui.theme.NovaColors

@Composable
fun NovaRoot(
    settings: IslandSettings,
    viewModel: MainViewModel,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp,
            ) {
                NovaDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NovaColors.Green,
                            selectedTextColor = NovaColors.Green,
                            indicatorColor = NovaColors.Green.copy(alpha = 0.14f),
                            unselectedIconColor = NovaColors.TextSecondary,
                            unselectedTextColor = NovaColors.TextSecondary,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NovaDestination.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(220)) },
            exitTransition = { fadeOut(tween(160)) },
            popEnterTransition = { fadeIn(tween(220)) },
            popExitTransition = { fadeOut(tween(160)) },
        ) {
            composable(NovaDestination.Home.route) {
                HomeScreen(
                    settings = settings,
                    onIslandEnabledChange = viewModel::setIslandEnabled,
                )
            }
            composable(NovaDestination.Features.route) {
                FeaturesScreen()
            }
            composable(NovaDestination.Appearance.route) {
                AppearanceScreen(
                    settings = settings,
                    onCommit = viewModel::saveAppearance,
                    onReset = viewModel::resetAppearance,
                )
            }
            composable(NovaDestination.Settings.route) {
                SettingsScreen(
                    settings = settings,
                    onIslandEnabledChange = viewModel::setIslandEnabled,
                    onAnimationEffectsChange = viewModel::setAnimationEffects,
                    onResetAll = viewModel::resetAll,
                )
            }
            composable(NovaDestination.About.route) {
                AboutScreen()
            }
        }
    }
}
