package com.shashank.recipedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.RecipeDetail
import com.shashank.recipedia.repositories.RecipeRepository
import com.shashank.recipedia.requests.RecipeApiClient

class RecipeViewModel: ViewModel() {

    private val mRecipeRepository: RecipeRepository = RecipeRepository
    private var mRecipeId: String?= null
    private var mDidRetrieveRecipe: Boolean = false

    fun getRecipeDetail(): LiveData<RecipeDetail> = mRecipeRepository.getRecipeDetail()

    fun isRecipeDetailRequestTimedOut(): LiveData<Boolean> =
        mRecipeRepository.isRecipeDetailRequestTimedOut()

    fun searchRecipeById(recipeId: String) {
        mRecipeId = recipeId
        mRecipeRepository.searchRecipeById(recipeId)
    }

    fun getRecipeId(): String?= mRecipeId

    fun setRetrievedRecipe(retrievedRecipe: Boolean) {
        mDidRetrieveRecipe = retrievedRecipe
    }

    fun didRetrievedRecipe(): Boolean = mDidRetrieveRecipe
}