package com.shashank.recipedia.adapters

interface OnRecipeListener {

    fun onRecipeClick(position: Int)

    fun onCategoryClick(category: String)
}