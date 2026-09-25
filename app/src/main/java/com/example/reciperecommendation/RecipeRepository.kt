// RecipeRepository.kt
package com.example.reciperecommendation

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class RecipeRepository {

    private val TAG = "RecipeRepository"

    // Safe access to static list (your file: com.example.reciperecommendation.contant.listOfRecipes)
    private val staticRecipes: List<RecipeEntity>
        get() = try {
            com.example.reciperecommendation.contant.listOfRecipes
        } catch (e: Exception) {
            emptyList()
        }

    /**
     * Flow:
     * 0) staticRecipes (immediately return any matches, but continue)
     * 1) local DB (add items not already emitted)
     * 2) Edamam (save results to DB)
     * 3) Spoonacular (save results to DB; prefer step-by-step instructions)
     *
     * onSuccess will be called multiple times (quick UI updates). onError only if no results at all.
     */
    suspend fun getRecipes(
        context: Context,
        ingredients: List<String>,
        intolerances: List<String> = emptyList(),
        onSuccess: (List<RecipeItem>) -> Unit,
        onError: (String) -> Unit
    ) {
        if (ingredients.isEmpty()) {
            onError("Please enter at least one ingredient")
            return
        }

        val dao = AppDatabase.getDatabase(context).recipeDao()
        val emitted = mutableListOf<RecipeItem>()
        val seenIds = mutableSetOf<Int>()

        fun String?.containsIng(ing: String): Boolean =
            this?.lowercase()?.contains(ing.lowercase()) == true

        // ------------------------------
        // 0) STATIC DATA (always first)
        // ------------------------------
        try {
            val matches = staticRecipes.filter { ent ->
                ingredients.any { ing ->
                    ent.title.containsIng(ing) || (ent.instructions?.containsIng(ing) == true)
                }
            }

            matches.forEach { ent ->
                if (seenIds.add(ent.id)) {
                    val item = RecipeItem(
                        id = ent.id,
                        title = ent.title ?: "Untitled",
                        image = ent.image ?: "",
                        instructions = ent.instructions ?: "",
                        readyInMinutes = ent.readyInMinutes,
                        servings = ent.servings,
                        sourceUrl = ent.sourceUrl ?: "",
                        isSaved = ent.isSaved,
                        journalCategory = ent.journalCategory
                    )
                    emitted.add(item)

                    // best-effort: insert static into DB so it's available later
                    try {
                        withContext(Dispatchers.IO) {
                            dao.insertRecipe(ent)
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Insert static into DB failed: ${e.localizedMessage}")
                    }
                }
            }

            if (emitted.isNotEmpty()) {
                // quick UI update with static matches
                onSuccess(emitted.toList())
                // do NOT return — continue to fetch/append other sources
            }
        } catch (e: Exception) {
            Log.w(TAG, "Static check failed: ${e.localizedMessage}")
        }

        // ------------------------------
        // 1) LOCAL DB (add unseen)
        // ------------------------------
        val localList = withContext(Dispatchers.IO) {
            val list = mutableListOf<RecipeItem>()
            try {
                ingredients.forEach { ing ->
                    dao.searchByIngredient(ing).forEach { ent ->
                        if (seenIds.add(ent.id)) {
                            list.add(
                                RecipeItem(
                                    id = ent.id,
                                    title = ent.title,
                                    image = ent.image,
                                    instructions = ent.instructions,
                                    readyInMinutes = ent.readyInMinutes,
                                    servings = ent.servings,
                                    sourceUrl = ent.sourceUrl,
                                    isSaved = ent.isSaved,
                                    journalCategory = ent.journalCategory
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Local DB search failed: ${e.localizedMessage}")
            }
            list
        }

        if (localList.isNotEmpty()) {
            emitted.addAll(localList)
            onSuccess(emitted.toList()) // update UI with static + local
            // continue to call remote APIs to augment results
        }

        // ------------------------------
        // 2) EDAMAM
        // ------------------------------
        try {
            val edResp = ApiClient.edamamRetrofit.searchRecipes(
                query = ingredients.joinToString(" "),
                appId = ApiClient.EDAMAM_APP_ID,
                appKey = ApiClient.EDAMAM_APP_KEY,
                from = 0,
                to = 20
            )

            if (edResp.isSuccessful) {
                val body = edResp.body()
                val edList = mutableListOf<RecipeItem>()

                body?.hits?.forEach { hit ->
                    val r = hit.recipe ?: return@forEach
                    val uri = r.uri ?: r.label ?: ""
                    val id = stableIdFromUri(uri)
                    if (!seenIds.add(id)) return@forEach

                    // prefer small image if available, else regular, else r.image
                    val image = r.images?.SMALL?.url ?: r.images?.REGULAR?.url ?: r.image ?: ""
                    val instructions = r.ingredientLines?.joinToString("\n") ?: ""
                    val ready = r.totalTime?.toInt() ?: 0
                    val servings = r.yield?.toInt() ?: 0
                    val title = r.label ?: "Untitled"
                    val sourceUrl = r.url ?: r.source ?: ""

                    val item = RecipeItem(
                        id = id,
                        title = title,
                        image = image,
                        instructions = instructions,
                        readyInMinutes = ready,
                        servings = servings,
                        sourceUrl = sourceUrl,
                        isSaved = false,
                        journalCategory = null
                    )

                    edList.add(item)

                    // save to local DB (IO)
                    try {
                        withContext(Dispatchers.IO) {
                            dao.insertRecipe(
                                RecipeEntity(
                                    id = id,
                                    title = title,
                                    image = image,
                                    instructions = instructions,
                                    readyInMinutes = ready,
                                    servings = servings,
                                    sourceUrl = sourceUrl
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "Insert Edamam into DB failed: ${e.localizedMessage}")
                    }
                }

                if (edList.isNotEmpty()) {
                    emitted.addAll(edList)
                    onSuccess(emitted.toList()) // update UI with appended Edamam results
                    // we do NOT return — Spoonacular is a fallback but can be skipped if Edamam sufficient
                }
            } else {
                Log.w(TAG, "Edamam failed: ${edResp.code()} ${edResp.message()}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Edamam call exception: ${e.localizedMessage}")
        }

        // ------------------------------
        // 3) SPOONACULAR (fallback, prefer step-by-step)
        // ------------------------------
        try {
            val spoonResp = ApiClient.retrofit.getRecipes(
                ingredients = ingredients.joinToString(","),
                number = 20,
                intolerances = if (intolerances.isEmpty()) null else intolerances.joinToString(",")
            )

            if (spoonResp.isSuccessful) {
                val body = spoonResp.body()
                val spList = mutableListOf<RecipeItem>()

                body?.forEach { r ->
                    try {
                        val id = r.id
                        if (!seenIds.add(id)) return@forEach

                        val analyzed = flattenAnalyzedInstructions(r.analyzedInstructions)
                        val plain = stripHtml(r.instructions)
                        val finalInstructions = if (analyzed.isNotBlank()) analyzed else plain

                        val item = RecipeItem(
                            id = id,
                            title = r.title ?: "Untitled",
                            image = r.image ?: "",
                            instructions = finalInstructions,
                            readyInMinutes = r.readyInMinutes ?: 0,
                            servings = r.servings ?: 0,
                            sourceUrl = r.sourceUrl ?: "",
                            isSaved = false,
                            journalCategory = null
                        )

                        // Save to DB
                        try {
                            withContext(Dispatchers.IO) {
                                dao.insertRecipe(
                                    RecipeEntity(
                                        id = item.id,
                                        title = item.title,
                                        image = item.image,
                                        instructions = item.instructions,
                                        readyInMinutes = item.readyInMinutes,
                                        servings = item.servings,
                                        sourceUrl = item.sourceUrl
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "Insert Spoon into DB failed: ${e.localizedMessage}")
                        }

                        // Accept only if there's meaningful instructions
                        if (item.instructions.length > 40) {
                            spList.add(item)
                        }

                    } catch (e: Exception) {
                        Log.w(TAG, "Mapping Spoon item failed: ${e.localizedMessage}")
                    }
                }

                if (spList.isNotEmpty()) {
                    emitted.addAll(spList)
                    onSuccess(emitted.toList())
                    return
                } else {
                    Log.i(TAG, "Spoonacular returned no usable items")
                }
            } else {
                Log.w(TAG, "Spoonacular failed: ${spoonResp.code()} ${spoonResp.message()}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Spoonacular exception: ${e.localizedMessage}")
        }

        // final fallback
        if (emitted.isEmpty()) {
            onError("No recipes found from static, local, Edamam, or Spoonacular")
        } else {
            // ensure UI has results at least once
            onSuccess(emitted.toList())
        }
    }

    // ----------------------------
    // Details: prefer local then Spoonacular (optional)
    // ----------------------------
    suspend fun getRecipeDetails(
        context: Context,
        recipeId: Int,
        onSuccess: (RecipeDetails?) -> Unit,
        onError: (String) -> Unit
    ) {
        val dao = AppDatabase.getDatabase(context).recipeDao()
        try {
            val local = withContext(Dispatchers.IO) { dao.getRecipeById(recipeId) }
            if (local != null) {
                onSuccess(
                    RecipeDetails(
                        id = local.id,
                        title = local.title,
                        image = local.image,
                        instructions = local.instructions,
                        readyInMinutes = local.readyInMinutes,
                        servings = local.servings,
                        sourceUrl = local.sourceUrl
                    )
                )
                return
            }

            // Optional: fetch Spoonacular details here if you want richer instructions.
            onError("Recipe details not available")
        } catch (e: Exception) {
            onError("Error: ${e.localizedMessage}")
        }
    }

    // ----------------------------
    // Helpers
    // ----------------------------
    private fun stripHtml(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return raw.replace(Regex("<[^>]*>"), "").trim()
    }

    private fun flattenAnalyzedInstructions(list: List<AnalyzedInstruction>?): String {
        if (list.isNullOrEmpty()) return ""
        val sb = StringBuilder()
        var step = 1
        list.forEach { ai ->
            ai.steps?.forEach { s ->
                val txt = s.step ?: ""
                if (txt.isNotBlank()) {
                    sb.append("$step. ${txt.trim()}\n")
                    step++
                }
            }
        }
        return sb.toString().trim()
    }

    fun stableIdFromUri(uri: String): Int {
        val h = uri.hashCode()
        return if (h == Int.MIN_VALUE) Int.MAX_VALUE else kotlin.math.abs(h)
    }

    // ----------------------------
    // Image upload helpers (unchanged)
    // ----------------------------
    suspend fun prepareImageFilePart(context: Context, imageUri: Uri): MultipartBody.Part =
        withContext(Dispatchers.IO) {
            val input = context.contentResolver.openInputStream(imageUri)
            val file = File(context.cacheDir, "upload_image.jpg")
            FileOutputStream(file).use { out -> input?.copyTo(out) }
            val request = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, request)
        }

    suspend fun uploadImageAndAnalyze(context: Context, imageUri: Uri): List<String>? {
        return try {
            val filePart = prepareImageFilePart(context, imageUri)
            val resp = ApiClient.retrofit.classifyImage(filePart)
            if (resp.isSuccessful) listOfNotNull(resp.body()?.category) else null
        } catch (e: Exception) {
            Log.w(TAG, "Image classify failed: ${e.localizedMessage}")
            null
        }
    }
}
