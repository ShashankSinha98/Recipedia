package com.shashank.recipedia.requests.responses

import retrofit2.Response
import java.io.IOException

open class ApiResponse<T> {

    // for error
    fun create(error: Throwable): ApiResponse<T> {
        var errorMsg = "Unknown error\nCheck network connection"
        error.message?.let { msg->
            if(msg.isNotEmpty()) errorMsg = msg
        }

        return ApiErrorResponse(errorMsg)
    }

    // for response - can be success or failure
    fun create(response: Response<T>): ApiResponse<T> {
        if(response.isSuccessful) {
            val body: T? = response.body()

            return if(body==null || response.code()==204) {
                ApiEmptyResponse()
            } else {
                ApiSuccessResponse(body)
            }

        } else {
            var errorMsg = ""
            try {
                errorMsg = response.errorBody().toString()
            } catch (e: IOException) {
                e.printStackTrace()
                errorMsg = response.message()
            }

            return ApiErrorResponse(errorMsg)
        }
    }

    class ApiSuccessResponse<T>(private val body: T): ApiResponse<T>() {
        fun getBody() = body
    }

    class ApiErrorResponse<T>(private val errorMessage: String): ApiResponse<T>() {
        fun getErrorMessage() = errorMessage
    }

    class ApiEmptyResponse<T>(): ApiResponse<T>() { }

}


