package com.shashank.recipedia

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashank.recipedia.adapters.OnRecipeListener
import com.shashank.recipedia.adapters.RecipeRecyclerAdapter
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.requests.RecipeApi
import com.shashank.recipedia.requests.ServiceGenerator
import com.shashank.recipedia.requests.responses.RecipeResponse
import com.shashank.recipedia.util.Testing
import com.shashank.recipedia.viewmodels.RecipeListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeListActivity : BaseActivity(), OnRecipeListener {

    private val TAG = "RecipeListActivity"

    private lateinit var mRecipeListViewModel: RecipeListViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecipeRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        mRecyclerView = findViewById(R.id.recipe_list)
        mRecipeListViewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)

        initRecyclerView()
        subscribeObservers()
        testRecipeRequest()
    }

    private fun subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, Observer { recipes ->

            recipes?.let { recipes ->
                Testing.printRecipes(TAG,recipes)
                mAdapter.setRecipes(recipes)
            }
        })
    }

    private fun searchRecipesApi(query: String, pageNumber: Int) {
        mRecipeListViewModel.searchRecipesApi(query, pageNumber)
    }


    private fun initRecyclerView() {
        mAdapter = RecipeRecyclerAdapter(this)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun testRecipeRequest() {
        searchRecipesApi("chicken breast",1)
    }

    override fun onRecipeClick(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCategoryClick(category: String) {
        TODO("Not yet implemented")
    }
}