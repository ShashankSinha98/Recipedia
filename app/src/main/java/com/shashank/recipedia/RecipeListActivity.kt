package com.shashank.recipedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible

class RecipeListActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        findViewById<Button>(R.id.test_btn).setOnClickListener {
            showProgressBar(!mProgressBar.isVisible)
        }
    }
}