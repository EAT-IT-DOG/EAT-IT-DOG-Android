package com.stac.eatitdog

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {
    @GET("search/{foodname}")
    fun loadNotice(@Path("foodname") foodname: String): Call<SearchData>
}