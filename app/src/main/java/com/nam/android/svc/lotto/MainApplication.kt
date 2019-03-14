package com.nam.android.svc.lotto

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        instance = this
        super.attachBaseContext(base)
    }

    companion object {
        lateinit var instance: Application
            private set
    }
}