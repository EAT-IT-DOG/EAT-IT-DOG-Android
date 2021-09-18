package com.example.eatitdogandroid

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {//https://eatitdog.loca.lt/search/초콜릿
    @GET("search/{foodname}")
    fun loadNotice(@Path("foodname") foodname: String): Call<SearchData>
}