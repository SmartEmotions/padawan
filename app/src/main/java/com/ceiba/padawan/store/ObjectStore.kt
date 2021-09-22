package com.ceiba.padawan.store

import android.app.Application
import com.ceiba.padawan.store.ObjectBox.init

// Singleton box instance
class ObjectStore : Application() {
    override fun onCreate() {
        super.onCreate()
        init(this)
    }
}
