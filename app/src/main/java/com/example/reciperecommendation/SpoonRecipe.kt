package com.example.reciperecommendation

import com.google.gson.annotations.SerializedName

data class SpoonRecipe(

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String?,

    @SerializedName("image")
    val image: String?,

    // For findByIngredients API:
    @SerializedName("usedIngredients")
    val usedIngredients: List<SpoonIngredient>? = null,

    @SerializedName("missedIngredients")
    val missedIngredients: List<SpoonIngredient>? = null,

    // For detailed recipe response:
    @SerializedName("instructions")
    val instructions: String? = null,

    @SerializedName("analyzedInstructions")
    val analyzedInstructions: List<AnalyzedInstruction>? = null,

    @SerializedName("readyInMinutes")
    val readyInMinutes: Int? = null,

    @SerializedName("servings")
    val servings: Int? = null,

    @SerializedName("sourceUrl")
    val sourceUrl: String? = null
)

data class SpoonIngredient(
    @SerializedName("original")
    val original: String? = null
)

/**
 * Steps inside analyzedInstructions
 */
data class AnalyzedInstruction(
    @SerializedName("steps")
    val steps: List<SpoonStep>? = null
)

data class SpoonStep(
    @SerializedName("number")
    val number: Int? = null,

    @SerializedName("step")
    val step: String? = null
)
