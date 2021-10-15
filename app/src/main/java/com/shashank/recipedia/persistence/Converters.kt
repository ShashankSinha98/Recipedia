package com.shashank.recipedia.persistence

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val TAG = "Converters"

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        Log.d(TAG,"fromString value: $value")
        value?.let {
            return Gson().fromJson(it, Array<String>::class.java)?.toList()
        }
        return null
    }


    @TypeConverter
    fun fromArrayList(list: List<String>?): String? {
        Log.d(TAG,"fromArrayList: $list")
        list?.let {
            return Gson().toJson(list)
        }
        return null
    }
}