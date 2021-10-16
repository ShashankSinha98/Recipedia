package com.shashank.recipedia.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.models.RecipeDetail
import com.shashank.recipedia.repositories.RecipeRepository
import com.shashank.recipedia.util.Resource

class RecipeViewModel(application: Application): AndroidViewModel(application) {

    private val recipeRepositoru = RecipeRepository.getInstance(application)

    fun searchRecipeApi(recipeId: String): LiveData<Resource<Recipe>> {
        return recipeRepositoru.searchRecipeApi(recipeId)
    }
}