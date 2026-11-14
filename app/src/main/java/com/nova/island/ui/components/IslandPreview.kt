package com.nova.island.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nova.island.data.settings.IslandSettings
import com.nova.island.ui.theme.NovaColors

/**
 * Pratinjau tampilan Dynamic Island yang memakai pengaturan Appearance yang sama
 * dengan overlay (ukuran, tinggi, radius, kecepatan animasi, opasitas, gradient).
 * Di Phase 2 komponen ini dipakai ulang oleh overlay sungguhan.
 */
@Composable
fun IslandPreview(
    settings: IslandSettings,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val stageShape = MaterialTheme.shapes.large

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(stageShape)
            .background(Brush.verticalGradient(listOf(Color(0xFF0C0C0C), Color(0xFF1C1C1C))))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), stageShape),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(modifier = Modifier.padding(top = 14.dp)) {
            IslandCapsule(
                settings = settings,
                expanded = expanded,
                onToggle = { expanded = !expanded },
            )
        }
        Text(
            text = "Ketuk island untuk memperluas",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * Kapsul island. Dipakai oleh preview di aplikasi dan oleh overlay sungguhan di layar.
 */
@Composable
fun IslandCapsule(
    settings: IslandSettings,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    expandedSubtitle: String = "Pratinjau tampilan",
) {
    val effects = settings.animationEffects
    val speed = settings.animationSpeedPercent.coerceIn(50, 200)
    val duration = 420 * 100 / speed

    fun <T> spec(): AnimationSpec<T> =
        if (effects) tween(durationMillis = duration, easing = FastOutSlowInEasing) else snap()

    val collapsedWidth = 126f * settings.sizePercent / 100f
    val collapsedHeight = settings.heightDp.toFloat()

    val targetWidth = if (expanded) minOf(collapsedWidth * 2.4f, 300f) else collapsedWidth
    val targetHeight = if (expanded) collapsedHeight + 64f else collapsedHeight
    val targetRadius = (if (expanded) settings.radiusDp + 14f else settings.radiusDp.toFloat())
        .coerceAtMost(targetHeight / 2f)

    val width by animateDpAsState(targetWidth.dp, spec(), label = "islandWidth")
    val height by animateDpAsState(targetHeight.dp, spec(), label = "islandHeight")
    val radius by animateDpAsState(targetRadius.dp, spec(), label = "islandRadius")

    val pulse = rememberInfiniteTransition(label = "dotPulse")
    val glow by pulse.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "dotGlow",
    )
    val dotAlpha = if (effects) glow else 1f
    val gradientIntensity = settings.gradientIntensityPercent / 100f
    val shape = RoundedCornerShape(radius)

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(shape)
            .background(NovaColors.Black.copy(alpha = settings.backgroundOpacityPercent / 100f))
            .border(1.dp, Color.White.copy(alpha = 0.07f), shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle,
            ),
    ) {
        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                NovaColors.Purple.copy(alpha = 0.55f * gradientIntensity),
                            )
                        )
                    )
            )
        }

        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier.align(Alignment.BottomStart),
            enter = if (effects) fadeIn(tween(duration, delayMillis = duration / 3))
            else EnterTransition.None,
            exit = if (effects) fadeOut(tween(duration / 3)) else ExitTransition.None,
        ) {
            Row(
                modifier = Modifier.padding(start = 16.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NovaLogoMark(size = 32.dp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = "NOVA ISLAND",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = expandedSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = ((collapsedHeight - 10f) / 2f).dp, end = 12.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(NovaColors.Green.copy(alpha = dotAlpha))
        )
    }
}
