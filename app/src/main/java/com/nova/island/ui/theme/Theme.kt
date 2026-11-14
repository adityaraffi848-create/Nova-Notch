package com.nova.island.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * Tema Nova Island selalu gelap. Dynamic color (Material You) sengaja tidak dipakai
 * supaya identitas hitam dan hijau tetap konsisten.
 */
@Composable
fun NovaTheme(
    amoled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val background = if (amoled) NovaColors.Black else NovaColors.DarkGray

    val colorScheme = darkColorScheme(
        primary = NovaColors.Green,
        onPrimary = NovaColors.Black,
        secondary = NovaColors.Blue,
        onSecondary = NovaColors.TextPrimary,
        tertiary = NovaColors.Purple,
        onTertiary = NovaColors.TextPrimary,
        background = background,
        onBackground = NovaColors.TextPrimary,
        surface = NovaColors.Surface,
        onSurface = NovaColors.TextPrimary,
        surfaceVariant = NovaColors.Surface,
        onSurfaceVariant = NovaColors.TextSecondary,
        outline = NovaColors.Outline,
        error = NovaColors.Danger,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NovaTypography,
        shapes = NovaShapes,
        content = content,
    )
}
