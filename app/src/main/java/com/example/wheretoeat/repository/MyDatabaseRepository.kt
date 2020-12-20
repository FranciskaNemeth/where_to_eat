package com.example.wheretoeat.repository

import androidx.lifecycle.LiveData
import com.example.wheretoeat.data.*
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.entity.UserEntity

class MyDatabaseRepository(private val databaseDao: DatabaseDao) {
    var readUserData : LiveData<UserEntity> = databaseDao.readUserData()

    suspend fun addUser(user : UserEntity) {
        databaseDao.addUser(user)
        readUserData  = databaseDao.readUserData()
    }

    suspend fun updateUser(user: UserEntity) {
        databaseDao.updateUser(user)
        readUserData  = databaseDao.readUserData()
    }

    suspend fun addRestaurantImage(restaurantImage: RestaurantImageEntity) : MutableList<RestaurantImageEntity>{
        databaseDao.addRestaurantImage(restaurantImage)
        return databaseDao.readRestaurantImages(restaurantImage.rid)
    }

    suspend fun getRestaurantImages(rid: Int) : MutableList<RestaurantImageEntity> {
        return databaseDao.readRestaurantImages(rid)
    }

    fun getNextPictureId(): Int {
        return databaseDao.getNextPictureId()
    }

    suspend fun deleteRestaurantImage(imageId : Int, rid : Int) : MutableList<RestaurantImageEntity> {
        databaseDao.deleteRestaurantImage(imageId)
        return databaseDao.readRestaurantImages(rid)
    }

    suspend fun deleteFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) : MutableList<FavoriteRestaurantsEntity> {
        databaseDao.deleteFavoriteRestaurant(favoriteRestaurant)
        return databaseDao.readFavoriteRestaurants()
    }

    suspend fun addFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) : MutableList<FavoriteRestaurantsEntity> {
        databaseDao.addFavoriteRestaurant(favoriteRestaurant)
        return databaseDao.readFavoriteRestaurants()
    }

    suspend fun getFavoriteRestaurants() : MutableList<FavoriteRestaurantsEntity> {
        return databaseDao.readFavoriteRestaurants()
    }
}