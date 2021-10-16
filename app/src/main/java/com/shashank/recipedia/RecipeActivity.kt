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
import com.shashank.recipedia.util.Constants
import com.shashank.recipedia.util.Resource
import com.shashank.recipedia.viewmodels.RecipeViewModel
import kotlin.math.roundToInt

class RecipeActivity: BaseActivity() {

    private val TAG = "RecipeActivity"

    private lateinit var mRecipeImage: AppCompatImageView
    private lateinit var mRecipeTitle: TextView
    private lateinit var mRecipeRank: TextView
    private lateinit var mRecipeIngredientsContainer: LinearLayout
    private lateinit var mScrollView: ScrollView
    private lateinit var mRecipeViewModel: RecipeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)


        mRecipeImage  = findViewById(R.id.recipe_image)
        mRecipeTitle  = findViewById(R.id.recipe_title)
        mRecipeRank = findViewById(R.id.recipe_social_score)
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container)
        mScrollView = findViewById(R.id.parent)

        mRecipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)

        showProgressBar(true)
        getIncomingIntent()
    }



    private fun getIncomingIntent() {
        if(intent.hasExtra("recipe")) {
            val recipe: Recipe? = intent.getParcelableExtra<Recipe>("recipe")
            Log.d(TAG,"getIncomingIntent: ${recipe?.title}")

            recipe?.let { subscribeObservers(recipe.recipeId)  }
        }
    }

    private fun subscribeObservers(recipeId: String) {
        mRecipeViewModel.searchRecipeApi(recipeId).observe(this, Observer { recipeResource ->

            if(recipeResource!=null) {
                if(recipeResource.data!=null) {

                    when(recipeResource.status) {

                        Resource.Status.LOADING -> {
                            showProgressBar(true)
                        }

                        Resource.Status.ERROR -> {
                            Log.d(TAG,"onChanged: status: ERROR, Recipe: ${recipeResource.data}")
                            Log.e(TAG, "onChanged: ERROR msg: ${recipeResource.message}")
                            showParent()
                            showProgressBar(false)
                            setRecipeProperties(recipeResource.data)
                        }

                        Resource.Status.SUCCESS -> {
                            Log.d(TAG,"onChanged: cache has been refreshed")
                            Log.d(TAG,"onChanged: status: SUCCESS, Recipe: ${recipeResource.data.title}")
                            showParent()
                            showProgressBar(false)
                            setRecipeProperties(recipeResource.data)
                        }
                    }

                }
            }
        })
    }


    private fun setRecipeProperties(recipe: Recipe?) {
        if(recipe!=null) {
            val options = RequestOptions()
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.error_img)

            Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(recipe.imageUrl)
                .into(mRecipeImage)

            mRecipeTitle.text = recipe.title
            mRecipeRank.text = (recipe.socialRank)?.roundToInt().toString()
            setIngredients(recipe)
        }
    }


    private fun setIngredients(recipe: Recipe) {
        mRecipeIngredientsContainer.removeAllViews()

        if(recipe.ingredients!=null) {
            for(ingredient in recipe.ingredients?: listOf()) {
                val textView = TextView(this)
                textView.text = ingredient
                textView.textSize = 15f
                textView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                mRecipeIngredientsContainer.addView(textView)
            }
        } else {
            Log.d(TAG,"Show Error retreiving ingredients msg")
            val textView = TextView(this)
            textView.text = "Error retreiving ingredients\nCheck network connection."
            textView.textSize = 15f
            textView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            mRecipeIngredientsContainer.addView(textView)

        }
    }

    private fun showParent() {
        mScrollView.visibility = View.VISIBLE
    }

}