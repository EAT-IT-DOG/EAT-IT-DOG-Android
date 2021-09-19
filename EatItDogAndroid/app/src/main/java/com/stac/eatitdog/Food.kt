package com.stac.eatitdog

data class Food(
    val __v: Int,
    val _id: String,
    val barcodeNumber: List<Long>,
    val edible: String,
    val feedMethod: List<String>,
    val foodName: String,
    val ingredient: List<String>,
    val safetyGrade: String,
    val safetyLevel: Int,
    val symptom: List<String>
)