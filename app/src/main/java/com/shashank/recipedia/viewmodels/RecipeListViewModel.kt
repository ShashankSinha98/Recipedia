package com.shashank.recipedia.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.repositories.RecipeRepository

class RecipeListViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "RecipeListViewModel"

    enum class ViewState {CATEGORIES, RECIPES}

    private var viewState: MutableLiveData<ViewState>?= null

    init {
        if(viewState==null) {
            viewState = MutableLiveData()
            viewState?.value = ViewState.CATEGORIES
        }
    }

    fun getViewState(): LiveData<ViewState>? = viewState
}