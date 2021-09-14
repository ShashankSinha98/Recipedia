package com.shashank.recipedia.util

class Constants {

    companion object {
        val BASE_URL = "https://recipesapi.herokuapp.com"
        val NETWORK_TIMEOUT = 3000L

        val IMAGE_NOT_FOUND_URL = "https://bitsofco.de/content/images/2018/12/broken-1.png"
        val TEXT_NOT_FOUND_MSG = "NA"
        val SCORE_NOT_FOUND_MSG = 0F

        val DEFAULT_SEARCH_CATEGORIES = listOf<String>("Barbeque", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian")

        val DEFAULT_SEARCH_CATEGORY_IMAGES = listOf<String>(
            "barbeque",
            "breakfast",
            "chicken",
            "beef",
            "brunch",
            "dinner",
            "wine",
            "italian"
        )
    }
}