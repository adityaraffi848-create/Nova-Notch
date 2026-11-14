package com.nova.island.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nova.island.data.settings.IslandSettings
import com.nova.island.ui.components.IslandPreview
import com.nova.island.ui.components.NovaCard
import com.nova.island.ui.components.NovaLogoMark
import com.nova.island.ui.components.NovaSwitchRow
import com.nova.island.ui.components.SectionTitle
import com.nova.island.ui.components.StatusChip
import com.nova.island.ui.theme.NovaColors

@Composable
fun HomeScreen(
    settings: IslandSettings,
    overlayGranted: Boolean,
    onIslandEnabledChange: (Boolean) -> Unit,
    onRequestOverlayPermission: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NovaLogoMark(size = 56.dp)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = "NOVA ISLAND",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "Dynamic Island untuk Android",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        item {
            SectionTitle("Preview")
            Spacer(Modifier.height(8.dp))
            IslandPreview(settings = settings)
        }

        item {
            NovaCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Status Dynamic Island",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    StatusChip(
                        text = if (settings.islandEnabled) "AKTIF" else "NONAKTIF",
                        color = if (settings.islandEnabled) NovaColors.Green
                        else NovaColors.TextSecondary,
                    )
                }
                Spacer(Modifier.height(6.dp))
                NovaSwitchRow(
                    title = "Enable Dynamic Island",
                    subtitle = "Island tampil di tengah atas layar, di atas aplikasi lain",
                    checked = settings.islandEnabled,
                    onCheckedChange = onIslandEnabledChange,
                )
            }
        }

        item {
            NovaCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Izin tampil di atas aplikasi lain",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    StatusChip(
                        text = if (overlayGranted) "DIIZINKAN" else "BELUM",
                        color = if (overlayGranted) NovaColors.Green else NovaColors.Danger,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Dibutuhkan agar island bisa muncul di atas aplikasi lain.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (!overlayGranted) {
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onRequestOverlayPermission,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NovaColors.Green,
                            contentColor = NovaColors.Black,
                        ),
                    ) {
                        Text("Buka pengaturan izin")
                    }
                }
            }
        }
    }
}
