package com.nova.island.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nova.island.ui.theme.NovaColors

/** Logo NOVA ISLAND: kapsul island dengan outline gradient dan titik hijau. */
@Composable
fun NovaLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.3f))
            .background(NovaColors.DarkGray),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(size * 0.66f)
                .height(size * 0.3f)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        listOf(NovaColors.Green, NovaColors.Blue, NovaColors.Purple)
                    )
                )
                .padding(size * 0.035f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(NovaColors.Black),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = size * 0.07f)
                        .size(size * 0.08f)
                        .clip(CircleShape)
                        .background(NovaColors.Green)
                )
            }
        }
    }
}
