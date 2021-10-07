package com.shashank.recipedia.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.shashank.recipedia.AppExecutors
import com.shashank.recipedia.requests.responses.ApiResponse

// CacheObject: Type for resource data (db cache)
// RequestObject: Type for API response (network request)

abstract class NetworkBoundResource<CacheObject, RequestObject>(
    private val appExecutors: AppExecutors
) {
    private val TAG = "NetworkBoundResource"


    private val results: MediatorLiveData<Resource<CacheObject>> = MediatorLiveData()

    init {
        // update livedata for laoding status
        results.value = Resource.loading(null) as Resource<CacheObject>

        // observe livedata source from local db
        val dbSource: LiveData<CacheObject> = loadFromDB()

        results.addSource(dbSource, Observer { cacheObject ->
            results.removeSource(dbSource) // stop observing this data source

            if(shouldFetch(cacheObject)) {
                // get data from network
                fetchFromNetwork(dbSource)
            } else {
                results.addSource(dbSource, Observer { cacheObject ->
                    setValue(Resource.success(cacheObject))
                })

            }
        })
    }




    // fetch data from network and insert it into db cache
    /**
        1) Observe local db
        2) if <condition/> query the network
        3) Stop observing local db
        4) Insert new data into local db
        5) begin observing local db again to see refreshed data from network
     */
    private fun fetchFromNetwork(dbSource: LiveData<CacheObject>) {
        Log.d(TAG, "fetchFromNetwork called")

        // update live data for loading status. show cached data meanwhile
        results.addSource(dbSource, Observer { cacheObject->
            setValue(Resource.loading(cacheObject))
        })

        // response from retrofit in form of LiveData (not call)
        val apiResponse: LiveData<ApiResponse<RequestObject>> = createCall()

        results.addSource(apiResponse, Observer { requestObjectApiResponse ->
            /**
             *  3 cases -
             *  1) ApiSuccessResponse
             *  2) ApiEmptyResponse
             *  3) ApiErrorResponse
             * */

            when(requestObjectApiResponse) {

                is ApiResponse.ApiSuccessResponse -> {
                    Log.d(TAG, "onChanged: ApiSuccessResponse")

                    appExecutors.diskIO().execute { // save response to local db - need to be done on background thread, so used diskIO
                        // saveCallResult(processResponse(requestObjectApiResponse))
                        saveCallResult(requestObjectApiResponse.getBody())

                        // pass result to our single source data i.e., results
                        appExecutors.mainThread().execute { // used main thread as we need to SET value to our single source data i.e., results

                            // reading latest data from db
                            results.addSource(loadFromDB(), Observer { cacheObject ->
                                setValue(Resource.success(cacheObject))
                            })
                        }
                    }
                }

                is ApiResponse.ApiEmptyResponse -> {
                    Log.d(TAG, "onChanged: ApiEmptyResponse")
                    // get data from local and set it to our single source data i.e., results
                    appExecutors.mainThread().execute { // used main thread as we need to SET value to our single source data i.e., results
                        // As we didn't update db as response was empty, get old data from db
                        results.addSource(dbSource, Observer { cacheObject -> // NOTE- my logic: loadFromDB() -> dbSource
                            setValue(Resource.success(cacheObject))
                        })
                    }
                }

                is ApiResponse.ApiErrorResponse -> {
                    Log.d(TAG, "onChanged: ApiEmptyResponse")

                    // get data from local and set it to our single source data i.e., results
                    // Also notify that it was error response
                    appExecutors.mainThread().execute { // used main thread as we need to SET value to our single source data i.e., results
                        // As we didn't update db as response has error, get old data from db
                        results.addSource(dbSource, Observer { cacheObject -> // NOTE- my logic: loadFromDB() -> dbSource
                            setValue(Resource.error(
                                requestObjectApiResponse.getErrorMessage(),
                                cacheObject))
                        })
                    }
                }
            }
        })
    }




    private fun processResponse(response: ApiResponse.ApiSuccessResponse<RequestObject>): RequestObject
            = response.getBody()



    // must be performed on Main Thread - for immediate results
    // postValue - background thread, might take some time (when resource/thread are free, only then it will execute)
    private fun setValue(newValue: Resource<CacheObject>) {
        if(results.value != newValue) {
            results.value = newValue
        }
    }


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