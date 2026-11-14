package com.nova.island.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/** Titik masuk tunggal untuk menyalakan dan mematikan service island. */
object IslandServiceController {

    /** @return true jika Android menerima permintaan menjalankan service. */
    fun start(context: Context): Boolean = try {
        ContextCompat.startForegroundService(
            context,
            Intent(context, IslandOverlayService::class.java),
        )
        true
    } catch (e: Exception) {
        false
    }

    fun stop(context: Context) {
        context.stopService(Intent(context, IslandOverlayService::class.java))
    }
}
