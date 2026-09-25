package com.example.reciperecommendation

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface EdamamApiService {
    @Headers("Edamam-Account-User: drishtii")
    @GET("api/recipes/v2")
    suspend fun searchRecipes(
        @Query("type") type: String = "public",
        @Query("q") query: String,
        @Query("app_id") appId: String = ApiClient.EDAMAM_APP_ID,
        @Query("app_key") appKey: String = ApiClient.EDAMAM_APP_KEY,
        @Query("from") from: Int = 0,
        @Query("to") to: Int = 10
    ): Response<EdamamResponse>
}