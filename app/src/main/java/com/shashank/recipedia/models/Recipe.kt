package com.shashank.recipedia.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(

    @Expose
    @SerializedName("title")
    private val title: String?= null,

    @Expose
    @SerializedName("publisher")
    private val publisher: String?= null,

    @Expose
    @SerializedName("ingredients")
    private val ingredients: List<String>?= null,

    @Expose
    @SerializedName("recipe_id")
    private val recipeId: String?= null,

    @Expose
    @SerializedName("image_url")
    private val imageUrl: String?= null,

    @Expose
    @SerializedName("social_rank")
    private val socialRank: Float?= null,

) : Parcelable {

}