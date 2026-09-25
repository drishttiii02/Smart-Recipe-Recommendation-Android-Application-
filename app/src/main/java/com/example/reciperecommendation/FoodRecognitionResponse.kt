package com.example.reciperecommendation

data class FoodRecognitionResponse(
    val category: String?,
    val imageType: String?,
    val annotations: List<Annotation>?
)

data class Annotation(
    val tag: String,
    val confidence: Float
)
