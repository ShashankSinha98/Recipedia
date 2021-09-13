package com.shashank.recipedia.util

import android.util.Log
import com.shashank.recipedia.models.Recipe

class Testing {

    companion object {

        fun printRecipes(tag: String, list: List<Recipe>) {
            for(recipe in list) {
                Log.d(tag, "$recipe")
            }
        }


    }
}