package com.shashank.recipedia.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.shashank.recipedia.R
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.util.Constants
import kotlin.math.roundToInt

class RecipeRecyclerAdapter(
    private val mOnRecipeListener: OnRecipeListener,
    private val mRequestManager: RequestManager
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val RECIPE_TYPE = 1
        private val LOADING_TYPE = 2
        private val CATEGORY_TYPE = 3
        private val EXHAUSTED_TYPE = 4
    }


    private var mRecipes: MutableList<Recipe> = mutableListOf<Recipe>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View?= null

        when(viewType) {

            RECIPE_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item,
                    parent, false)

                return RecipeViewHolder(view, mOnRecipeListener, mRequestManager)
            }

            LOADING_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_loading_list_item,
                    parent, false)

                return LoadingViewHolder(view)
            }


            EXHAUSTED_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_search_exhausted,
                    parent, false)

                return SearchExhaustedViewHolder(view)
            }

            CATEGORY_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_category_list_item,
                    parent, false)

                return CategoryViewHolder(view, mOnRecipeListener, mRequestManager)
            }

            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item,
                    parent, false)

                return RecipeViewHolder(view, mOnRecipeListener, mRequestManager)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewType: Int = getItemViewType(position)

        val requestOptions: RequestOptions = RequestOptions()
            .placeholder(R.drawable.loading_img)



        if(itemViewType == RECIPE_TYPE) {

            val recipeViewHolder = holder as RecipeViewHolder
            recipeViewHolder.onBind(mRecipes[position])
        }

        else if(itemViewType == CATEGORY_TYPE) {

            val categoryViewHolder = holder as CategoryViewHolder
            categoryViewHolder.onBind(mRecipes[position])

        }
    }

    override fun getItemCount(): Int = mRecipes.size

    override fun getItemViewType(position: Int): Int {
        if(mRecipes.isNotEmpty() && mRecipes[position].socialRank==-1f) {
            return CATEGORY_TYPE
        } else if(mRecipes.isNotEmpty() && mRecipes[position].title.equals("LOADING")) {
            return LOADING_TYPE
        } else if(mRecipes.isNotEmpty() && mRecipes[position].title.equals("EXHAUSTED")) {
            return EXHAUSTED_TYPE
        } else {
            return RECIPE_TYPE
        }
    }

    // display loading during search request
    fun displayOnlyLoading() {
        clearRecipesList()
        val recipe = Recipe(recipeId = "-1")
        recipe.title = "LOADING"
        mRecipes.add(recipe)
        notifyDataSetChanged()

    }

    private fun clearRecipesList() {
        mRecipes.clear()
        notifyDataSetChanged()
    }

    fun setQueryExhausted() {
        hideLoading()
        val exhaustedRecipe = Recipe(recipeId = "-1")
        exhaustedRecipe.title = "EXHAUSTED"
        mRecipes.add(exhaustedRecipe)
        notifyDataSetChanged()
    }


    fun hideLoading() {
        if(isLoading()) {
            if(mRecipes[0].title=="LOADING") {
                mRecipes.removeAt(0)
            }

            else if(mRecipes[mRecipes.size-1].title=="LOADING") {
                mRecipes.removeAt(mRecipes.size-1)
            }
            notifyDataSetChanged()
        }
    }

    private fun isLoading(): Boolean {
        if(mRecipes.isNotEmpty() && mRecipes[mRecipes.size-1].title.equals("LOADING")) {
            return true
        }
        return false
    }


    // display loading in pagination
    fun displayLoading() {
        if(!isLoading()) {
            val recipe = Recipe(recipeId = "-1")
            recipe.title = "LOADING"
            mRecipes.add(recipe)
            notifyDataSetChanged()
            //val loadingList = mutableListOf<Recipe>(recipe)
            // mRecipes = loadingList
        }
    }

    fun displaySearchCategories() {
        val categories = mutableListOf<Recipe>()

        for(i in Constants.DEFAULT_SEARCH_CATEGORIES.indices) {
            val recipe = Recipe(recipeId = "-1")
            recipe.title = Constants.DEFAULT_SEARCH_CATEGORIES[i]
            recipe.imageUrl = Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]
            recipe.socialRank = -1f

            categories.add(recipe)
        }

        mRecipes = categories
        notifyDataSetChanged()
    }

    fun setRecipes(recipes: MutableList<Recipe>) {
        mRecipes = recipes
        notifyDataSetChanged()
    }

    fun getSelectedRecipe(position: Int): Recipe? {
        mRecipes?.let {
            if(mRecipes.isNotEmpty()) {
                return mRecipes[position]
            }
        }
        return null
    }
}