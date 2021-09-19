package com.shashank.recipedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.RecipeDetail
import com.shashank.recipedia.repositories.RecipeRepository

class RecipeViewModel: ViewModel() {

    private val mRecipeRepository: RecipeRepository = RecipeRepository
    private var mRecipeId: String?= null

    fun getRecipeDetail(): LiveData<RecipeDetail> = mRecipeRepository.getRecipeDetail()


    fun searchRecipeById(recipeId: String) {
        mRecipeId = recipeId
        mRecipeRepository.searchRecipeById(recipeId)
    }

    fun getRecipeId(): String?= mRecipeId
}