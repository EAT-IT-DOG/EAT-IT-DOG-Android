package com.stac.eatitdog

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchlistService {//http://52.79.148.59:4000/list
    @GET("list")
    fun loadNotice(): Call<Searchlist>
}