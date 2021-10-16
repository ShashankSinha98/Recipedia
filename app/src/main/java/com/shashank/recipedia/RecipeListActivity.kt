package com.shashank.recipedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.shashank.recipedia.adapters.OnRecipeListener
import com.shashank.recipedia.adapters.RecipeRecyclerAdapter
import com.shashank.recipedia.util.Resource
import com.shashank.recipedia.util.Testing
import com.shashank.recipedia.util.VerticalSpacingItemDecorator
import com.shashank.recipedia.viewmodels.RecipeListViewModel

class RecipeListActivity : BaseActivity(), OnRecipeListener {

    private val TAG = "RecipeListActivity"

    private lateinit var mRecipeListViewModel: RecipeListViewModel


    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecipeRecyclerAdapter
    private lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        mRecipeListViewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)


        mRecyclerView = findViewById(R.id.recipe_list)
        mSearchView = findViewById(R.id.search_view)

        initRecyclerView()
        initSearchView()
        subscribeObservers()

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

    }


    private fun subscribeObservers() {

        mRecipeListViewModel.getRecipes().observe(this, { listResource ->
            listResource?.let {
                Log.d(TAG,"mRecipeListViewModel.getRecipes() onChanged: ${listResource.status}")
                //  assumed it to be success
                if(listResource.data!=null) {

                    when(listResource.status) {
                        Resource.Status.LOADING -> {
                            if(mRecipeListViewModel.getPageNumber()>1) {
                                mAdapter.displayLoading()
                            } else {
                                mAdapter.displayOnlyLoading()
                            }
                        }

                        Resource.Status.ERROR -> {
                            Log.d(TAG,"onChanged: Cannot refresh the cache")
                            Log.d(TAG,"onChanged: Error message: ${listResource.message}")
                            Log.d(TAG,"onChanged: Status: ERROR, #recipes: ${listResource.data.size}")
                            mAdapter.hideLoading()
                            mAdapter.setRecipes(listResource.data.toMutableList())
                            Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show()

                            if(listResource.message.equals(RecipeListViewModel.QUERY_EXHAUSTED)) {
                                mAdapter.setQueryExhausted()
                            }
                        }

                        Resource.Status.SUCCESS -> {
                            Log.d(TAG, "onChanged: cache has been refreshed.")
                            Log.d(TAG, "onChanged: status: SUCCESS, #Recipes: ${listResource.data.size}")
                            mAdapter.hideLoading()
                            mAdapter.setRecipes(listResource.data.toMutableList())

                        }
                    }
                }
            }
        })

        mRecipeListViewModel.getViewState()?.observe(this, Observer { viewState ->

            viewState?.let {
                when(viewState) {

                    RecipeListViewModel.ViewState.RECIPES -> {
                        // recipes will show automatically from another observer
                    }

                    RecipeListViewModel.ViewState.CATEGORIES -> {
                        displaySearchCategories()
                    }
                }
            }
        })
    }

    private fun displaySearchCategories() {
        mAdapter.displaySearchCategories()
    }


    private fun initSearchView() {

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchRecipesApi(query)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean = false

        })
    }


    private fun initRecyclerView() {
        mAdapter = RecipeRecyclerAdapter(this, initGlide())
        mRecyclerView.addItemDecoration(VerticalSpacingItemDecorator(30))
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!mRecyclerView.canScrollVertically(1)
                    && mRecipeListViewModel.getViewState()?.value==RecipeListViewModel.ViewState.RECIPES) {
                    mRecipeListViewModel.searchNextPage()
                }
            }
        })

        mRecyclerView.adapter = mAdapter
    }


    override fun onRecipeClick(position: Int) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position))
        startActivity(intent)
    }

    override fun onCategoryClick(category: String) {
        searchRecipesApi(category)
    }

    private fun searchRecipesApi(query: String?) {
        var q = query ?: ""
        mRecyclerView.smoothScrollToPosition(0) // top of list
        mRecipeListViewModel.searchRecipesApi(q, 1)
        mSearchView.clearFocus()
    }

    private fun initGlide(): RequestManager {
        val options = RequestOptions()
            .placeholder(R.drawable.loading_img)
            .error(R.drawable.error_img)

        return Glide.with(this).setDefaultRequestOptions(options)
    }

    override fun onBackPressed() {
        if(mRecipeListViewModel.getViewState()?.value==RecipeListViewModel.ViewState.CATEGORIES) {
            super.onBackPressed()
        } else {
            mRecipeListViewModel.setViewCategories()
        }
    }

}