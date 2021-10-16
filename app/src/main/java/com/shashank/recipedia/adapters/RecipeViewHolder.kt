package com.shashank.recipedia.adapters

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.shashank.recipedia.R
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.util.Constants
import kotlin.math.roundToInt

class RecipeViewHolder(
    itemView: View,
    private val onRecipeListener: OnRecipeListener,
    private val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


    private val title: TextView = itemView.findViewById(R.id.recipe_title)
    private val publisher: TextView = itemView.findViewById(R.id.recipe_publisher)
    private val socialScore: TextView = itemView.findViewById(R.id.recipe_social_score)
    private val imageView: AppCompatImageView = itemView.findViewById(R.id.recipe_image)

    init {
        itemView.setOnClickListener(this)
    }

    fun onBind(recipe: Recipe) {

        title.text = recipe.title?: Constants.TEXT_NOT_FOUND_MSG
        publisher.text = recipe.publisher?: Constants.TEXT_NOT_FOUND_MSG
        socialScore.text = (recipe.socialRank ?: Constants.SCORE_NOT_FOUND_MSG).
        roundToInt().toString()

        requestManager
            .load(recipe.imageUrl?: Constants.IMAGE_NOT_FOUND_URL)
            .into(imageView)
    }


    override fun onClick(p0: View?) {
        onRecipeListener.onRecipeClick(adapterPosition)
    }
}