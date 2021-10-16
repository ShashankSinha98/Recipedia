package com.shashank.recipedia.adapters

import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.shashank.recipedia.R
import com.shashank.recipedia.models.Recipe
import de.hdodenhof.circleimageview.CircleImageView

class CategoryViewHolder(
    itemView: View,
    private val listener: OnRecipeListener,
    private val requestManager: RequestManager
): RecyclerView.ViewHolder(itemView), View.OnClickListener {


    private val categoryImage: CircleImageView = itemView.findViewById(R.id.category_image)
    private val categoryTitle: TextView = itemView.findViewById(R.id.category_title)

    init {
        itemView.setOnClickListener(this)
    }

    fun onBind(recipe: Recipe) {
        val path: Uri = Uri.parse("android.resource://com.shashank.recipedia/drawable/${recipe.imageUrl}")

        requestManager
            .load(path)
            .into(categoryImage)

        categoryTitle.text = recipe.title
    }


    override fun onClick(p0: View?) {
        listener.onCategoryClick(categoryTitle.text.toString())
    }
}