package com.shashank.recipedia.requests

import com.shashank.recipedia.util.Constants
import com.shashank.recipedia.util.Constants.Companion.CONNECTION_TIMEOUT
import com.shashank.recipedia.util.Constants.Companion.READ_TIMEOUT
import com.shashank.recipedia.util.Constants.Companion.WRITE_TIMEOUT
import com.shashank.recipedia.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {

    private val client = OkHttpClient.Builder() // establish connection with server
        .connectTimeout(
            CONNECTION_TIMEOUT,
            TimeUnit.SECONDS
        ) // time between each byte read from server
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS) // time between each byte sent to server
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()

    val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())

    val recipeApi: RecipeApi by lazy {
        retrofitBuilder.build()
            .create(RecipeApi::class.java)
    }
}