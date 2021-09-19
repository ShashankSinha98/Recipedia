package com.shashank.recipedia

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shashank.recipedia.models.Recipe
import com.shashank.recipedia.models.RecipeDetail
import com.shashank.recipedia.viewmodels.RecipeViewModel
import kotlin.math.roundToInt

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

        showProgressBar(true)
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
        mRecipeViewModel.getRecipeDetail().observe(this, Observer { recipeDetail ->

            recipeDetail?.let {
                if(recipeDetail.recipeId.equals(mRecipeViewModel.getRecipeId())) {
                    setRecipeProperties(recipeDetail)
                }
            }
        })
    }


    private fun setRecipeProperties(recipe: RecipeDetail?) {
        recipe?.let {
            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(R.drawable.loading_img)

            Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(recipe.imageUrl)
                .into(mRecipeImage)

            mRecipeTitle.text = recipe.title?:"NA"
            mRecipeRank.text = (recipe.socialRank ?: 0f).roundToInt().toString()

            mRecipeIngredientsContainer.removeAllViews()
            for(ingredient in recipe.ingredients?: listOf()) {
                val textView = TextView(this)
                textView.text = ingredient
                textView.textSize = 15F
                textView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)

                mRecipeIngredientsContainer.addView(textView)
            }

            showParent()
            showProgressBar(false)
        }
    }

    private fun showParent() {
        mScrollView.visibility = View.VISIBLE
    }

}