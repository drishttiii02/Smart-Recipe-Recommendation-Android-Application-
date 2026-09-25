package com.example.reciperecommendation.contant

import com.example.reciperecommendation.RecipeEntity

val listOfRecipes = listOf<RecipeEntity>(
    RecipeEntity(
        id = 101,
        title = "Paneer Butter Masala",
        image = "https://i.pinimg.com/1200x/75/25/c2/7525c28b815e93b8f4ad4a3bb889090e.jpg",


        instructions = """
        1. Prepare Cashew Paste:
           Blend soaked cashews into a smooth paste and keep aside.
           <br>

        2. Make the Masala Base:
           Heat 1 tbsp oil + 1 tbsp butter in a pan. Add chopped onions, garlic, and ginger. Sauté till golden.
           <br>
        3. Add Tomatoes and Spices:
           Add tomato puree, cook till oil separates. Add chili powder, turmeric, and coriander powder.
           <br>
        4. Mix Cashew Paste:
           Add cashew paste and cook for 2 mins. Adjust with water for desired gravy consistency.
           <br>
        5. Add Paneer:
           Add paneer cubes and simmer for 5–7 mins. Finish with cream and kasuri methi.
           <br>
        6. Serve:
           Serve hot with naan or rice.
    """.trimIndent(),

        readyInMinutes = 30,
        servings = 2,
        sourceUrl = "https://example.com/paneer-butter-masala"
    ),

    RecipeEntity(
        id = 107,
        title = "Palak Paneer",
        image = "https://www.indianveggiedelight.com/wp-content/uploads/2017/10/palak-paneer-recipe-featured.jpg",
        instructions = "1.Blanch Spinach:\n" +
                "Boil water, add spinach leaves for 2 mins, then blend to puree.\n" +
                "\n" +
                "2.Make Masala:\n" +
                "Heat oil, sauté onions and ginger-garlic, then add tomatoes.\n" +
                "\n" +
                "3.Add Spices:\n" +
                "Add turmeric, chili powder, and garam masala.\n" +
                "\n" +
                "4.Combine Spinach:\n" +
                "Add spinach puree and cook for 2–3 mins.\n" +
                "\n" +
                "5.Add Paneer:\n" +
                "Add paneer cubes and simmer for 5 mins.\n" +
                "\n" +
                "6.Serve:\n" +
                "Serve with roti or rice.",
        readyInMinutes = 30,
        servings = 2,
        sourceUrl = "https://example.com/palak-paneer"
    ),
    RecipeEntity(
        id = 107,
        title = "Palak Paneer",
        image = "https://i.pinimg.com/736x/39/ad/9d/39ad9d2315b8b4c3c49c70d8c6859d07.jpg",
        instructions = "1.Blanch Spinach:\n" +
                "Boil water, add spinach leaves for 2 mins, then blend to puree.\n" +
                "\n" +
                "2.Make Masala:\n" +
                "Heat oil, sauté onions and ginger-garlic, then add tomatoes.\n" +
                "\n" +
                "3.Add Spices:\n" +
                "Add turmeric, chili powder, and garam masala.\n" +
                "\n" +
                "4.Combine Spinach:\n" +
                "Add spinach puree and cook for 2–3 mins.\n" +
                "\n" +
                "5.Add Paneer:\n" +
                "Add paneer cubes and simmer for 5 mins.\n" +
                "\n" +
                "6.Serve:\n" +
                "Serve with roti or rice.",
        readyInMinutes = 30,
        servings = 2,
        sourceUrl = "https://example.com/palak-paneer"
    ),
    RecipeEntity(
        id = 112,
        title = "Matar Paneer",
        image = "https://i.pinimg.com/736x/47/3c/ac/473cac7258421fa99725ae00b3abc187.jpg",
        instructions = "1.Make Onion-Tomato Base:\n" +
                "Heat oil, sauté onions till golden. Add ginger-garlic and tomatoes.\n" +
                "\n" +
                "2.Add Spices:\n" +
                "Add coriander powder, turmeric, chili powder, and salt.\n" +
                "\n" +
                "3.Cook Peas:\n" +
                "Add green peas and cook for 5 mins.\n" +
                "\n" +
                "4.Add Paneer:\n" +
                "Add paneer cubes with water, simmer for 8 mins.\n" +
                "\n" +
                "5.Garnish:\n" +
                "Top with coriander and serve hot.",
        readyInMinutes = 30,
        servings = 3,
        sourceUrl = "https://example.com/matar-paneer"
    ),
    RecipeEntity(
        id = 102,
        title = "Chole Masala",
        image = "https://www.indianveggiedelight.com/wp-content/uploads/2021/09/chole-masala.jpg",
        instructions = "1.Soak Chickpeas:\n" +
                "Soak chickpeas overnight, then boil until soft.\n" +
                "\n" +
                "2.Make Masala:\n" +
                "Heat oil, sauté onions, ginger-garlic paste, and tomatoes.\n" +
                "\n" +
                "3.Add Spices:\n" +
                "Add chole masala, cumin, chili powder, and salt.\n" +
                "\n" +
                "4.Combine:\n" +
                "Add boiled chickpeas with water, mix well.\n" +
                "\n" +
                "5.Simmer:\n" +
                "Cook for 10 mins until gravy thickens.\n" +
                "\n" +
                "6.Serve:\n" +
                "Garnish with coriander and serve with bhature.",
        readyInMinutes = 40,
        servings = 4,
        sourceUrl = "https://example.com/chole-masala"
    ),
    RecipeEntity(
        id = 103,
        title = "Vegetable Pulao",
        image = "https://i.pinimg.com/736x/05/c4/f5/05c4f5b465461df7fa87ee27f34dab6f.jpg",
        instructions = "1.Sauté Spices:\n" +
                "Heat oil in a pan, add whole spices and sauté.\n" +
                "\n" +
                "2.Add Veggies:\n" +
                "Add chopped vegetables and sauté for 3 mins.\n" +
                "\n" +
                "3.Add Rice:\n" +
                "Add soaked basmati rice and mix gently.\n" +
                "\n" +
                "4.Cook:\n" +
                "Add water and salt, cover and cook until rice is fluffy.\n" +
                "\n" +
                "5.Serve:\n" +
                "Serve with raita or pickle.",
        readyInMinutes = 25,
        servings = 3,
        sourceUrl = "https://i.pinimg.com/736x/05/c4/f5/05c4f5b465461df7fa87ee27f34dab6f.jpg"
    ),
    RecipeEntity(
        id = 142,
        title = "Lemon Rice",
        image = "https://i.pinimg.com/736x/31/7c/d6/317cd62849557d086aafbdeecc29db70.jpg",
        instructions = "1.Cook Rice:\n" +
                "Cook 1 cup rice, cool it slightly.\n\n" +
                "2.Prepare Tempering:\n" +
                "Heat 2 tsp oil, add mustard seeds, curry leaves, and green chilies.\n\n" +
                "3.Add Spices:\n" +
                "Add turmeric and a pinch of hing. Fry for a few seconds.\n\n" +
                "4.Mix Rice:\n" +
                "Add cooked rice to the pan, mix gently.\n\n" +
                "5.Add Lemon:\n" +
                "Turn off heat, add fresh lemon juice and mix well.\n\n" +
                "6.Serve:\n" +
                "Serve with papad or pickle for a complete meal.",
        readyInMinutes = 10,
        servings = 2,
        sourceUrl = "https://i.pinimg.com/736x/31/7c/d6/317cd62849557d086aafbdeecc29db70.jpg"

    ),
    RecipeEntity(
        id = 136,
        title = "Chana Masala",
        image = "https://i.pinimg.com/1200x/80/43/df/8043dff1a636e92c7fc7d83857ddb5be.jpg",
        instructions = "1.Soak Chickpeas:\n" +
                "Soak 1 cup chickpeas overnight, then pressure cook until soft.\n\n" +
                "2.Prepare Masala:\n" +
                "Heat oil in a pan, sauté onions until golden.\n\n" +
                "3.Add Tomatoes & Spices:\n" +
                "Add ginger-garlic paste, chopped tomatoes, chili powder, turmeric, and chana masala.\n\n" +
                "4.Cook Base:\n" +
                "Cook until oil separates and masala thickens.\n\n" +
                "5.Add Chickpeas:\n" +
                "Add boiled chickpeas with 1 cup water. Simmer for 10–12 minutes.\n\n" +
                "6.Serve:\n" +
                "Garnish with coriander leaves. Serve with rice or bhature.",
        readyInMinutes = 30,
        servings = 4,
        sourceUrl = "https://i.pinimg.com/1200x/80/43/df/8043dff1a636e92c7fc7d83857ddb5be.jpg"
    ),
    RecipeEntity(
        id = 138,
        title = "Rajma Masala",
        image = "https://i.pinimg.com/1200x/91/c9/bf/91c9bfb5dcccc481b8918eb0d72e4e8a.jpg",
        instructions = "1.Soak Rajma:\n" +
                "Soak 1 cup kidney beans overnight and pressure cook until soft.\n\n" +
                "2.Prepare Gravy:\n" +
                "Heat oil, sauté onions until golden, then add ginger-garlic paste.\n\n" +
                "3.Add Tomatoes & Spices:\n" +
                "Cook chopped tomatoes with turmeric, chili powder, and garam masala.\n\n" +
                "4.Cook Masala:\n" +
                "Cook until oil separates and masala is aromatic.\n\n" +
                "5.Simmer with Beans:\n" +
                "Add cooked rajma with 1 cup water and simmer for 15 minutes.\n\n" +
                "6.Serve:\n" +
                "Garnish with coriander and serve with steamed rice.",
        readyInMinutes = 40,
        servings = 3,
        sourceUrl = "https://example.com/rajma-masala"
    ),
    RecipeEntity(
        id = 145,
        title = "Poha",
        image = "https://i.pinimg.com/736x/6c/f4/67/6cf4674d31ca5efdf262b404d1f5de53.jpg",
        instructions = "1.Rinse Poha:\n" +
                "Rinse flattened rice (poha) in water and drain completely.\n\n" +
                "2.Tempering:\n" +
                "Heat 2 tsp oil, add mustard seeds, curry leaves, green chilies.\n\n" +
                "3.Add Veggies:\n" +
                "Add chopped onions and sauté until soft.\n\n" +
                "4.Spice It:\n" +
                "Add turmeric and salt, mix well.\n\n" +
                "5.Add Poha:\n" +
                "Add rinsed poha, gently mix and cook for 2 mins.\n\n" +
                "6.Serve:\n" +
                "Garnish with coriander and lemon juice.",
        readyInMinutes = 12,
        servings = 2,
        sourceUrl = "https://example.com/poha"
    ),
    RecipeEntity(
        id = 111,
        title = "Upma",
        image = "https://www.indianhealthyrecipes.com/wp-content/uploads/2022/08/upma-recipe.jpg",
        instructions = "1.Roast Rava:\n" +
                "Dry roast semolina until aromatic and set aside.\n\n" +
                "2.Prepare Tempering:\n" +
                "Heat oil, add mustard seeds, curry leaves, green chilies.\n\n" +
                "3.Add Veggies:\n" +
                "Add chopped onions and sauté lightly.\n\n" +
                "4.Add Water:\n" +
                "Add 2 cups water, bring to boil, add salt.\n\n" +
                "5.Add Rava:\n" +
                "Slowly add roasted rava, stirring continuously.\n\n" +
                "6.Serve:\n" +
                "Cook until soft, garnish with coriander.",
        readyInMinutes = 20,
        servings = 2,
        sourceUrl = "https://example.com/upma"
    ),
    RecipeEntity(
        id = 105,
        title = "Masoor Dal",
        image = "https://www.vegrecipesofindia.com/wp-content/uploads/2021/02/masoor-dal-1.jpg",
        instructions = "1.Wash Lentils:\n" +
                "Rinse 1 cup masoor dal thoroughly.\n\n" +
                "2.Cook Dal:\n" +
                "Pressure cook dal with turmeric and salt until soft.\n\n" +
                "3.Prepare Tempering:\n" +
                "Heat ghee, add mustard seeds, cumin, garlic, and red chili.\n\n" +
                "4.Add Spices:\n" +
                "Add hing and sauté for 30 secs.\n\n" +
                "5.Combine:\n" +
                "Pour tempering over cooked dal, mix well.\n\n" +
                "6.Serve:\n" +
                "Garnish with coriander, serve hot with rice.",
        readyInMinutes = 20,
        servings = 3,
        sourceUrl = "https://example.com/masoor-dal"
    ),
    RecipeEntity(
        id = 108,
        title = "Bhindi Masala",
        image = "https://www.cookwithmanali.com/wp-content/uploads/2021/03/Bhindi-Masala-500x500.jpg",
        instructions = "1.Prepare Okra:\n" +
                "Wash and dry okra, cut into pieces.\n\n" +
                "2.Sauté Okra:\n" +
                "Heat oil, shallow fry okra until lightly crisp.\n\n" +
                "3.Make Masala:\n" +
                "In another pan, heat oil, add onions, cook until soft.\n\n" +
                "4.Add Spices:\n" +
                "Add tomatoes, turmeric, chili powder, and garam masala.\n\n" +
                "5.Combine:\n" +
                "Add fried okra, cook for 5 mins.\n\n" +
                "6.Serve:\n" +
                "Serve hot with roti.",
        readyInMinutes = 25,
        servings = 3,
        sourceUrl = "https://example.com/bhindi-masala"
    ),
    RecipeEntity(
        id = 110,
        title = "Kadhi Pakora",
        image = "https://www.vegrecipesofindia.com/wp-content/uploads/2021/06/kadhi-pakora-1.jpg",
        instructions = "1.Make Pakoras:\n" +
                "Mix gram flour, onions, and spices. Fry small balls until golden.\n\n" +
                "2.Prepare Kadhi:\n" +
                "Whisk curd with gram flour and water.\n\n" +
                "3.Cook Kadhi:\n" +
                "Heat oil, add mustard seeds, fenugreek, and curry leaves. Pour kadhi mixture and simmer.\n\n" +
                "4.Add Pakoras:\n" +
                "Drop pakoras into simmering kadhi.\n\n" +
                "5.Cook:\n" +
                "Simmer for 10 mins for flavors to combine.\n\n" +
                "6.Serve:\n" +
                "Serve hot with rice.",
        readyInMinutes = 40,
        servings = 4,
        sourceUrl = "https://example.com/kadhi-pakora"
    ),
    RecipeEntity(
        id = 104,
        title = "Aloo Gobi",
        image = "https://www.cookwithmanali.com/wp-content/uploads/2019/04/Aloo-Gobi-500x500.jpg",
        instructions = "1.Prepare Veggies:\n" +
                "Cut potatoes and cauliflower into bite-sized pieces.\n\n" +
                "2.Tempering:\n" +
                "Heat oil, add cumin seeds, then ginger and garlic.\n\n" +
                "3.Sauté Veggies:\n" +
                "Add potatoes first, cook for 3-4 mins, then add cauliflower.\n\n" +
                "4.Add Spices:\n" +
                "Add turmeric, coriander, chili powder, and salt. Mix well.\n\n" +
                "5.Cook Covered:\n" +
                "Cover and cook for 10-12 mins on low heat until tender.\n\n" +
                "6.Serve:\n" +
                "Garnish with coriander and serve with roti.",
        readyInMinutes = 35,
        servings = 4,
        sourceUrl = "https://example.com/aloo-gobi"
    ),
    RecipeEntity(
        id = 114,
        title = "Egg Curry",
        image = "https://www.indianhealthyrecipes.com/wp-content/uploads/2020/12/egg-curry-recipe.jpg",
        instructions = "1.Boil Eggs:\n" +
                "Hard boil eggs, peel and keep aside.\n\n" +
                "2.Prepare Masala:\n" +
                "Heat oil, sauté onions till golden. Add ginger-garlic paste.\n\n" +
                "3.Add Tomatoes:\n" +
                "Cook chopped tomatoes till soft.\n\n" +
                "4.Add Spices:\n" +
                "Add turmeric, chili powder, coriander, and salt.\n\n" +
                "5.Simmer:\n" +
                "Add water and boil. Add eggs, simmer for 5 mins.\n\n" +
                "6.Serve:\n" +
                "Garnish with coriander and serve with rice or roti.",
        readyInMinutes = 35,
        servings = 3,
        sourceUrl = "https://example.com/egg-curry"
    ),
    RecipeEntity(
        id = 124,
        title = "Pav Bhaji",
        image = "https://www.cookwithmanali.com/wp-content/uploads/2021/06/Pav-Bhaji-500x500.jpg",
        instructions = "1.Boil Veggies:\n" +
                "Boil potatoes, peas, carrots, and mash them.\n\n" +
                "2.Prepare Masala:\n" +
                "Heat butter, sauté onions, ginger-garlic, and capsicum.\n\n" +
                "3.Add Tomatoes:\n" +
                "Cook chopped tomatoes until soft.\n\n" +
                "4.Spice It:\n" +
                "Add pav bhaji masala, chili powder, and salt.\n\n" +
                "5.Combine:\n" +
                "Add mashed veggies and water. Simmer for 10 mins.\n\n" +
                "6.Serve:\n" +
                "Serve with buttered pav and lemon.",
        readyInMinutes = 30,
        servings = 3,
        sourceUrl = "https://example.com/pav-bhaji"
    ),
    RecipeEntity(
        id = 122,
        title = "Moong Dal Khichdi",
        image = "https://www.vegrecipesofindia.com/wp-content/uploads/2021/01/moong-dal-khichdi-recipe.jpg",
        instructions = "1.Wash & Soak:\n" +
                "Wash rice and moong dal, soak for 15 mins.\n\n" +
                "2.Tempering:\n" +
                "Heat ghee, add cumin, garlic, and hing.\n\n" +
                "3.Add Ingredients:\n" +
                "Add rice and dal, sauté for 1-2 mins.\n\n" +
                "4.Cook:\n" +
                "Add turmeric, salt, and water. Pressure cook for 3 whistles.\n\n" +
                "5.Check Consistency:\n" +
                "Add more water if needed for a porridge-like texture.\n\n" +
                "6.Serve:\n" +
                "Serve hot with ghee and papad.",
        readyInMinutes = 25,
        servings = 2,
        sourceUrl = "https://example.com/moong-dal-khichdi"
    ),
    RecipeEntity(
        id = 120,
        title = "Besan Chilla",
        image = "https://www.vegrecipesofindia.com/wp-content/uploads/2021/06/besan-chilla-recipe.jpg",
        instructions = "1.Prepare Batter:\n" +
                "Mix gram flour with water, salt, turmeric, and spices.\n\n" +
                "2.Add Veggies:\n" +
                "Add chopped onions, tomatoes, and coriander.\n\n" +
                "3.Check Consistency:\n" +
                "Ensure the batter is smooth and pourable.\n\n" +
                "4.Heat Pan:\n" +
                "Grease a tawa and heat on medium flame.\n\n" +
                "5.Cook Chilla:\n" +
                "Pour batter, spread gently, cook till golden on both sides.\n\n" +
                "6.Serve:\n" +
                "Serve hot with green chutney.",
        readyInMinutes = 15,
        servings = 2,
        sourceUrl = "https://example.com/besan-chilla"
    ),
    RecipeEntity(
        id = 125,
        title = "Rava Kesari",
        image = "https://www.vegrecipesofindia.com/wp-content/uploads/2022/03/rava-kesari-recipe.jpg",
        instructions = "1.Roast Rava:\n" +
                "Dry roast semolina until aromatic.\n\n" +
                "2.Prepare Syrup:\n" +
                "Boil water with sugar, saffron, and cardamom.\n\n" +
                "3.Add Rava:\n" +
                "Slowly add roasted rava to syrup, stirring continuously.\n\n" +
                "4.Add Ghee:\n" +
                "Add ghee gradually for a soft texture.\n\n" +
                "5.Cook:\n" +
                "Cook until mixture thickens and leaves sides.\n\n" +
                "6.Serve:\n" +
                "Garnish with nuts and serve warm.",
        readyInMinutes = 20,
        servings = 4,
        sourceUrl = "https://example.com/rava-kesari"

    ),
    RecipeEntity(
        id = 117,
        title = "Sambar",
        image = "https://www.vegrecipesofindia.com/wp-content/uploads/2021/06/sambar-recipe-1.jpg",
        instructions = "1.Cook Dal:\n" +
                "Pressure cook toor dal with turmeric.\n\n" +
                "2.Prepare Tamarind Water:\n" +
                "Soak tamarind in warm water, extract pulp.\n\n" +
                "3.Cook Veggies:\n" +
                "Boil drumsticks, carrots, and beans in water.\n\n" +
                "4.Add Spices:\n" +
                "Add sambar powder and tamarind water to vegetables.\n\n" +
                "5.Combine:\n" +
                "Mix cooked dal, simmer for 10 mins.\n\n" +
                "6.Tempering:\n" +
                "Add mustard seeds, curry leaves in oil and pour over sambar.",
        readyInMinutes = 40,
        servings = 4,
        sourceUrl = "https://example.com/sambar"
    ),
    RecipeEntity(
        id = 119,
        title = "Tawa Pulao",
        image = "https://www.vegrecipesofindia.com/wp-content/uploads/2019/07/tawa-pulao-recipe-1.jpg",
        instructions = "1.Cook Rice:\n" +
                "Cook basmati rice and keep aside.\n\n" +
                "2.Sauté Veggies:\n" +
                "On tawa, heat butter, sauté onions, capsicum, peas, and tomatoes.\n\n" +
                "3.Add Spices:\n" +
                "Add pav bhaji masala, turmeric, chili powder, and salt.\n\n" +
                "4.Add Rice:\n" +
                "Mix cooked rice with veggies.\n\n" +
                "5.Fry Well:\n" +
                "Toss on tawa for 3-4 minutes.\n\n" +
                "6.Serve:\n" +
                "Serve hot with raita.",
        readyInMinutes = 20,
        servings = 3,
        sourceUrl = "https://example.com/tawa-pulao"
    )
)




