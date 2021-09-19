package com.shashank.recipedia

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.shashank.recipedia.models.Recipe

class RecipeActivity: BaseActivity() {

    private val mRecipeImage: AppCompatImageView = findViewById(R.id.recipe_image)
    private val mRecipeTitle: TextView = findViewById(R.id.recipe_title)
    private val mRecipeRank: TextView = findViewById(R.id.recipe_social_score)
    private val mRecipeIngredientsContainer: LinearLayout = findViewById(R.id.ingredients_container)
    private val mScrollView: ScrollView = findViewById(R.id.parent)

    init {
        getIncomingIntent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
    }



    fun getIncomingIntent() {
        if(intent.hasExtra("recipe")) {
            val recipe: Recipe? = intent.getParcelableExtra<Recipe>("recipe")
        }
    }
}