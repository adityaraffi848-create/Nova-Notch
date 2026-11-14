package com.nova.island

import android.app.Application
import com.nova.island.di.AppContainer

class NovaApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
