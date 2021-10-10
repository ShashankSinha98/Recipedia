package com.shashank.recipedia.models

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "recipes")
data class Recipe(

    @Expose
    @SerializedName("title")
    @ColumnInfo(name="title")
    var title: String?= null,

    @Expose
    @SerializedName("publisher")
    @ColumnInfo(name="publisher")
    var publisher: String?= null,

    @Expose
    @SerializedName("ingredients")
    @ColumnInfo(name="ingredients")
    var ingredients: List<String>?= null,

    @Expose
    @SerializedName("id")
    @PrimaryKey
    @NonNull
    var recipeId: String?= null,

    @Expose
    @SerializedName("imageUrl")
    @ColumnInfo(name="imageUrl")
    var imageUrl: String?= null,

    @Expose
    @SerializedName("socialUrl")
    @ColumnInfo(name="socialRank")
    var socialRank: Float?= null,

    @ColumnInfo(name="timestamp")
    var timestamp: Int

) : Parcelable {

}