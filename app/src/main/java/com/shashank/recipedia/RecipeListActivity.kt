package com.shashank.recipedia

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
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
    private lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        mRecyclerView = findViewById(R.id.recipe_list)
        mRecipeListViewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)
        mSearchView = findViewById(R.id.search_view)

        initRecyclerView()
        initSearchView()
        subscribeObservers()

        // To Do - Move this logic inside view model
        if(!mRecipeListViewModel.isViewingRecipes()) {
            // display search categories
            displaySearchCategories()
        }

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

    }

    private fun subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, Observer { recipes ->

            recipes?.let { recipes ->
                if(mRecipeListViewModel.isViewingRecipes()) {
                    Testing.printRecipes(TAG, recipes)
                    mRecipeListViewModel.setIsPerformingQuery(false)
                    mAdapter.setRecipes(recipes)
                }
            }
        })
    }



    private fun initSearchView() {

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mAdapter.displayLoading()
                    mRecipeListViewModel.searchRecipesApi(it, 1)
                    mSearchView.clearFocus()
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
        mSearchView.clearFocus()
    }


    private fun displaySearchCategories() {
        mRecipeListViewModel.setIsViewingRecipes(false)
        mAdapter.displaySearchCategories()
    }

    override fun onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()) {
            super.onBackPressed()
        } else {
            displaySearchCategories()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.action_categories) {
            displaySearchCategories()
        }

        return super.onOptionsItemSelected(item)
    }
}