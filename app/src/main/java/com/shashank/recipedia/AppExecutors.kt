package com.shashank.recipedia

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


object AppExecutors {

    // for db operations
    private val mDiskIO: Executor = Executors.newSingleThreadExecutor()

    // For returning data from background thread to main thread
    private val mMainThreadExecutor: Executor = MainThreadExecutor()

    fun diskIO(): Executor = mDiskIO

    fun mainThread(): Executor = mMainThreadExecutor

    class MainThreadExecutor: Executor {

        private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

        override fun execute(runnable: Runnable?) {
            runnable?.let {
                mainThreadHandler.post(it)
            }
        }

    }
}