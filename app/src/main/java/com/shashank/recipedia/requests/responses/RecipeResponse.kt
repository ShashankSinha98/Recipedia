package com.shashank.recipedia.requests.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.shashank.recipedia.models.Recipe

data class RecipeResponse(

    @SerializedName("recipe")
    @Expose
    val recipe: Recipe
) {
}