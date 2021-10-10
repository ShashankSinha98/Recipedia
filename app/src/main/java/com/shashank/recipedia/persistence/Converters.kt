package com.shashank.recipedia.persistence

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    companion object {

        @TypeConverter
        fun fromString(value: String): List<String> {
            return Gson().fromJson(value, Array<String>::class.java).toList()
        }


        @TypeConverter
        fun fromArrayList(list: List<String>): String {
            return Gson().toJson(list)
        }
    }
}