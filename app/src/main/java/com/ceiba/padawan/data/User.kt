package com.ceiba.padawan.data

import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class User (
    @Id
    var identifier: Long = 0,
    @SerializedName("id" )
    var id: Int = 0,
    @SerializedName("name" )
    var name: String? = null,
    @SerializedName("username" )
    var username: String? = null,
    @SerializedName("email" )
    var email: String? = null,
    @SerializedName("phone" )
    var phone: String? = null,
) {
    lateinit var posts: ToMany<Post>
}