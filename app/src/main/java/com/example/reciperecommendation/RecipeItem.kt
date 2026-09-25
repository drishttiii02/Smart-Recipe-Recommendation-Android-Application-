package com.example.reciperecommendation

data class RecipeItem(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val instructions: String,
    val sourceUrl: String,
    val journalCategory: String?,
    val isSaved: Boolean,
    val servings: Int
)
