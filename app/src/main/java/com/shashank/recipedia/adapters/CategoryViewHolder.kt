package com.shashank.recipedia.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shashank.recipedia.R
import de.hdodenhof.circleimageview.CircleImageView

class CategoryViewHolder(
    itemView: View,
    val listener: OnRecipeListener
): RecyclerView.ViewHolder(itemView), View.OnClickListener {


    val categoryImage: CircleImageView = itemView.findViewById(R.id.category_image)
    val categoryTitle: TextView = itemView.findViewById(R.id.category_title)

    init {
        itemView.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        listener.onCategoryClick(categoryTitle.text.toString())
    }
}