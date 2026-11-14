package com.nova.island.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nova.island.data.settings.IslandSettings
import com.nova.island.ui.components.NovaCard
import com.nova.island.ui.components.NovaSwitchRow
import com.nova.island.ui.components.ScreenHeader
import com.nova.island.ui.components.SectionTitle
import com.nova.island.ui.theme.NovaColors

@Composable
fun SettingsScreen(
    settings: IslandSettings,
    onIslandEnabledChange: (Boolean) -> Unit,
    onAnimationEffectsChange: (Boolean) -> Unit,
    onResetAll: () -> Unit,
) {
    var confirmReset by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ScreenHeader(
                title = "Settings",
                subtitle = "Pengaturan umum. Opsi lain muncul bersama fiturnya.",
            )
        }

        item {
            SectionTitle("General")
            Spacer(Modifier.height(8.dp))
            NovaCard {
                NovaSwitchRow(
                    title = "Enable Dynamic Island",
                    subtitle = null,
                    checked = settings.islandEnabled,
                    onCheckedChange = onIslandEnabledChange,
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                NovaSwitchRow(
                    title = "Animation Effects",
                    subtitle = "Matikan agar perubahan ukuran tanpa animasi",
                    checked = settings.animationEffects,
                    onCheckedChange = onAnimationEffectsChange,
                )
            }
        }

        item {
            SectionTitle("Data")
            Spacer(Modifier.height(8.dp))
            NovaCard {
                Text(
                    text = "Mengembalikan semua pengaturan Nova Island ke nilai awal.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { confirmReset = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NovaColors.Danger.copy(alpha = 0.16f),
                        contentColor = NovaColors.Danger,
                    ),
                ) {
                    Text("Reset semua pengaturan")
                }
            }
        }
    }

    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text("Reset semua pengaturan?") },
            text = { Text("Semua pengaturan akan kembali ke nilai awal.") },
            confirmButton = {
                TextButton(onClick = {
                    onResetAll()
                    confirmReset = false
                }) { Text("Reset", color = NovaColors.Danger) }
            },
            dismissButton = {
                TextButton(onClick = { confirmReset = false }) {
                    Text("Batal", color = NovaColors.Green)
                }
            },
        )
    }
}
