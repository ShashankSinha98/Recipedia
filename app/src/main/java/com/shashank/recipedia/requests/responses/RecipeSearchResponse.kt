package com.shashank.recipedia.requests.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.shashank.recipedia.models.Recipe

data class RecipeSearchResponse(

    @SerializedName("count")
    @Expose()
    var count: Int?= null,

    @SerializedName("recipes")
    @Expose()
    var recipes: List<Recipe>?= null
) {

}