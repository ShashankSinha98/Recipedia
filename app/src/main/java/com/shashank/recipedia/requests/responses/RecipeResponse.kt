package com.shashank.recipedia.requests.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.models.RecipeDetail

data class RecipeResponse(

    @SerializedName("recipe")
    @Expose
    val recipeDetail: RecipeDetail
) {
}