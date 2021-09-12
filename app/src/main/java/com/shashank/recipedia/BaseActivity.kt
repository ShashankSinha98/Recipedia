package com.shashank.recipedia


import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

abstract class BaseActivity: AppCompatActivity() {

    lateinit var mProgressBar: ProgressBar

    override fun setContentView(layoutResID: Int) {

        val constraintLayout: ConstraintLayout = layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout
        val frameLayout: FrameLayout = constraintLayout.findViewById(R.id.activity_content)
        mProgressBar = constraintLayout.findViewById(R.id.progress_bar)

        layoutInflater.inflate(layoutResID, frameLayout, true)

        super.setContentView(constraintLayout)
    }

    fun showProgressBar(visibility: Boolean) {
        println("showProgressBar: $visibility")
        mProgressBar.visibility = if(visibility) View.VISIBLE else View.INVISIBLE
    }
}