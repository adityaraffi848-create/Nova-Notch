package com.nova.island.ui.screens.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.ScreenRotation
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nova.island.ui.components.NovaCard
import com.nova.island.ui.components.ScreenHeader
import com.nova.island.ui.components.StatusChip
import com.nova.island.ui.theme.NovaColors

private data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val phase: Int,
    val accent: Color,
)

private val featureCatalog = listOf(
    FeatureItem(
        "Island Overlay",
        "Kapsul di atas layar dengan mode Normal, Expanded, dan Interactive.",
        Icons.Rounded.PhoneAndroid, 2, NovaColors.Green,
    ),
    FeatureItem(
        "Music & Lyrics",
        "Cover album, kontrol pemutar, lirik tersinkron, dan gradient dari warna cover.",
        Icons.Rounded.MusicNote, 3, NovaColors.Purple,
    ),
    FeatureItem(
        "WhatsApp",
        "Notifikasi masuk lewat izin Notification Access, dengan opsi sembunyikan preview.",
        Icons.Rounded.ChatBubble, 4, NovaColors.Green,
    ),
    FeatureItem(
        "Charging",
        "Indikator petir, persentase baterai, dan estimasi waktu pengisian.",
        Icons.Rounded.BatteryChargingFull, 5, NovaColors.Green,
    ),
    FeatureItem(
        "Connectivity",
        "Bluetooth, WiFi, data seluler, dan hotspot sebatas yang diizinkan Android.",
        Icons.Rounded.Bluetooth, 5, NovaColors.Blue,
    ),
    FeatureItem(
        "Landscape Auto-Hide",
        "Island menyingkir saat landscape agar tidak menutupi game.",
        Icons.Rounded.ScreenRotation, 6, NovaColors.Blue,
    ),
    FeatureItem(
        "Quick Settings",
        "Panel pengaturan melayang langsung dari island.",
        Icons.Rounded.Tune, 7, NovaColors.Purple,
    ),
)

@Composable
fun FeaturesScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = "Features",
                subtitle = "Roadmap fitur. Statusnya berubah menjadi Aktif saat fase terkait selesai.",
            )
        }
        items(featureCatalog) { feature ->
            NovaCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(feature.accent.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = feature.icon,
                            contentDescription = null,
                            tint = feature.accent,
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = feature.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = feature.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    StatusChip(text = "Phase ${feature.phase}", color = NovaColors.TextSecondary)
                }
            }
        }
    }
}
