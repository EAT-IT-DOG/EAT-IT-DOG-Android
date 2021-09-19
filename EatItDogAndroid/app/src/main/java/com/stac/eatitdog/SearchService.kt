package com.stac.eatitdog

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchService { //http://52.79.148.59:4000/search/%EC%B4%88%EC%BD%9C%EB%A6%BF
    @GET("search/{foodname}")
    fun loadNotice(@Path("foodname") foodname: String): Call<SearchData>
}