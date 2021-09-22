package com.ceiba.padawan.store.vo

import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Post(
    @Id
    var identifier: Long = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("body")
    var body: String? = null
) {
    lateinit var user: ToOne<User>
}
