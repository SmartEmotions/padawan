package com.ceiba.padawan.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton Client
object ClientServices {
    val retrofit: Retrofit =  Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}