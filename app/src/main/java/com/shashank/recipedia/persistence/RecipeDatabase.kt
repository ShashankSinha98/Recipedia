package com.shashank.recipedia.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shashank.recipedia.models.Recipe


@Database(entities = [Recipe::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecipeDatabase: RoomDatabase() {

    companion object {

        private val DATABASE_NAME = "recipes_db"

        private var instance: RecipeDatabase?= null

        fun getInstance(context: Context): RecipeDatabase {
            if(instance==null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }

            return instance!!
        }
    }

    abstract fun getRecipeDao(): RecipeDao

}