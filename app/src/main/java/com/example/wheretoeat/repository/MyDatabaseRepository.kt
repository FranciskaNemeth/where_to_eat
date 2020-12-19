package com.example.wheretoeat.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wheretoeat.data.DatabaseDao
import com.example.wheretoeat.data.MyDatabase
import com.example.wheretoeat.data.UserEntity

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
}