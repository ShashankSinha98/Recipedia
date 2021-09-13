package com.shashank.recipedia.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashank.recipedia.models.Recipe

object RecipeRepository {

    private val mRecipes: MutableLiveData<List<Recipe>> = MutableLiveData()

    fun getRecipes(): LiveData<List<Recipe>> = mRecipes

}