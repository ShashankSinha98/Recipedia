package com.shashank.recipedia.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.shashank.recipedia.AppExecutors
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.persistence.RecipeDao
import com.shashank.recipedia.persistence.RecipeDatabase
import com.shashank.recipedia.requests.ServiceGenerator
import com.shashank.recipedia.requests.responses.ApiResponse
import com.shashank.recipedia.requests.responses.RecipeResponse
import com.shashank.recipedia.requests.responses.RecipeSearchResponse
import com.shashank.recipedia.util.Constants
import com.shashank.recipedia.util.NetworkBoundResource
import com.shashank.recipedia.util.Resource

class RecipeRepository {

    private val TAG = "RecipeRepository"

    private constructor (context: Context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao()
    }

    companion object {
        private var instance: RecipeRepository?= null
        private lateinit var recipeDao: RecipeDao

        fun getInstance(context: Context): RecipeRepository {
            if (instance == null) {
                instance = RecipeRepository(context)
            }
            return instance!!
        }
    }





    // search recipe using query
    fun searchRecipesApi(
        query: String,
        pageNumber: Int): LiveData<Resource<List<Recipe>>> {

        return object: NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors) {

            override fun saveCallResult(item: RecipeSearchResponse) {
                Log.d(TAG,"saveCallResult called")
                Log.d(TAG,"item: $item")

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
                Log.d(TAG,"shouldFetch called")
                Log.d(TAG,"data got from local db: $data")
                return true // get everytime from network for list of recipes
            }


            override fun loadFromDB(): LiveData<List<Recipe>> {
                Log.d(TAG,"loadFromDB called")
                return recipeDao.searchRecipes(query, pageNumber)
            }

            override fun createCall(): LiveData<ApiResponse<RecipeSearchResponse>> {
                Log.d(TAG,"createCall called")
                return ServiceGenerator.recipeApi.searchRecipes(query, pageNumber.toString())
            }

        }.getAsLiveData()
    }


    fun searchRecipeApi(
        recipeId: String
    ): LiveData<Resource<Recipe>> {
        return object : NetworkBoundResource<Recipe, RecipeResponse>(AppExecutors) {

            override fun saveCallResult(item: RecipeResponse) {

                if(item.recipeDetail!=null) {
                    val recipe = Recipe(
                        recipeId= item.recipeDetail.recipeId,
                        title = item.recipeDetail.title,
                        publisher = item.recipeDetail.publisher,
                        ingredients = item.recipeDetail.ingredients,
                        imageUrl =  item.recipeDetail.imageUrl,
                        socialRank = item.recipeDetail.socialRank,
                        timestamp = (System.currentTimeMillis()/1000).toInt()
                    )
                    recipeDao.insertRecipe(recipe)
                }
            }

            override fun shouldFetch(data: Recipe): Boolean {
                Log.d(TAG,"shouldFetch: recipe: $data")
                val currentTime = (System.currentTimeMillis()/1000).toInt()
                Log.d(TAG,"shouldFetch: current time: $currentTime")
                val lastRefresh = data.timestamp?:0
                Log.d(TAG,"shouldFetch: last refresh: $lastRefresh")
                Log.d(TAG,"shouldFetch: it's been ${(currentTime-lastRefresh)/60/60/24}" +
                        " days since this recipe was refreshed. 30 days must elapse before refreshing.")

                if((currentTime-lastRefresh)>=Constants.RECIPE_REFRESH_TIME) {
                    Log.d(TAG,"shouldFetch: SHOULD REFRESH RECIPE: true")
                    return true
                }
                Log.d(TAG,"shouldFetch: SHOULD REFRESH RECIPE: false")
                return false
            }

            override fun loadFromDB(): LiveData<Recipe> = recipeDao.getRecipe(recipeId)

            override fun createCall(): LiveData<ApiResponse<RecipeResponse>> {
                return ServiceGenerator.recipeApi.getRecipe(recipeId)
            }

        }.getAsLiveData()
    }


}