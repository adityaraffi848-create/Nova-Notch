package com.nova.island.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nova.island.data.settings.IslandSettings
import com.nova.island.data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: SettingsRepository) : ViewModel() {

    val settings: StateFlow<IslandSettings> = repository.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = IslandSettings(),
    )

    fun setIslandEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setIslandEnabled(enabled) }
    }

    fun setAnimationEffects(enabled: Boolean) {
        viewModelScope.launch { repository.setAnimationEffects(enabled) }
    }

    fun saveAppearance(settings: IslandSettings) {
        viewModelScope.launch { repository.saveAppearance(settings) }
    }

    fun resetAppearance() {
        viewModelScope.launch { repository.resetAppearance() }
    }

    fun resetAll() {
        viewModelScope.launch { repository.resetAll() }
    }

    companion object {
        fun factory(repository: SettingsRepository): ViewModelProvider.Factory =
            viewModelFactory { initializer { MainViewModel(repository) } }
    }
}
