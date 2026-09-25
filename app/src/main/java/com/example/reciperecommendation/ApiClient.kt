package com.example.reciperecommendation

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val API_KEY = "bd272fb408d14cb683ca48dc4fbf18a0"

    val retrofit: SpoonacularApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonacularApiService::class.java)
    }

    const val EDAMAM_APP_ID = "ab8e81e9"        // <-- replace this
    const val EDAMAM_APP_KEY = "b43a3c519e7fefa0466f993ea56c339e" // your key (keep safe)
    const val EDAMAM_USER_ID = "drishtii"


    val edamamRetrofit: EdamamApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.edamam.com/")   // correct
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EdamamApiService::class.java)

}
}
