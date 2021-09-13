package com.shashank.recipedia.adapters

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.shashank.recipedia.R

class RecipeViewHolder(
    itemView: View,
    private val onRecipeListener: OnRecipeListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


    val title: TextView = itemView.findViewById(R.id.recipe_title)
    val publisher: TextView = itemView.findViewById(R.id.recipe_publisher)
    val socialScore: TextView = itemView.findViewById(R.id.recipe_social_score)
    val imageView: AppCompatImageView = itemView.findViewById(R.id.recipe_image)

    init {
        itemView.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        onRecipeListener.onRecipeClick(adapterPosition)
    }
}