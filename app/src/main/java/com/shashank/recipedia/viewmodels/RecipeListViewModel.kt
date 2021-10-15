package com.shashank.recipedia.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.repositories.RecipeRepository
import com.shashank.recipedia.util.Resource

class RecipeListViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "RecipeListViewModel"

    enum class ViewState {CATEGORIES, RECIPES}

    private var viewState: MutableLiveData<ViewState>?= null
    private val recipes: MediatorLiveData<Resource<List<Recipe>>> = MediatorLiveData()
    private val recipeRepository = RecipeRepository.getInstance(application)



    init {
        if(viewState==null) {
            viewState = MutableLiveData()
            viewState?.value = ViewState.CATEGORIES
        }
    }

    fun getViewState(): LiveData<ViewState>? = viewState

    fun getRecipes(): LiveData<Resource<List<Recipe>>> = recipes

    fun searchRecipesApi(query: String, pageNumber: Int) {
        Log.d(TAG,"searchRecipesApi: query: $query, page no: $pageNumber")
        val repositorySource: LiveData<Resource<List<Recipe>>> = recipeRepository.searchRecipesApi(query, pageNumber)

        recipes.addSource(repositorySource) { listResource ->
            // react to data
            recipes.value = listResource
        }
    }
}