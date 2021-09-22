package com.ceiba.padawan.store

import com.ceiba.padawan.store.vo.User
import io.objectbox.Box

object ObjectBoxStores {
    val userStore: Box<User> = ObjectBox.store.boxFor(User::class.java)
}