package com.shashank.recipedia.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.shashank.recipedia.requests.responses.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LiveDataCallAdapter<R>(
    private val responseType: Type
): CallAdapter<R, LiveData<ApiResponse<R>>> {

    private val TAG = "LiveDataCallAdapter"

    override fun responseType(): Type {
        Log.d(TAG, "responseType called")
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {
        Log.d(TAG, "adapt called")
        return object : LiveData<ApiResponse<R>>() {

            override fun onActive() {
                super.onActive()

                val apiResponse = ApiResponse<R>()
                call.enqueue(object : Callback<R> {

                    override fun onResponse(call: Call<R>, response: Response<R>) {
                        Log.d(TAG,"onResponse called: ${response.body()}")
                        postValue(apiResponse.create(response))
                    }

                    override fun onFailure(call: Call<R>, t: Throwable) {
                        Log.d(TAG,"onFailure called: ${t.message}")
                        postValue(apiResponse.create(t))
                    }
                })

            }
        }
    }

}