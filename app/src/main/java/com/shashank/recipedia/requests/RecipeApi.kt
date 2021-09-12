package com.shashank.recipedia.requests

import com.shashank.recipedia.requests.responses.RecipeResponse
import com.shashank.recipedia.requests.responses.RecipeSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

    // Search
    @GET("api/v2/recipes")
    fun searchRecipes(
        @Query("q") query: String,
        @Query("page") page: String
    ): Call<RecipeSearchResponse>


    // Get recipe request
    @GET("api/get")
    fun getRecipe(
        @Query("rId") recipeId: String
    ): Call<RecipeResponse>
}