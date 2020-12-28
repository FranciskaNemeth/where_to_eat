package com.example.wheretoeat.repository

import androidx.lifecycle.LiveData
import com.example.wheretoeat.data.*
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.entity.UserEntity

// it's role is to retrieve data from the local database
class MyDatabaseRepository(private val databaseDao: DatabaseDao) {
    var readUserData : LiveData<UserEntity> = databaseDao.readUserData()

    // threadsafe function for adding a user to database and retrieving the added data from database
    suspend fun addUser(user : UserEntity) {
        databaseDao.addUser(user)
        readUserData  = databaseDao.readUserData()
    }

    // threadsafe function for updating a user in database and retrieving the updated data from database
    suspend fun updateUser(user: UserEntity) {
        databaseDao.updateUser(user)
        readUserData  = databaseDao.readUserData()
    }

    // threadsafe function for adding a restaurant picture to database and retrieving the restaurant's images from database
    suspend fun addRestaurantImage(restaurantImage: RestaurantImageEntity) : MutableList<RestaurantImageEntity>{
        databaseDao.addRestaurantImage(restaurantImage)
        return databaseDao.readRestaurantImages(restaurantImage.rid)
    }

    // threadsafe function for retrieving the restaurant images from database
    suspend fun getRestaurantImages(rid: Int) : MutableList<RestaurantImageEntity> {
        return databaseDao.readRestaurantImages(rid)
    }

    // function for "generating" next id for pictures in the database
    fun getNextPictureId(): Int {
        return databaseDao.getNextPictureId()
    }

    // threadsafe function for deleting a restaurant picture from database and retrieving the images data from database
    suspend fun deleteRestaurantImage(imageId : Int, rid : Int) : MutableList<RestaurantImageEntity> {
        databaseDao.deleteRestaurantImage(imageId)
        return databaseDao.readRestaurantImages(rid)
    }

    // threadsafe function for deleting a favorite restaurant from database and retrieving data(updating)
    suspend fun deleteFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) : MutableList<FavoriteRestaurantsEntity> {
        databaseDao.deleteFavoriteRestaurant(favoriteRestaurant)
        return databaseDao.readFavoriteRestaurants()
    }

    // threadsafe function for adding a favorite restaurant to database and retrieving data(updating) from database
    suspend fun addFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) : MutableList<FavoriteRestaurantsEntity> {
        databaseDao.addFavoriteRestaurant(favoriteRestaurant)
        return databaseDao.readFavoriteRestaurants()
    }

    // threadsafe function for retrieving favorite restaurants data
    suspend fun getFavoriteRestaurants() : MutableList<FavoriteRestaurantsEntity> {
        return databaseDao.readFavoriteRestaurants()
    }

    // threadsafe function for retrieving all images from the database
    suspend fun getAllRestaurantImages(): MutableList<RestaurantImageEntity> {
        return databaseDao.readAllRestaurantImages()
    }
}