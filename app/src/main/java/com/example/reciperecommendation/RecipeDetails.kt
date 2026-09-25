package com.example.reciperecommendation

data class RecipeDetails(
    val id: Int,
    val title: String,
    val image: String,
    val instructions: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String
)
