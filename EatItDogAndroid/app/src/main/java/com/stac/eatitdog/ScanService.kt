package com.stac.eatitdog

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ScanService {
    @GET("barcodes/{barcodenum}")
    fun loadNotice(@Path("barcodenum") barcodenum: String): Call<SearchData>
}