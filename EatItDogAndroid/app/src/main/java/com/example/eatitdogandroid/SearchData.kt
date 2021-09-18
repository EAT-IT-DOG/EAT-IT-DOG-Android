package com.example.eatitdogandroid

data class SearchData(
    val barcodeNumber: Int,
    val edible: String,
    val feedMethod: String,
    val foodName: String,
    val ingredient: String,
    val safetyGrade: String,
    val safetyLevel: Int,
    val symptom: String
)