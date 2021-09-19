package com.shashank.recipedia.repositories

import androidx.lifecycle.LiveData
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.models.RecipeDetail
import com.shashank.recipedia.requests.RecipeApiClient

object RecipeRepository {

    private val mRecipeApiClient: RecipeApiClient = RecipeApiClient
    private var mQuery: String?= null
    private var mPageNumber: Int?= null


    fun getRecipes(): LiveData<List<Recipe>> = mRecipeApiClient.getRecipes()

    fun getRecipeDetail(): LiveData<RecipeDetail> = mRecipeApiClient.getRecipeDetail()

    fun isRecipeDetailRequestTimedOut(): LiveData<Boolean> =
        mRecipeApiClient.isRecipeDetailRequestTimedOut()


    fun searchRecipesApi(query: String, pageNumber: Int) {
        val searchPageNumber = if(pageNumber==0) 1 else pageNumber
        mQuery = query
        mPageNumber = pageNumber
        mRecipeApiClient.searchRecipesApi(query, searchPageNumber)
    }

    fun searchRecipeById(recipeId: String) { mRecipeApiClient.searchRecipeById(recipeId) }



    fun searchNextPage() {
        if(mQuery!=null && mPageNumber!=null) {
            searchRecipesApi(mQuery!!, mPageNumber!! + 1)
        }
    }

    fun cancelRequest() {
        mRecipeApiClient.cancelRequest()
    }
}