package com.shashank.recipedia.util

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


    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): LiveData<ApiResponse<R>> {

        return object : LiveData<ApiResponse<R>>() {

            override fun onActive() {
                super.onActive()

                val apiResponse = ApiResponse<R>()
                call.enqueue(object : Callback<R> {

                    override fun onResponse(call: Call<R>, response: Response<R>) {
                        postValue(apiResponse.create(response))
                    }

                    override fun onFailure(call: Call<R>, t: Throwable) {
                        apiResponse.create(t)
                    }

                })

            }
        }
    }

}