package com.shashank.recipedia.requests

import com.shashank.recipedia.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    val recipeApi: RecipeApi by lazy {
        retrofitBuilder.build()
            .create(RecipeApi::class.java)
    }
}