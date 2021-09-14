package com.shashank.recipedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.repositories.RecipeRepository

class RecipeListViewModel: ViewModel() {

    private val mRecipeRepository: RecipeRepository = RecipeRepository

    private var mIsViewingRecipes: Boolean = false


    fun getRecipes(): LiveData<List<Recipe>> = mRecipeRepository.getRecipes()

    fun searchRecipesApi(query: String, pageNumber: Int) {
        mIsViewingRecipes = true
        mRecipeRepository.searchRecipesApi(query, pageNumber)
    }

    fun isViewingRecipes() = mIsViewingRecipes

    fun setIsViewingRecipes(isViewingRecipes: Boolean) {
        mIsViewingRecipes = isViewingRecipes
    }
}