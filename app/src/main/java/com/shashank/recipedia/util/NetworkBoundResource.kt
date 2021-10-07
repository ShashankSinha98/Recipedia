package com.shashank.recipedia.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shashank.recipedia.requests.responses.ApiResponse

// CacheObject: Type for resource data (db cache)
// RequestObject: Type for API response (network request)

abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private val results: MutableLiveData<Resource<CacheObject>> = MutableLiveData()

    // called to save the results of API response into db
    protected abstract fun saveCallResult(item: RequestObject)

    // Called with data in db to decide whether to fetch
    // potentially updated data from network
    /*** Eg- Suppose recipe data is last updated 1 week ago, and app sync
     recipe data which is > 1 week old ***/
    protected abstract fun shouldFetch(data: CacheObject): Boolean


    // called to get cached data from db
    protected abstract fun loadFromDB(): LiveData<CacheObject>


    // Called to create API call
    /*** Note- We will convert Retrofit Call to LiveData
    // No need to use executors for background operation for network ***/
    protected abstract fun createCall(): LiveData<ApiResponse<RequestObject>>


    // Return a LiveData object that represents the resource that's implemented
    // in the base class
    /*** Return data to UI - single source of truth - always cache data ***/
    fun getAsLiveData(): LiveData<Resource<CacheObject>> = results
}