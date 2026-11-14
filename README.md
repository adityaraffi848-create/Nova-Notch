# NOVA ISLAND

![Build APK](https://github.com/USERNAME/NovaIsland/actions/workflows/build.yml/badge.svg)

Aplikasi Android **Dynamic Island interaktif**: musik dengan lirik tersinkron, notifikasi WhatsApp,
indikator charging, dan deteksi Bluetooth / WiFi / hotspot dalam satu kapsul di atas layar.

> Status: **Phase 1 selesai** (fondasi project, design system, navigasi, penyimpanan pengaturan).
> Overlay Dynamic Island sungguhan dikerjakan mulai Phase 2. Lihat [docs/ROADMAP.md](docs/ROADMAP.md).

## Teknologi

Kotlin, Jetpack Compose, Material 3, DataStore, MVVM + repository, Navigation Compose.
Min SDK 29 (Android 10), compile/target SDK 35, JDK 17.

## Menjalankan project

1. Buka folder ini di Android Studio (**File > Open**).
2. Tunggu Gradle Sync selesai (Gradle 8.9 diunduh otomatis).
3. Jalankan di perangkat atau emulator.

## Build APK lewat GitHub Actions

Setiap push ke `main` membangun APK debug otomatis. Unduh dari tab **Actions** > run terbaru > **Artifacts**
> `nova-island-debug-apk`. Workflow juga bisa dijalankan manual (**Run workflow**).

## Struktur

```
app/src/main/java/com/nova/island/
├── NovaApp.kt, MainActivity.kt
├── di/            AppContainer
├── data/settings/ IslandSettings, DataStore, SettingsRepository
└── ui/
    ├── main/        MainViewModel
    ├── theme/       warna, tipografi, shape, tema
    ├── components/  kartu, kontrol, logo, IslandPreview
    ├── navigation/  NovaDestination, NovaRoot
    └── screens/     home, features, appearance, settings, about
```

Arsitektur lengkap: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

## Privasi

Data diproses di perangkat. Aplikasi hanya meminta izin yang dibutuhkan fitur yang diaktifkan pengguna.

## Lisensi

MIT. Lihat [LICENSE](LICENSE). Ganti nama pemegang hak cipta sesuai kebutuhan.
