package com.shashank.recipedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.requests.RecipeApi
import com.shashank.recipedia.requests.ServiceGenerator
import com.shashank.recipedia.requests.responses.RecipeResponse
import com.shashank.recipedia.requests.responses.RecipeSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeListActivity : BaseActivity() {

    private val TAG = "RecipeListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        findViewById<Button>(R.id.test_btn).setOnClickListener {
            testRecipeRequest()
        }
    }


    private fun testRecipeRequest() {
        val recipeApi: RecipeApi = ServiceGenerator.recipeAPi

        /*val responseCall: Call<RecipeSearchResponse> = recipeApi.searchRecipes(
            query = "rice",
            page = "1"
        )

        responseCall.enqueue(object : Callback<RecipeSearchResponse> {

            override fun onResponse(
                call: Call<RecipeSearchResponse>,
                response: Response<RecipeSearchResponse>
            ) {
               Log.d(TAG,"onResponse: Server response: $response")

                if(response.code()==200) {
                    Log.d(TAG,"onResponse: ${response.body()}")

                    val recipes: List<Recipe>? = response.body()?.recipes

                    if (recipes != null) {
                        for(recipe in recipes) {
                            Log.d(TAG,"onResponse: ${recipe.title}")
                        }
                    }
                } else {

                    try {
                        Log.d(TAG,"onResponse: ${response.errorBody()}")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<RecipeSearchResponse>, t: Throwable) {
                Log.d(TAG,"onResponse[Failure]: ${t.message}")
            }

        })*/

        val responseCall: Call<RecipeResponse> = recipeApi.getRecipe(
            recipeId = "41470"
        )


        responseCall.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                Log.d(TAG,"onResponse: Server response: $response")

                if(response.code()==200) {
                    Log.d(TAG,"onResponse: ${response.body()}")

                    val recipe: Recipe? = response.body()?.recipe

                    Log.d(TAG,"onResponse: $recipe")

                } else {

                    try {
                        Log.d(TAG,"onResponse: ${response.errorBody()}")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.d(TAG,"onResponse[Failure]: ${t.message}")
            }

        })
    }
}