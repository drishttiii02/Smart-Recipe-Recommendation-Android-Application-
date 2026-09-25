package com.example.reciperecommendation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reciperecommendation.ui.JournalScreen
import com.example.reciperecommendation.ui.LoginScreen
import com.example.reciperecommendation.ui.RecipeDetailScreen
import com.example.reciperecommendation.ui.RecipeListScreen
import com.example.reciperecommendation.ui.SavedRecipesScreen
import com.example.reciperecommendation.ui.SignupScreen
import com.example.reciperecommendation.ui.SplashScreen
import com.example.reciperecommendation.ui.theme.RecipeRecommendationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        setContent {
            RecipeRecommendationTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()
                val recipeViewModel: RecipeViewModel = viewModel()

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(navController = navController)
                    }
                    composable("signup") {
                        SignupScreen(navController = navController, viewModel = loginViewModel)
                    }

                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            viewModel = loginViewModel
                        )
                    }
                    composable("recipes") {
                        RecipeListScreen(
                            navController = navController,
                            viewModel = recipeViewModel
                        )
                    }
                    composable(
                        "recipe_detail/{recipeId}",
                        arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val recipeId =
                            backStackEntry.arguments?.getInt("recipeId") ?: return@composable
                        RecipeDetailScreen(
                            recipeId = recipeId,
                            onBack = { navController.popBackStack() },
                        )
                    }
                    composable("saved_recipes") {
                        SavedRecipesScreen(
                            navController = navController,
                            viewModel = recipeViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("journal_screen") {
                        JournalScreen(
                            viewModel = recipeViewModel,
                            onBack = { navController.popBackStack() },
                            onRecipeClick = { recipe ->
                                navController.navigate("recipe_detail/${recipe.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}



