package com.shashank.recipedia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shashank.recipedia.R
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.util.Constants
import kotlin.math.roundToInt

class RecipeRecyclerAdapter(
    private val mOnRecipeListener: OnRecipeListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val RECIPE_TYPE = 1
        private val LOADING_TYPE = 2
    }


    private var mRecipes: List<Recipe> = listOf<Recipe>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View?= null

        when(viewType) {

            RECIPE_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item,
                    parent, false)

                return RecipeViewHolder(view, mOnRecipeListener)
            }

            LOADING_TYPE -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_loading_list_item,
                    parent, false)

                return LoadingViewHolder(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item,
                    parent, false)

                return RecipeViewHolder(view, mOnRecipeListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemViewType: Int = getItemViewType(position)

        if(itemViewType == RECIPE_TYPE) {
            val recipeViewHolder = holder as RecipeViewHolder

            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(R.drawable.loading_img)

            recipeViewHolder.apply {
                title.text = mRecipes[position].title?: Constants.TEXT_NOT_FOUND_MSG
                publisher.text = mRecipes[position].publisher?: Constants.TEXT_NOT_FOUND_MSG
                socialScore.text = (mRecipes[position].socialRank ?: Constants.SCORE_NOT_FOUND_MSG).
                roundToInt().toString()

                Glide.with(holder.itemView.context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(mRecipes[position].imageUrl?: Constants.IMAGE_NOT_FOUND_URL)
                    .into(imageView)
            }
        }
    }

    override fun getItemCount(): Int = mRecipes.size

    override fun getItemViewType(position: Int): Int {
        if(mRecipes.isNotEmpty() && mRecipes[position].title.equals("LOADING")) {
            return LOADING_TYPE
        }
        return RECIPE_TYPE
    }

    private fun isLoading(): Boolean {
        if(mRecipes.isNotEmpty() && mRecipes[mRecipes.size-1].title.equals("LOADING")) {
            return true
        }
        return false
    }


    fun displayLoading() {
        if(!isLoading()) {
            val recipe = Recipe()
            recipe.title = "LOADING"
            val loadingList = listOf<Recipe>(recipe)
            mRecipes = loadingList
            notifyDataSetChanged()
        }
    }

    fun setRecipes(recipes: List<Recipe>) {
        mRecipes = recipes
        notifyDataSetChanged()
    }
}