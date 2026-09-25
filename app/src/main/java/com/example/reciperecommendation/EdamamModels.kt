

data class EdamamHit(
    val recipe: EdamamRecipe
)

data class EdamamRecipe(
    val uri: String,
    val label: String,
    val image: String,
    val images: Images?,            // IMPORTANT (Edamam V2 gives multiple resolutions)
    val source: String,
    val url: String,
    val shareAs: String?,
    val yield: Double?,             // yield is DOUBLE (not Int)
    val dietLabels: List<String>?,
    val healthLabels: List<String>?,
    val ingredientLines: List<String>,
    val ingredients: List<Ingredient>?,
    val calories: Double?,
    val totalWeight: Double?,
    val cuisineType: List<String>?,
    val mealType: List<String>?,
    val dishType: List<String>?,
    val totalTime: Double
)

data class Ingredient(
    val text: String?,
    val weight: Double?
)

data class Images(
    val THUMBNAIL: ImageSize?,
    val SMALL: ImageSize?,
    val REGULAR: ImageSize?,
    val LARGE: ImageSize?
)

data class ImageSize(
    val url: String?,
    val width: Int?,
    val height: Int?
)
