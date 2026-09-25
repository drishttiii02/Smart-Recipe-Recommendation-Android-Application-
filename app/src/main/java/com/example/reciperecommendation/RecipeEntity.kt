package com.example.reciperecommendation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val instructions: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String,
    val isSaved: Boolean = false,
    val journalCategory: String? = null,
    val type: String = "saved" ,


)

