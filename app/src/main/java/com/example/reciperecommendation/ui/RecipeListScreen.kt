package com.example.reciperecommendation.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.reciperecommendation.ApiClient
import com.example.reciperecommendation.RecipeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun RecipeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RecipeViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        val res = ApiClient.edamamRetrofit.searchRecipes(query = "chicken")

        println("STATUS = ${res.code()}")
        println("BODY = ${res.body()}")

    }
    val context = LocalContext.current
    val activity = context as Activity
    val recipes by viewModel.recipes.collectAsState()

    var ingredientInput by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedIntolerances by remember { mutableStateOf<List<String>>(emptyList()) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val detectedIngredients by viewModel.detectedIngredients.collectAsState()

    val intolerances = listOf("Dairy", "Egg", "Gluten", "Grain", "Peanut", "Seafood", "Sesame", "Shellfish", "Soy", "Sulfite", "Tree Nut", "Wheat")

    val toastMessage by viewModel.toastMessage.collectAsState()
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                viewModel.analyzeImageAndFetchRecipes(context, it)
            }
        } else {
            Toast.makeText(context, "Image capture cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(detectedIngredients) {
        if (detectedIngredients.isNotEmpty()) {
            ingredientInput = detectedIngredients.joinToString(", ")
            Toast.makeText(context, "Autofilled from image: $ingredientInput", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFF1B1B1B), Color(0xFF121212))))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Recipes",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("journal_screen") }) {
                            Icon(
                                Icons.Default.Book,
                                contentDescription = "Journal",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { navController.navigate("saved_recipes") }) {
                            Icon(
                                Icons.Default.Bookmark,
                                contentDescription = "Saved",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1F1F1F), // richer dark
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFF2E2E2E), Color(0xFF1F1F1F))
                            )
                        ),
                )
            }

                ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Find Delicious Recipes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = ingredientInput,
                    onValueChange = { ingredientInput = it },
                    placeholder = { Text("Enter ingredients (comma-separated)", color = Color.Gray) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { showDialog = true }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Set Intolerances", tint = Color.White)
                            }
                            IconButton(onClick = { showImagePickerDialog = true }) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Camera or Gallery", tint = Color.White)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF81C784),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF81C784),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )


                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val ingredients = ingredientInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        if (ingredients.isNotEmpty()) {
                            viewModel.fetchRecipesWithFallback(
                                context = context,
                                ingredients = ingredients,
                                intolerances = selectedIntolerances
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C), contentColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Get Recipes", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (recipes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No recipes yet. Enter ingredients to search!", color = Color.Gray)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recipes) { recipe ->
                            RecipeCard(recipe.title, recipe.image) {
                                navController.navigate("recipe_detail/${recipe.id}")
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                IntoleranceDialog(
                    intolerances = intolerances,
                    selectedIntolerances = selectedIntolerances,
                    onDismiss = { showDialog = false },
                    onApply = {
                        selectedIntolerances = it
                        showDialog = false
                    }
                )
            }

            if (showImagePickerDialog) {
                AlertDialog(
                    onDismissRequest = { showImagePickerDialog = false },
                    title = { Text("Select Image Source") },
                    text = {
                        Column {
                            Text("Camera", modifier = Modifier.fillMaxWidth().clickable {
                                showImagePickerDialog = false
                                ImagePicker.with(activity).crop().compress(1024).maxResultSize(1080, 1080).cameraOnly().createIntent { launcher.launch(it) }
                            }.padding(12.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Gallery", modifier = Modifier.fillMaxWidth().clickable {
                                showImagePickerDialog = false
                                ImagePicker.with(activity).crop().compress(1024).maxResultSize(1080, 1080).galleryOnly().createIntent { launcher.launch(it) }
                            }.padding(12.dp))
                        }
                    },
                    confirmButton = {},
                    dismissButton = {}
                )
            }
        }
    }
}


@Composable
fun IntoleranceDialog(
    intolerances: List<String>,
    selectedIntolerances: List<String>,
    onDismiss: () -> Unit,
    onApply: (List<String>) -> Unit
) {
    var currentSelection by remember { mutableStateOf(selectedIntolerances) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onApply(currentSelection) }) { Text("Apply") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Select Intolerances") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                items(intolerances.size) { index ->
                    val item = intolerances[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = currentSelection.contains(item),
                            onCheckedChange = { checked ->
                                currentSelection = if (checked)
                                    currentSelection + item else currentSelection - item
                            }
                        )
                        Text(item, fontSize = 16.sp)
                    }
                }
            }
        }
    )
}


@Composable


fun RecipeCard(
    title: String,
    imageUrl: String?,          // nullable to be safe
    onClick: () -> Unit
) {
    // Card width is controlled by parent grid; we keep a nice fixed height so images look uniform.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)                      // taller card for better image visibility
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image area - fills the top portion but we keep it covering entire card for a modern look
            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()                 // fill the whole card
                )
            } else {
                // fallback box when no image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF121212)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No image", color = Color.Gray, fontSize = 12.sp)
                }
            }

            // Dark gradient at bottom to make title readable regardless of image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(76.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xAA000000))
                        )
                    )
            )

            // Title placed over the gradient at bottom-left
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}


