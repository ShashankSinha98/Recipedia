package com.shashank.recipedia.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.shashank.recipedia.requests.responses.ApiResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataCallAdapterFactory: CallAdapter.Factory() {


    /**
     * This method performs a number of checks and then returns the Response type for the Retrofit requests.
     * (@bodyType is the ResponseType. It can be RecipeResponse or RecipeSearchResponse)
     *
     * CHECK #1) returnType returns LiveData
     * CHECK #2) Type LiveData<T> is of ApiResponse.class
     * CHECK #3) Make sure ApiResponse is parameterized. AKA: ApiResponse<T> exists.
     *
     */

    private val TAG = "LiveDataCallAdapterFact"


    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        Log.d(TAG,"get start")
        // Check #1
        // Make sure the CallAdapter is returning a type of LiveData


        // Check #1
        // Make sure the CallAdapter is returning a type of LiveData
        Log.d(TAG,"getRawType(returnType): ${getRawType(returnType)}")
        Log.d(TAG,"LiveData::class: ${LiveData::class.java}")
        if(getRawType(returnType)!= LiveData::class.java) {
            Log.d(TAG,"check1 null")
            return null
        }

        // Check #2
        // Type that LiveData is wrapping
        val observableType: Type = getParameterUpperBound(0, returnType as ParameterizedType)

        // Check if its of type ApiResponse
        val rawObservableType: Type = getRawType(observableType)
        if(rawObservableType!=ApiResponse::class.java) {
            Log.d(TAG,"check2 error")
            throw IllegalArgumentException("Type must be a defined resource")
        }

        // Check #3
        // Check if ApiResponse is parameterized. AKA: Does ApiResponse<T> exists? (must wrap around T)
        // FYI: T is either RecipeResponse or T will be a RecipeSearchResponse
        if(observableType !is ParameterizedType) {
            Log.d(TAG,"check3 null")
            throw IllegalArgumentException("resource must be parameterized")
        }

        val bodyType: Type = getParameterUpperBound(0, observableType as ParameterizedType)
        return LiveDataCallAdapter<Type>(bodyType)
    }
}