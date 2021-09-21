package com.ceiba.padawan.store

import android.content.Context
import com.ceiba.padawan.data.MyObjectBox
import io.objectbox.BoxStore

// Singleton Box
object ObjectBox {
    lateinit var store: BoxStore
        private set

    fun init(context: Context) {
        // TODO What are we going to do about migrations on schema ?
        store = MyObjectBox.builder().baseDirectory( context.filesDir ).name("{{name}}")
            .androidContext(context.applicationContext)
            .build()
    }
}