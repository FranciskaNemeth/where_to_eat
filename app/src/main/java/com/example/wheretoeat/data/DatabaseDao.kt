package com.example.wheretoeat.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user : UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    // csak egy user van es azert limit 1 ha veletlenul tobb kerulne az adatbazisba valami bug miatt es ne crasheljen
    @Query("SELECT * FROM user_table LIMIT 1")
    fun readUserData() : LiveData<UserEntity>
}