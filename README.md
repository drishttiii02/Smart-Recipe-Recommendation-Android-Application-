# Recipe Recommendation

An Android recipe discovery app built with Kotlin and Jetpack Compose. Search with ingredients, explore recipe details, save recipes, and organize meal ideas in a food journal.

## Features

- Sign up and log in with a username and password.
- Search recipes using one or more comma-separated ingredients.
- Choose intolerance filters for recipe searches.
- View recipe images, preparation time, servings, instructions, and source links when available.
- Save recipes for later.
- Add recipes to the Breakfast, Lunch, Dinner, or Snack journal categories.
- Select a food image from the camera or gallery for food recognition.
- Store user and recipe data locally with Room.

## App Walkthrough

1. Open the app and continue through the splash screen.
2. Create an account, then log in. Accounts are stored on the device.
3. Enter ingredients such as `tomato, onion` and search.
4. Browse matching recipe cards. Results can arrive from local sample data, the local database, and online recipe services; exact results depend on available data and network/API access.
5. Open a recipe to view its details, save it, or add it to a journal category.
6. Open Saved Recipes or the Food Journal to revisit saved items.

The app's visible output is a recipe list followed by recipe detail, saved-recipe, and journal screens. Recipe data may include a title, image, preparation time, servings, instructions, and a source link. If remote services are unavailable, local matches may still be shown.

## Tech Stack

- Kotlin 2.0.21 and Android Gradle Plugin 8.10.1
- Jetpack Compose and Material 3
- Navigation Compose
- Room for local SQLite persistence
- Retrofit and Gson for API requests
- Coil for loading recipe images
- Spoonacular and Edamam API integrations

## Requirements

- Android Studio with Android SDK 35 installed
- JDK 17 for the Android Gradle Plugin
- Android device or emulator running Android 7.0 (API 24) or later
- Internet access for online recipe search and food recognition

## Build and Run

From the project root in PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:installDebug
```

With an emulator or device connected and ADB available, launch the installed app with:

```powershell
adb shell monkey -p com.example.reciperecommendation 1
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Run local unit tests with:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

## Data and API Notes

- Recipes and accounts are stored in the on-device Room database (`recipes_db`). App data is local to the device and is not synchronized to an account server.
- Recipe search checks bundled sample data and the local database, then requests results from Edamam and Spoonacular. Results may update as additional sources return.
- Food image recognition uses the Spoonacular integration and requires working credentials and network access.
- Some API calls may fail because of missing, invalid, or rate-limited credentials. Local recipe matches can still be available.

## Security Notice

API credentials are currently hard-coded in the app source. Do not publish or reuse exposed credentials: revoke or rotate them with their providers, then move replacement credentials into a local, untracked configuration before using the integrations. The current local login is for demonstration only; passwords are stored in the device database and are not suitable for production authentication.

## Project Structure

```text
app/src/main/java/com/example/reciperecommendation/
  ui/             Compose screens
  contant/        Bundled sample recipe data
  *Repository.kt  Recipe and user data access
  *Dao.kt         Room database queries
  *Entity.kt      Room database models
```