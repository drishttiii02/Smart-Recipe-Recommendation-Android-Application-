package com.example.reciperecommendation

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * RecipeViewModel - safe DB + network usage
 */
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val recipeRepository = RecipeRepository()

    private val _recipes = MutableStateFlow<List<RecipeItem>>(emptyList())
    val recipes: StateFlow<List<RecipeItem>> = _recipes.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<RecipeDetails?>(null)
    val selectedRecipe: StateFlow<RecipeDetails?> = _selectedRecipe.asStateFlow()

    private val _savedRecipes = MutableStateFlow<List<RecipeItem>>(emptyList())
    val savedRecipes: StateFlow<List<RecipeItem>> = _savedRecipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _detectedIngredients = MutableStateFlow<List<String>>(emptyList())
    val detectedIngredients: StateFlow<List<String>> = _detectedIngredients.asStateFlow()

    private val _journalRecipes = MutableStateFlow<Map<String, List<RecipeItem>>>(emptyMap())
    val journalRecipes: StateFlow<Map<String, List<RecipeItem>>> = _journalRecipes.asStateFlow()

    fun clearToast() {
        _toastMessage.value = null
    }

    /**
     * Fetch recipes: tries repository pipeline (Edamam -> Spoonacular -> local)
     */
    fun fetchRecipesWithFallback(
        context: Context,
        ingredients: List<String>,
        intolerances: List<String> = emptyList()
    ) {
        if (ingredients.isEmpty()) {
            _toastMessage.value = "Please enter at least one ingredient"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                recipeRepository.getRecipes(
                    context = context,
                    ingredients = ingredients,
                    intolerances = intolerances,
                    onSuccess = { listOfRecipes ->
                        _recipes.update { listOfRecipes }
                        _isLoading.value = false
                    },
                    onError = { errorMsg ->
                        _isLoading.value = false
                        _toastMessage.value = errorMsg ?: "Failed to fetch recipes"
                    }
                )
            } catch (e: Exception) {
                _isLoading.value = false
                _toastMessage.value = "Fetch failed: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Fetch recipe details (prefers local cache first inside repository)
     * repository.getRecipeDetails is suspend and uses callbacks (keeps existing design)
     */
    fun fetchRecipeDetails(context: Context, recipeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                recipeRepository.getRecipeDetails(
                    context = context,
                    recipeId = recipeId,
                    onSuccess = { recipe ->
                        _selectedRecipe.value = recipe
                        _isLoading.value = false
                    },
                    onError = { errorMsg ->
                        _isLoading.value = false
                        _toastMessage.value = errorMsg ?: "Failed to load recipe details"
                    }
                )
            } catch (e: Exception) {
                _isLoading.value = false
                _toastMessage.value = "Load details failed: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Save recipe to local DB and refresh saved list.
     * Runs DB op on Dispatchers.IO
     */
    fun saveRecipe(context: Context, recipe: RecipeDetails) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(context).recipeDao()
                    dao.insertRecipe(
                        RecipeEntity(
                            id = recipe.id,
                            title = recipe.title,
                            image = recipe.image,
                            instructions = recipe.instructions ?: "",
                            readyInMinutes = recipe.readyInMinutes,
                            servings = recipe.servings,
                            sourceUrl = recipe.sourceUrl,
                            isSaved = true
                        )
                    )
                }
                loadSavedRecipes(context) // will run its own IO internally
                _toastMessage.value = "Saved"
            } catch (e: Exception) {
                _toastMessage.value = "Save failed: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Unsave (delete) recipe and refresh saved / journal lists.
     */
    fun unsaveRecipe(context: Context, recipeId: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(context).recipeDao()
                    dao.deleteRecipeById(recipeId)
                }
                loadSavedRecipes(context)
                loadJournalRecipes(context)
                _toastMessage.value = "Removed from saved"
            } catch (e: Exception) {
                _toastMessage.value = "Remove failed: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Load saved recipes from DB (runs DAO on IO)
     */
    fun loadSavedRecipes(context: Context) {
        viewModelScope.launch {
            try {
                val saved = withContext(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(context).recipeDao()
                    dao.getSavedRecipes()
                }
                _savedRecipes.value = saved.map {
                    RecipeItem(
                        id = it.id,
                        title = it.title,
                        image = it.image,
                        instructions = it.instructions,
                        readyInMinutes = it.readyInMinutes,
                        servings = it.servings,
                        sourceUrl = it.sourceUrl,
                        isSaved = it.isSaved,
                        journalCategory = it.journalCategory
                    )
                }
            } catch (e: Exception) {
                _toastMessage.value = "Load saved failed: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Convenience suspend helpers left available for callers that want them:
     */
    suspend fun isRecipeSaved(context: Context, recipeId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val dao = AppDatabase.getDatabase(context).recipeDao()
            dao.getRecipeById(recipeId)?.isSaved == true
        }
    }


    /**
     * Journal helpers
     */
    fun loadJournalRecipes(context: Context) {
        viewModelScope.launch {
            try {
                val entities = withContext(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(context).recipeDao()
                    // ensure your DAO provides getJournalRecipes(); it's expected to return List<RecipeEntity>
                    dao.getJournalRecipes()
                }

                val grouped = entities.map { entity ->
                    RecipeItem(
                        id = entity.id,
                        title = entity.title,
                        image = entity.image,
                        instructions = entity.instructions,
                        readyInMinutes = entity.readyInMinutes,
                        servings = entity.servings,
                        sourceUrl = entity.sourceUrl,
                        isSaved = entity.isSaved,
                        journalCategory = entity.journalCategory
                    )
                }.groupBy { it.journalCategory ?: "Other" }

                _journalRecipes.value = grouped
            } catch (e: Exception) {
                _toastMessage.value = "Load journal failed: ${e.localizedMessage}"
            }
        }
    }

    fun updateJournalCategory(context: Context, recipeId: Int, category: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).recipeDao()
                        .updateJournalCategory(recipeId, category)
                }
                loadJournalRecipes(context)
                _toastMessage.value = "Updated category"
            } catch (e: Exception) {
                _toastMessage.value = "Update failed: ${e.localizedMessage}"
            }
        }
    }

    fun deleteRecipeFromJournal(context: Context, recipeId: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val dao = AppDatabase.getDatabase(context).recipeDao()
                    dao.updateJournalCategory(recipeId, null)
                }
                loadJournalRecipes(context)
                _toastMessage.value = "Removed from journal"
            } catch (e: Exception) {
                _toastMessage.value = "Operation failed: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Detected ingredients from image analysis
     */
    fun updateDetectedIngredients(newIngredients: List<String>) {
        _detectedIngredients.value = newIngredients
    }

    /**
     * Analyze uploaded image via repository and fetch recipes using the first detected ingredient as fallback
     */
    fun analyzeImageAndFetchRecipes(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // repository.uploadImageAndAnalyze is suspend (and will use IO internally)
                val ingredients = recipeRepository.uploadImageAndAnalyze(context, imageUri)
                _isLoading.value = false
                if (!ingredients.isNullOrEmpty()) {
                    updateDetectedIngredients(ingredients)
                    val firstIngredient = ingredients.firstOrNull()
                    if (firstIngredient != null) {
                        fetchRecipesWithFallback(context, listOf(firstIngredient))
                    }
                } else {
                    _toastMessage.value = "No ingredients detected from image"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _toastMessage.value = "Image analysis failed: ${e.localizedMessage}"
            }
        }
    }
}
