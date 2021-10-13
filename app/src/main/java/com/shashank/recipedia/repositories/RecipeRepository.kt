package com.shashank.recipedia.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.shashank.recipedia.AppExecutors
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.models.RecipeDetail
import com.shashank.recipedia.persistence.RecipeDao
import com.shashank.recipedia.persistence.RecipeDatabase
import com.shashank.recipedia.requests.RecipeApiClient
import com.shashank.recipedia.requests.ServiceGenerator
import com.shashank.recipedia.requests.responses.ApiResponse
import com.shashank.recipedia.requests.responses.RecipeSearchResponse
import com.shashank.recipedia.util.NetworkBoundResource
import com.shashank.recipedia.util.Resource

class RecipeRepository {

    private val TAG = "RecipeRepository"

    private constructor (context: Context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao()
    }

    companion object {
        private lateinit var instance: RecipeRepository
        private lateinit var recipeDao: RecipeDao

        fun getInstance(context: Context): RecipeRepository {
            if (instance == null) {
                instance = RecipeRepository(context)
            }
            return instance
        }
    }

    fun searchRecipesApi(
        query: String,
        pageNumber: Int): LiveData<Resource<List<Recipe>>> {

        return object: NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors) {

            override fun saveCallResult(item: RecipeSearchResponse) {
                item.recipes?.let { recipes ->

                    for((index, rowId) in recipeDao.insertRecipes(*recipes.toTypedArray()).withIndex()) {
                        if(rowId==-1L) {
                            Log.d(TAG,"saveCallResult: CONFLICT.. This recipe is already in cache")
                            // if the recipe already exists... I don't want to set the ingredients or timestamp b/c
                            // they will be erased
                            recipeDao.updateRecipe(
                                recipes[index].recipeId,
                                recipes[index].title,
                                recipes[index].publisher,
                                recipes[index].imageUrl,
                                recipes[index].socialRank,
                            )
                        }
                    }
                }
            }

            override fun shouldFetch(data: List<Recipe>): Boolean {
                return true // get everytime from network for list of recipes
            }

            override fun loadFromDB(): LiveData<List<Recipe>> {
                return recipeDao.searchRecipes(query, pageNumber)
            }

            override fun createCall(): LiveData<ApiResponse<RecipeSearchResponse>> {
                return ServiceGenerator.recipeApi.searchRecipes(query, pageNumber.toString())
            }

        }.getAsLiveData()
    }
}