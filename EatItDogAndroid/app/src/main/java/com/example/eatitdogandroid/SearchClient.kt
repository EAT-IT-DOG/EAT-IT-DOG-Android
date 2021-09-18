package com.example.eatitdogandroid

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchClient {
    private const val baseUrl = "https://eatitdog.loca.lt/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(SearchService::class.java)!!
}