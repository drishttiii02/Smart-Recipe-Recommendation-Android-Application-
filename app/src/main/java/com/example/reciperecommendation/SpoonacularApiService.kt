package com.example.reciperecommendation

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApiService {

    @GET("recipes/findByIngredients")
    suspend fun getRecipes(
        @Query("ingredients") ingredients: String,
        @Query("number") number: Int = 10,
        @Query("intolerances") intolerances: String? = null,
        @Query("apiKey") apiKey: String = ApiClient.API_KEY
    ): Response<List<SpoonRecipe>>

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String = ApiClient.API_KEY
    ): Response<RecipeDetails>

    @Multipart
    @POST("food/images/classify")
    suspend fun classifyImage(
        @Part file: MultipartBody.Part,
        @Query("apiKey") apiKey: String = ApiClient.API_KEY
    ): Response<ImageClassificationResponse>
}

