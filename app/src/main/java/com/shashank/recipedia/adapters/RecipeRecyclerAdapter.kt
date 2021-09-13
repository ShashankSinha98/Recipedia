package com.shashank.recipedia.adapters

import android.view.LayoutInflater
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

    private var mRecipes: List<Recipe> = listOf<Recipe>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_recipe_list_item,
                                                                parent, false)
        return RecipeViewHolder(view, mOnRecipeListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

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

    override fun getItemCount(): Int = mRecipes.size

    fun setRecipes(recipes: List<Recipe>) {
        mRecipes = recipes
        notifyDataSetChanged()
    }
}