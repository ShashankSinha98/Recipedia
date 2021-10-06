package com.shashank.recipedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        mRecipeListViewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)


        mRecyclerView = findViewById(R.id.recipe_list)
        mSearchView = findViewById(R.id.search_view)

        initRecyclerView()
        initSearchView()
        subscribeObservers()

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

    }


    private fun subscribeObservers() {
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
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position))
        startActivity(intent)
    }

    override fun onCategoryClick(category: String) {

    }

}