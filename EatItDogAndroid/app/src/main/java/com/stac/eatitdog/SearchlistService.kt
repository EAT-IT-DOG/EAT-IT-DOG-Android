package com.stac.eatitdog

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchlistService {
    @GET("list")
    fun loadNotice(): Call<Searchlist>
}