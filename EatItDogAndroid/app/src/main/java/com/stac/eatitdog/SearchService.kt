package com.stac.eatitdog

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchService {
    @GET("search/{foodname}")
    fun loadNotice(@Path("foodname") foodname: String): Call<SearchData>
}