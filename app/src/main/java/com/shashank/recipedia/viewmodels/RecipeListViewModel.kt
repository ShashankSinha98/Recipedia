package com.shashank.recipedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.repositories.RecipeRepository

class RecipeListViewModel: ViewModel() {

    private val mRecipeRepository: RecipeRepository = RecipeRepository



    fun getRecipes(): LiveData<List<Recipe>> = mRecipeRepository.getRecipes()
}