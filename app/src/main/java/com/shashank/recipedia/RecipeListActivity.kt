package com.shashank.recipedia

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashank.recipedia.adapters.OnRecipeListener
import com.shashank.recipedia.adapters.RecipeRecyclerAdapter
import com.shashank.recipedia.util.Testing
import com.shashank.recipedia.util.VerticalSpacingItemDecorator
import com.shashank.recipedia.viewmodels.RecipeListViewModel

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
        initSearchView()
        subscribeObservers()

        // To Do - Move this logic inside view model
        if(!mRecipeListViewModel.isViewingRecipes()) {
            // display search categories
            displaySearchCategories()
        }

    }

    private fun subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, Observer { recipes ->

            recipes?.let { recipes ->
                Testing.printRecipes(TAG,recipes)
                mAdapter.setRecipes(recipes)
            }
        })
    }



    private fun initSearchView() {
        val searchView: SearchView = findViewById(R.id.search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mAdapter.displayLoading()
                    mRecipeListViewModel.searchRecipesApi(it, 1)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean = false

        })
    }


    private fun initRecyclerView() {
        mAdapter = RecipeRecyclerAdapter(this)
        mRecyclerView.addItemDecoration(VerticalSpacingItemDecorator(30))
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)

    }


    override fun onRecipeClick(position: Int) {

    }

    override fun onCategoryClick(category: String) {
        mAdapter.displayLoading()
        mRecipeListViewModel.searchRecipesApi(category,1)
    }


    private fun displaySearchCategories() {
        mRecipeListViewModel.setIsViewingRecipes(false)
        mAdapter.displaySearchCategories()
    }
}