package com.nova.island.ui.screens.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nova.island.data.settings.IslandSettings
import com.nova.island.ui.components.IslandPreview
import com.nova.island.ui.components.NovaCard
import com.nova.island.ui.components.NovaSliderRow
import com.nova.island.ui.components.NovaSwitchRow
import com.nova.island.ui.components.ScreenHeader
import com.nova.island.ui.components.SectionTitle
import com.nova.island.ui.theme.NovaColors
import kotlin.math.roundToInt

@Composable
fun AppearanceScreen(
    settings: IslandSettings,
    onCommit: (IslandSettings) -> Unit,
    onReset: () -> Unit,
) {
    // Draft lokal: slider dan preview bergerak halus saat digeser,
    // dan DataStore hanya ditulis saat jari diangkat.
    var draft by remember { mutableStateOf(settings) }
    LaunchedEffect(settings) { draft = settings }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ScreenHeader(
                title = "Appearance",
                subtitle = "Perubahan langsung terlihat pada preview.",
            )
        }
        item { IslandPreview(settings = draft) }

        item {
            SectionTitle("Island")
            Spacer(Modifier.height(8.dp))
            NovaCard {
                NovaSliderRow(
                    label = "Island Size",
                    valueText = "${draft.sizePercent}%",
                    value = draft.sizePercent.toFloat(),
                    valueRange = 80f..140f,
                    onValueChange = { draft = draft.copy(sizePercent = it.roundToInt()) },
                    onValueChangeFinished = { onCommit(draft) },
                )
                NovaSliderRow(
                    label = "Island Height",
                    valueText = "${draft.heightDp} dp",
                    value = draft.heightDp.toFloat(),
                    valueRange = 28f..48f,
                    onValueChange = { draft = draft.copy(heightDp = it.roundToInt()) },
                    onValueChangeFinished = { onCommit(draft) },
                )
                NovaSliderRow(
                    label = "Border Radius",
                    valueText = "${draft.radiusDp} dp",
                    value = draft.radiusDp.toFloat(),
                    valueRange = 8f..24f,
                    onValueChange = { draft = draft.copy(radiusDp = it.roundToInt()) },
                    onValueChangeFinished = { onCommit(draft) },
                )
            }
        }

        item {
            SectionTitle("Efek")
            Spacer(Modifier.height(8.dp))
            NovaCard {
                NovaSliderRow(
                    label = "Animation Speed",
                    valueText = "${draft.animationSpeedPercent}%",
                    value = draft.animationSpeedPercent.toFloat(),
                    valueRange = 50f..200f,
                    onValueChange = { draft = draft.copy(animationSpeedPercent = it.roundToInt()) },
                    onValueChangeFinished = { onCommit(draft) },
                )
                NovaSliderRow(
                    label = "Background Opacity",
                    valueText = "${draft.backgroundOpacityPercent}%",
                    value = draft.backgroundOpacityPercent.toFloat(),
                    valueRange = 40f..100f,
                    onValueChange = { draft = draft.copy(backgroundOpacityPercent = it.roundToInt()) },
                    onValueChangeFinished = { onCommit(draft) },
                )
                NovaSliderRow(
                    label = "Gradient Intensity",
                    valueText = "${draft.gradientIntensityPercent}%",
                    value = draft.gradientIntensityPercent.toFloat(),
                    valueRange = 0f..100f,
                    onValueChange = { draft = draft.copy(gradientIntensityPercent = it.roundToInt()) },
                    onValueChangeFinished = { onCommit(draft) },
                )
            }
        }

        item {
            SectionTitle("Tema")
            Spacer(Modifier.height(8.dp))
            NovaCard {
                NovaSwitchRow(
                    title = "Dark / AMOLED Theme",
                    subtitle = "Hitam pekat untuk layar AMOLED",
                    checked = draft.amoledTheme,
                    onCheckedChange = {
                        val updated = draft.copy(amoledTheme = it)
                        draft = updated
                        onCommit(updated)
                    },
                )
            }
        }

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                TextButton(onClick = onReset) {
                    Text("Reset Appearance ke default", color = NovaColors.Green)
                }
            }
        }
    }
}
