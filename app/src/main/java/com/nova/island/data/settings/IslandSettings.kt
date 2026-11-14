package com.nova.island.data.settings

/**
 * Model pengaturan Nova Island. Phase 1 berisi pengaturan umum dan Appearance.
 * Toggle musik, notifikasi, dan konektivitas ditambahkan bersama fiturnya.
 */
data class IslandSettings(
    val islandEnabled: Boolean = false,
    val animationEffects: Boolean = true,
    val amoledTheme: Boolean = true,
    val sizePercent: Int = 100,
    val heightDp: Int = 36,
    val radiusDp: Int = 18,
    val animationSpeedPercent: Int = 100,
    val backgroundOpacityPercent: Int = 100,
    val gradientIntensityPercent: Int = 70,
)
