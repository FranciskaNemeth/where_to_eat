package com.example.wheretoeat.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wheretoeat.data.*

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

    suspend fun addRestaurantImage(restaurantImage: RestaurantImageEntity) {
        databaseDao.addRestaurantImage(restaurantImage)
    }

    suspend fun getRestaurantImages(rid: Int) : MutableList<RestaurantImageEntity> {
        return databaseDao.readRestaurantImages(rid)
    }

    fun getNextPictureId(): Int {
        return databaseDao.getNextPictureId()
    }
}