package com.nova.island.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nova.island.MainActivity
import com.nova.island.NovaApp
import com.nova.island.R
import com.nova.island.overlay.IslandOverlayController
import com.nova.island.permission.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Foreground service pemilik overlay. Service mengamati pengaturan: island tampil selama
 * "Enable Dynamic Island" aktif dan izin overlay diberikan, dan hilang begitu salah satunya tidak.
 */
class IslandOverlayService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val overlay by lazy { IslandOverlayController(applicationContext) }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        try {
            startAsForeground()
        } catch (e: Exception) {
            stopSelf()
            return
        }

        val repository = (application as NovaApp).container.settingsRepository
        scope.launch {
            repository.settings.collect { settings ->
                when {
                    !settings.islandEnabled -> stopSelf()
                    !PermissionManager.canDrawOverlays(this@IslandOverlayService) -> {
                        repository.setIslandEnabled(false)
                        stopSelf()
                    }
                    else -> overlay.show(settings)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            val repository = (application as NovaApp).container.settingsRepository
            scope.launch {
                repository.setIslandEnabled(false)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        overlay.hide()
        super.onDestroy()
    }

    private fun startAsForeground() {
        val notification = buildNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Dynamic Island",
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = "Menjaga Dynamic Island tetap berjalan"
            setShowBadge(false)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val openApp = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val stop = PendingIntent.getService(
            this,
            1,
            Intent(this, IslandOverlayService::class.java).setAction(ACTION_STOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_island)
            .setContentTitle("NOVA ISLAND aktif")
            .setContentText("Dynamic Island sedang berjalan")
            .setContentIntent(openApp)
            .addAction(0, "Matikan", stop)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "nova_island_service"
        private const val NOTIFICATION_ID = 1001
        const val ACTION_STOP = "com.nova.island.action.STOP"
    }
}
