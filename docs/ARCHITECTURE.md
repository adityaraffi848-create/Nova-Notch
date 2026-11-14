# Arsitektur

```
Monitor (sumber data nyata Android)         State Holder              Overlay
MediaSessionMonitor   (musik)        ──┐
NotificationListener  (WhatsApp)     ──┤   IslandStateHolder    IslandOverlayService
ChargingMonitor       (baterai)      ──┼─► antrean + prioritas ─► (foreground service)
ConnectivityMonitor   (BT/WiFi/dll)  ──┤   mode Normal/Expanded   ComposeView di WindowManager
OrientationMonitor    (landscape)    ──┘   /Lyrics                (TYPE_APPLICATION_OVERLAY)
                      ▲
                      └── SettingsRepository (DataStore Flow) dipakai semua komponen
```

- Setiap monitor hanya memancarkan event; `IslandStateHolder` memutuskan apa yang tampil.
- Overlay hidup di Foreground Service, bukan Activity, sehingga tetap ada saat berpindah aplikasi.
- Pengaturan dibaca lewat Flow sehingga perubahan langsung berlaku tanpa restart.
- Aplikasi utama hanya untuk konfigurasi dan onboarding permission.

## Batasan platform Android

- Logo WhatsApp diambil dari ikon aplikasi asli lewat notifikasi / PackageManager.
- Jumlah perangkat hotspot tidak tersedia lewat API publik.
- Overlay biasa tidak bisa tampil di atas lock screen.
- Deteksi fullscreen/game tanpa Accessibility Service terbatas; landscape auto-hide memakai orientasi
  dan pengaturan manual sebagai cadangan.
