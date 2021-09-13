package com.shashank.recipedia.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.requests.RecipeApiClient

object RecipeRepository {

    private val mRecipeApiClient: RecipeApiClient = RecipeApiClient

    fun getRecipes(): LiveData<List<Recipe>> = mRecipeApiClient.getRecipes()

    fun searchRecipesApi(query: String, pageNumber: Int) {
        val searchPageNumber = if(pageNumber==0) 1 else pageNumber

        mRecipeApiClient.searchRecipesApi(query, searchPageNumber)
    }
}