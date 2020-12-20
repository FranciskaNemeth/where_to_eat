package com.example.wheretoeat.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.entity.UserEntity

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user : UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    // csak egy user van es azert limit 1 ha veletlenul tobb kerulne az adatbazisba valami bug miatt es ne crasheljen
    @Query("SELECT * FROM user_table LIMIT 1")
    fun readUserData() : LiveData<UserEntity>

    // add photos stuff
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRestaurantImage(restaurantImage : RestaurantImageEntity)

    @Query("SELECT * FROM restaurant_image_table WHERE rid = :restaurantId ORDER by id DESC")
    suspend fun readRestaurantImages(restaurantId: Int) : MutableList<RestaurantImageEntity>

    @Query("SELECT MAX(id) as id, rid, imageData FROM restaurant_image_table GROUP by rid")
    suspend fun readAllRestaurantImages() : MutableList<RestaurantImageEntity>

    @Query("Select MAX(id) + 1 from restaurant_image_table")
    fun getNextPictureId() : Int

    // delete photos
    @Query("DELETE FROM restaurant_image_table WHERE id = :imageId")
    suspend fun deleteRestaurantImage(imageId : Int)

    // favorite restaurants stuff
    @Query("SELECT * FROM favorite_restaurants_table")
    suspend fun readFavoriteRestaurants() : MutableList<FavoriteRestaurantsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteRestaurant(favoriteRestaurant : FavoriteRestaurantsEntity)

    // delete favorite restaurant
    @Delete
    suspend fun deleteFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity)

}