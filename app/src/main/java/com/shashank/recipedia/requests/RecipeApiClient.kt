package com.shashank.recipedia.requests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashank.recipedia.AppExecutors
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.requests.responses.RecipeSearchResponse
import com.shashank.recipedia.util.Constants
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit


object RecipeApiClient {

    private val TAG = "RecipeApiClient"
    private val mRecipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    private var mRetrieveRecipesRunnable: RetrieveRecipesRunnable?= null

    fun getRecipes(): LiveData<List<Recipe>> = mRecipes

    fun searchRecipesApi(query: String, pageNumber: Int) {

        if(mRetrieveRecipesRunnable!=null) mRetrieveRecipesRunnable = null

        mRetrieveRecipesRunnable = RetrieveRecipesRunnable(query, pageNumber)

        val handler: Future<*> = AppExecutors.networkIO().submit(mRetrieveRecipesRunnable)

        AppExecutors.networkIO().schedule(Runnable {

            // stop request - timeout occurred
            handler.cancel(true)

        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
    }


    private class RetrieveRecipesRunnable(
        private var query: String,
        private var pageNumber: Int,
        var cancelRequest: Boolean= false
    ): Runnable {


        /**
         * call.execute() runs the request on the current thread.
         * call.enqueue(callback) runs the request on a background thread, and runs the callback on the current thread.
         * */
        override fun run() {

            try {


                val response: Response<*> = getRecipes(query, pageNumber).execute()
                if(cancelRequest) return

                if(response.code()==200) {
                    Log.d(TAG,"Response code 200 for search API, response: ${response.body()}")

                    val list: List<Recipe>? = (response.body() as RecipeSearchResponse).recipes
                    list?.let {
                        if(pageNumber==1) {
                            mRecipes.postValue(list) // postValue- background thread, setValue- not on background Thread

                        } else {

                            var currentRecipes: List<Recipe>? = mRecipes.value
                            currentRecipes?.let {
                                val completeList: MutableList<Recipe> = mutableListOf<Recipe>()
                                completeList.addAll(currentRecipes)
                                completeList.addAll(list)
                                mRecipes.postValue(completeList)
                            }
                        }

                    }
                } else {
                    val error: String = response.errorBody().toString()
                    Log.d(TAG,"run: $error")
                    mRecipes.postValue(null)
                }

            } catch (e: IOException) {
                Log.d(TAG,"Exception: ${e.message}")
                e.printStackTrace()
                mRecipes.postValue(null)
            }
        }

        private fun getRecipes(query: String, pageNumber: Int): Call<RecipeSearchResponse> =
            ServiceGenerator.recipeApi.searchRecipes(
                query= query,
                page = pageNumber.toString()
            )

        fun cancelRequest() {
            Log.d(TAG,"Cancelling the Search Request")
            cancelRequest = true
        }
    }


    fun cancelRequest() {
        mRetrieveRecipesRunnable?.let {
            it.cancelRequest()
        }
    }
}