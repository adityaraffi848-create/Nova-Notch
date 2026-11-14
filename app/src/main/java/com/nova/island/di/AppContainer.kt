package com.nova.island.di

import android.content.Context
import com.nova.island.data.settings.SettingsRepository
import com.nova.island.data.settings.settingsDataStore

/** Dependency container sederhana. Monitor dan service ditambahkan di phase berikutnya. */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(appContext.settingsDataStore)
    }
}
