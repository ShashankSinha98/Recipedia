package com.shashank.recipedia.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(

    @Expose
    @SerializedName("title")
    var title: String?= null,

    @Expose
    @SerializedName("publisher")
    var publisher: String?= null,

    @Expose
    @SerializedName("ingredients")
    var ingredients: List<String>?= null,

    @Expose
    @SerializedName("id")
    var recipeId: String?= null,

    @Expose
    @SerializedName("imageUrl")
    var imageUrl: String?= null,

    @Expose
    @SerializedName("socialUrl")
    var socialRank: Float?= null,

) : Parcelable {

}