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

    // query extras
    private var isQueryExhausted: Boolean= false
    private var isPerformingQuery: Boolean= false
    private var pageNumber: Int?= null
    private var query: String?= null

    companion object {
        val QUERY_EXHAUSTED = "No more results..."
    }


    init {
        if(viewState==null) {
            viewState = MutableLiveData()
            viewState?.value = ViewState.CATEGORIES
        }
    }

    fun getViewState(): LiveData<ViewState>? = viewState

    fun getRecipes(): LiveData<Resource<List<Recipe>>> = recipes

    fun getPageNumber(): Int = pageNumber?:1

    fun searchRecipesApi(query: String, pageNumber: Int) {
        Log.d(TAG,"searchRecipesApi: query: $query, page no: $pageNumber")
        if(!isPerformingQuery) {
            this.pageNumber = pageNumber
            this.query = query
            if(pageNumber==0) {
                this.pageNumber = 1
            }
            isQueryExhausted = false
            executeRecipesSearch(this.query!!, this.pageNumber!!)
        }
    }

    fun searchNextPage() {
        if(!isQueryExhausted && !isPerformingQuery) {
            pageNumber = pageNumber!! + 1
            executeRecipesSearch(query!!, pageNumber!!)
        }
    }

    fun setViewCategories() {
        viewState?.value = ViewState.CATEGORIES
    }

    private fun executeRecipesSearch(query: String, pageNumber: Int) {
        isPerformingQuery = true
        viewState?.value = ViewState.RECIPES

        val repositorySource: LiveData<Resource<List<Recipe>>> = recipeRepository.searchRecipesApi(query, pageNumber)

        recipes.addSource(repositorySource) { listResource ->

            if(listResource!=null) {
                recipes.value = listResource

                if(listResource.status==Resource.Status.SUCCESS) {
                    isPerformingQuery = false

                    if(listResource.data!=null) {
                        if(listResource.data.isEmpty()) {

                            Log.d(TAG,"onChanged: Query is exhausted")
                            recipes.value = Resource<List<Recipe>>(
                                status = Resource.Status.ERROR,
                                data = listResource.data,
                                message = QUERY_EXHAUSTED
                            )
                        }
                    }
                    recipes.removeSource(repositorySource)

                } else if(listResource.status==Resource.Status.ERROR) {

                    isPerformingQuery=false
                    recipes.removeSource(repositorySource)
                }
            } else {
                recipes.removeSource(repositorySource)
            }
        }
    }
}