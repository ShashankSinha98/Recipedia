package com.shashank.recipedia.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.shashank.recipedia.models.Recipe

@Dao
interface RecipeDao {

    @Insert(onConflict = IGNORE)
    fun insertRecipes(vararg recipe: Recipe): Array<Long>

    @Insert(onConflict = REPLACE)
    fun insertRecipe(recipe: Recipe)

    @Query("UPDATE recipes SET title=:title, publisher=:publisher, imageUrl=:image_url, socialRank=:social_rank WHERE recipeId=:recipe_id")
    fun updateRecipe(
        recipe_id: String,
        title: String,
        publisher: String,
        image_url: String,
        social_rank: Float
    )


    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' OR ingredients LIKE '%' || :query || '%' ORDER BY socialRank DESC LIMIT (:pageNumber*30)")
    fun searchRecipes(
        query: String,
        pageNumber: Int
    ): LiveData<List<Recipe>>


    @Query("SELECT * FROM recipes WHERE recipeId=:recipe_id")
    fun getRecipe(recipe_id: String): LiveData<Recipe>
}