package com.shashank.recipedia

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.requests.RecipeApi
import com.shashank.recipedia.requests.ServiceGenerator
import com.shashank.recipedia.requests.responses.RecipeResponse
import com.shashank.recipedia.util.Testing
import com.shashank.recipedia.viewmodels.RecipeListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeListActivity : BaseActivity() {

    private val TAG = "RecipeListActivity"

    private lateinit var mRecipeListViewModel: RecipeListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        mRecipeListViewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)

        findViewById<Button>(R.id.test_btn).setOnClickListener {
            testRecipeRequest()
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, Observer { recipes ->

            recipes?.let { recipes ->
                Testing.printRecipes(TAG,recipes)
            }
        })
    }

    private fun searchRecipesApi(query: String, pageNumber: Int) {
        mRecipeListViewModel.searchRecipesApi(query, pageNumber)
    }


    private fun testRecipeRequest() {
        searchRecipesApi("chicken breast",1)
    }
}