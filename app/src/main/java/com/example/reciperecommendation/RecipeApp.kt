package com.example.reciperecommendation

import android.app.Application
import com.example.reciperecommendation.contant.listOfRecipes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val dao = AppDatabase.getDatabase(this).recipeDao()

        CoroutineScope(Dispatchers.IO).launch {

            dao.insertRecipes(listOfRecipes)


        }
    }
}