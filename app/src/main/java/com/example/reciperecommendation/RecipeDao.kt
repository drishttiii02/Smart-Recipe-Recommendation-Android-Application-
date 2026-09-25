package com.example.reciperecommendation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


    @Dao
    interface RecipeDao {

        @Query("SELECT * FROM recipes WHERE title LIKE '%' || :ingredient || '%'")
        suspend fun searchByIngredient(ingredient: String): List<RecipeEntity>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertRecipe(recipe: RecipeEntity)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertRecipes(recipes: List<RecipeEntity>)

        @Query("SELECT * FROM recipes WHERE id = :id")
        suspend fun getRecipeById(id: Int): RecipeEntity?

        @Query("SELECT * FROM recipes")
        suspend fun getAllRecipes(): List<RecipeEntity>

        @Query("DELETE FROM recipes WHERE id = :id")
        suspend fun deleteRecipeById(id: Int)

        @Query("UPDATE recipes SET isSaved = :isSaved WHERE id = :id")
        suspend fun updateSaveStatus(id: Int, isSaved: Boolean)

        @Query("SELECT * FROM recipes WHERE isSaved = 1")
        suspend fun getSavedRecipes(): List<RecipeEntity>

        @Query("SELECT * FROM recipes WHERE journalCategory IS NOT NULL")
        suspend fun getJournalRecipes(): List<RecipeEntity>

        @Query("SELECT * FROM recipes WHERE journalCategory = :category")
        suspend fun getRecipesByCategory(category: String): List<RecipeEntity>

        @Query("UPDATE recipes SET journalCategory = :category WHERE id = :recipeId")
        suspend fun updateJournalCategory(recipeId: Int, category: String?)

        // Optional: streaming version (Flow) for saved recipes (useful for UI observing)
        @Query("SELECT * FROM recipes WHERE isSaved = 1")
        fun observeSavedRecipes(): Flow<List<RecipeEntity>>
    }

