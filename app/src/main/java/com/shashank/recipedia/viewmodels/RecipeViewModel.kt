package com.shashank.recipedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.repositories.RecipeRepository

class RecipeViewModel: ViewModel() {

    private val mRecipeRepository: RecipeRepository = RecipeRepository

    fun getRecipe(): LiveData<Recipe> = mRecipeRepository.getRecipe()


    fun searchRecipeById(recipeId: String) {
        mRecipeRepository.searchRecipeById(recipeId)
    }
}