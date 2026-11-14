package com.nova.island.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val ENABLED = booleanPreferencesKey("island_enabled")
        val ANIMATION_EFFECTS = booleanPreferencesKey("animation_effects")
        val AMOLED = booleanPreferencesKey("amoled_theme")
        val SIZE = intPreferencesKey("size_percent")
        val HEIGHT = intPreferencesKey("height_dp")
        val RADIUS = intPreferencesKey("radius_dp")
        val SPEED = intPreferencesKey("animation_speed_percent")
        val OPACITY = intPreferencesKey("background_opacity_percent")
        val GRADIENT = intPreferencesKey("gradient_intensity_percent")
    }

    private val defaults = IslandSettings()

    val settings: Flow<IslandSettings> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { p ->
            IslandSettings(
                islandEnabled = p[Keys.ENABLED] ?: defaults.islandEnabled,
                animationEffects = p[Keys.ANIMATION_EFFECTS] ?: defaults.animationEffects,
                amoledTheme = p[Keys.AMOLED] ?: defaults.amoledTheme,
                sizePercent = p[Keys.SIZE] ?: defaults.sizePercent,
                heightDp = p[Keys.HEIGHT] ?: defaults.heightDp,
                radiusDp = p[Keys.RADIUS] ?: defaults.radiusDp,
                animationSpeedPercent = p[Keys.SPEED] ?: defaults.animationSpeedPercent,
                backgroundOpacityPercent = p[Keys.OPACITY] ?: defaults.backgroundOpacityPercent,
                gradientIntensityPercent = p[Keys.GRADIENT] ?: defaults.gradientIntensityPercent,
            )
        }

    suspend fun setIslandEnabled(enabled: Boolean) {
        dataStore.edit { it[Keys.ENABLED] = enabled }
    }

    suspend fun setAnimationEffects(enabled: Boolean) {
        dataStore.edit { it[Keys.ANIMATION_EFFECTS] = enabled }
    }

    suspend fun saveAppearance(s: IslandSettings) {
        dataStore.edit {
            it[Keys.AMOLED] = s.amoledTheme
            it[Keys.SIZE] = s.sizePercent
            it[Keys.HEIGHT] = s.heightDp
            it[Keys.RADIUS] = s.radiusDp
            it[Keys.SPEED] = s.animationSpeedPercent
            it[Keys.OPACITY] = s.backgroundOpacityPercent
            it[Keys.GRADIENT] = s.gradientIntensityPercent
        }
    }

    suspend fun resetAppearance() = saveAppearance(defaults)

    suspend fun resetAll() {
        dataStore.edit { it.clear() }
    }
}
