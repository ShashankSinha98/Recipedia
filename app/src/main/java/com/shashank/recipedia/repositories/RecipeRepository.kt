package com.shashank.recipedia.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.models.RecipeDetail
import com.shashank.recipedia.requests.RecipeApiClient

object RecipeRepository {

    private val TAG = "RecipeRepository"

    private val mRecipeApiClient: RecipeApiClient = RecipeApiClient
    private var mQuery: String?= null
    private var mPageNumber: Int?= null

    private val mIsQueryExhausted: MutableLiveData<Boolean> = MutableLiveData()
    private val mRecipes: MediatorLiveData<MutableList<Recipe>> = MediatorLiveData()



    init {
        initMediators()
    }



    fun getRecipes(): LiveData<MutableList<Recipe>> = mRecipes

    fun getRecipeDetail(): LiveData<RecipeDetail> = mRecipeApiClient.getRecipeDetail()

    fun isQueryExhausted(): LiveData<Boolean> = mIsQueryExhausted


    private fun initMediators() {
        val recipeListApiSource = mRecipeApiClient.getRecipes()
        mRecipes.addSource(recipeListApiSource, Observer { recipes ->
            if(recipes!=null) {
                mRecipes.value = recipes
                doneQuery(recipes)
            } else {
                // search database cache
                doneQuery(null)
            }
        })
    }

    private fun doneQuery(recipes: List<Recipe>?) {
        if(recipes!=null) {
            Log.d(TAG,"doneQuery: recipe list size: ${recipes.size}")
            if(recipes.size%30!=0) {
                mIsQueryExhausted.value = true
            }
        } else {
            mIsQueryExhausted.value = true
        }
    }


    fun isRecipeDetailRequestTimedOut(): LiveData<Boolean> =
        mRecipeApiClient.isRecipeDetailRequestTimedOut()


    fun searchRecipesApi(query: String, pageNumber: Int) {
        val searchPageNumber = if(pageNumber==0) 1 else pageNumber
        mQuery = query
        mPageNumber = pageNumber
        mIsQueryExhausted.value = false
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