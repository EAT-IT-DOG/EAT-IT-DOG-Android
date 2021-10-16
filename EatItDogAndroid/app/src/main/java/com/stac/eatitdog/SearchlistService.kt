package com.stac.eatitdog

import retrofit2.Call
import retrofit2.http.GET

interface SearchlistService {
    @GET("list")
    fun loadNotice(): Call<Searchlist>
}