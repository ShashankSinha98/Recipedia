package com.shashank.recipedia

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.viewmodels.RecipeViewModel

class RecipeActivity: BaseActivity() {

    private val TAG = "RecipeActivity"

    private lateinit var mRecipeViewModel: RecipeViewModel

    private lateinit var mRecipeImage: AppCompatImageView
    private lateinit var mRecipeTitle: TextView
    private lateinit var mRecipeRank: TextView
    private lateinit var mRecipeIngredientsContainer: LinearLayout
    private lateinit var mScrollView: ScrollView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        mRecipeImage  = findViewById(R.id.recipe_image)
        mRecipeTitle  = findViewById(R.id.recipe_title)
        mRecipeRank = findViewById(R.id.recipe_social_score)
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container)
        mScrollView = findViewById(R.id.parent)

        getIncomingIntent()
        subscribeObservers()
    }



    private fun getIncomingIntent() {
        if(intent.hasExtra("recipe")) {
            val recipe: Recipe? = intent.getParcelableExtra<Recipe>("recipe")
            Log.d(TAG,"getIncomingIntent: ${recipe?.title}")
            recipe?.recipeId?.let {
                mRecipeViewModel.searchRecipeById(recipe.recipeId!!)
            }
        }
    }


    private fun subscribeObservers() {
        mRecipeViewModel.getRecipe().observe(this, Observer { recipe ->

            recipe?.let {
                Log.d(TAG,"onChanged: ${recipe.title}")

                recipe.ingredients?.let {
                    for(ingredient in recipe.ingredients!!) {
                        Log.d(TAG,"onChanged: $ingredient")
                    }
                }

            }
        })
    }
}