package com.shashank.recipedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.repositories.RecipeRepository

class RecipeListViewModel: ViewModel() {

    private val mRecipeRepository: RecipeRepository = RecipeRepository
    private var mIsViewingRecipes: Boolean = false
    private var mIsPerformingQuery: Boolean = false


    fun getRecipes(): LiveData<MutableList<Recipe>> = mRecipeRepository.getRecipes()

    fun isQueryExhausted(): LiveData<Boolean> = mRecipeRepository.isQueryExhausted()


    fun searchRecipesApi(query: String, pageNumber: Int) {
        mIsViewingRecipes = true
        mIsPerformingQuery = true
        mRecipeRepository.searchRecipesApi(query, pageNumber)
    }

    fun isViewingRecipes() = mIsViewingRecipes


    fun searchNextPage() {
        if(!mIsPerformingQuery && mIsViewingRecipes
            && isQueryExhausted().value==false) {
            mRecipeRepository.searchNextPage()
        }
    }

    fun setIsViewingRecipes(isViewingRecipes: Boolean) {
        mIsViewingRecipes = isViewingRecipes
    }

    fun setIsPerformingQuery(isPerformingQuery: Boolean) {
        mIsPerformingQuery = isPerformingQuery
    }

    fun isPerformingQuery(): Boolean = mIsPerformingQuery

    fun onBackPressed(): Boolean {
        if(mIsPerformingQuery) {
            // Cancel the API request
            mRecipeRepository.cancelRequest()
            mIsPerformingQuery = false
        }


        if(mIsViewingRecipes) {
            mIsViewingRecipes = false
            return false
        }
        return true
    }
}