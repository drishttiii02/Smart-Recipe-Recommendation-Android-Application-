package com.example.reciperecommendation.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.reciperecommendation.RecipeDetails
import com.example.reciperecommendation.RecipeViewModel
import kotlinx.coroutines.launch

private const val TAG = "RecipeDetailScreen"

@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    onBack: () -> Unit,
    viewModel: RecipeViewModel = viewModel()
) {
    val context = LocalContext.current
    val recipe by viewModel.selectedRecipe.collectAsState()
    val scope = rememberCoroutineScope()
    var isSaved by remember { mutableStateOf(false) }
    var showJournalDialog by remember { mutableStateOf(false) }
    var loadErrorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(recipeId) {
        try {
            viewModel.fetchRecipeDetails(context, recipeId)
            isSaved = try {
                viewModel.isRecipeSaved(context, recipeId)
            } catch (e: Exception) {
                Log.w(TAG, "isRecipeSaved failed: ${e.localizedMessage}")
                false
            }
        } catch (e: Exception) {
            loadErrorMessage = "Failed to load recipe: ${e.localizedMessage}"
            Log.e(TAG, "fetchRecipeDetails error", e)
        }
    }

    // Loading / error UI
    if (recipe == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (loadErrorMessage != null) {
                Text(loadErrorMessage ?: "Unknown error", color = Color.Red)
            } else {
                CircularProgressIndicator(color = Color(0xFFEF6C00))
            }
        }
        return
    }

    val r: RecipeDetails = recipe!!

    Box(modifier = Modifier.fillMaxSize()) {
        // Top image area. This fills full width and fixed height.
        AsyncImage(
            model = r.image,
            contentDescription = r.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)          // taller image for nicer look
                .clip(RoundedCornerShape(0.dp))
        )

        // Dark overlay on the image so white back icon looks fine
        Box(
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.28f))
        )

        // Back button (placed over image, using status bar padding so it isn't hidden)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 12.dp, top = 8.dp)
                .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(50))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
        }

        // Content card that starts overlapping the image (rounded top corners).
        // The Column is vertically scrollable so long content scrolls.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 280.dp)  // overlap the image slightly
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color(0xFF1C1C1E))
                .verticalScroll(rememberScrollState())
                .padding(bottom = 88.dp) // keep space for FAB
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            // Title and badges
            Text(
                text = r.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                DetailBadge(Icons.Default.AccessTime, "${r.readyInMinutes} mins")
                Spacer(modifier = Modifier.width(10.dp))
                DetailBadge(Icons.Default.Group, "${r.servings} servings")
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ---------- Ingredients ----------
            Text(
                text = "Ingredients",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            // Heuristic: if instructions looks like short newline-separated lines (likely ingredients), use them.
            // Otherwise we fallback to "No ingredient information" and rely on Method / link.
            val possibleLines = r.instructions?.split("\n")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
            val ingredientsToShow: List<String> = if (possibleLines.size in 2..40 && possibleLines.all { it.length < 120 }) {
                // Very likely this is a list of ingredients
                possibleLines
            } else {
                // Not a short list -> probably full method text. We might not have ingredients separately.
                emptyList()
            }

            if (ingredientsToShow.isEmpty()) {
                Text(
                    text = "No ingredient information available (try opening source).",
                    fontSize = 15.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            } else {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    ingredientsToShow.forEach { line ->
                        Text("• $line", fontSize = 15.sp, color = Color.LightGray, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ---------- Method / Full Recipe ----------
            Text(
                text = "Method / Full Recipe",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            val methodText = r.instructions?.trim()
            if (!methodText.isNullOrBlank() && methodText.length > 80 && (ingredientsToShow.isEmpty() || methodText.length > 200)) {
                // show method text (if it's long enough to be meaningful)
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    // split into paragraphs for readability
                    methodText.split("\n\n").forEach { para ->
                        Text(text = para.trim(), fontSize = 15.sp, color = Color.LightGray, modifier = Modifier.padding(vertical = 6.dp))
                    }
                }
            } else {
                // show tap-to-open link fallback
                if (!r.sourceUrl.isNullOrBlank()) {
                    Text(
                        text = "Tap to view full recipe on source website",
                        fontSize = 14.sp,
                        color = Color(0xFF81D4FA),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(r.sourceUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Log.w(TAG, "Failed to open source URL: ${e.localizedMessage}")
                                }
                            }
                    )

                    Text(
                        text = r.sourceUrl,
                        fontSize = 12.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                } else {
                    Text(
                        text = "No method available.",
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Journal / Add button
            Button(
                onClick = { showJournalDialog = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF6C00))
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add to Journal", color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Save FAB placed above navigation bar (navigationBarsPadding) and with extra bottom padding
        FloatingActionButton(
            onClick = {
                scope.launch {
                    try {
                        if (isSaved) viewModel.unsaveRecipe(context, r.id)
                        else viewModel.saveRecipe(context, r)
                        isSaved = !isSaved
                    } catch (e: Exception) {
                        Log.w(TAG, "save/unsave failed: ${e.localizedMessage}")
                    }
                }
            },
            containerColor = Color(0xFFEF6C00),
            modifier = Modifier
                .navigationBarsPadding()    // ensure it sits above nav bar on devices with nav bar
                .padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder, contentDescription = null, tint = Color.White)
        }
    }

    // Journal dialog (outside the Box so it overlays correctly)
    if (showJournalDialog) {
        JournalCategoryDialog(
            onDismiss = { showJournalDialog = false },
            onCategorySelected = { category ->
                scope.launch {
                    try {
                        viewModel.updateJournalCategory(context, r.id, category)
                    } catch (e: Exception) {
                        Log.w(TAG, "updateJournalCategory failed: ${e.localizedMessage}")
                    } finally {
                        showJournalDialog = false
                    }
                }
            }
        )
    }
}

@Composable
fun DetailBadge(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFF2C2C2E), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
fun JournalCategoryDialog(
    onDismiss: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Category") },
        text = {
            Column {
                listOf("Breakfast", "Lunch", "Dinner", "Snack").forEach { cat ->
                    TextButton(onClick = { onCategorySelected(cat) }) { Text(cat) }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
