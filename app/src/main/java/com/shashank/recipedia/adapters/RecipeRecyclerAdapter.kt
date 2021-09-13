package com.shashank.recipedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shashank.recipedia.R
import com.shashank.recipedia.models.Recipe
import kotlin.math.roundToInt

class RecipeRecyclerAdapter(
    private var mRecipes: List<Recipe>,
    private val mOnRecipeListener: OnRecipeListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item,
                                                                parent, false)
        return RecipeViewHolder(view, mOnRecipeListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipeViewHolder = holder as RecipeViewHolder

        recipeViewHolder.apply {
            title.text = mRecipes[position].title?: "NA"
            publisher.text = mRecipes[position].publisher?: "NA"
            socialScore.text = (mRecipes[position].socialRank ?: 0F).roundToInt().toString()
        }

    }

    override fun getItemCount(): Int = mRecipes.size

    fun setRecipes(recipes: List<Recipe>) {
        mRecipes = recipes
        notifyDataSetChanged()
    }
}