package com.example.wheretoeat.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val userName : String,
    val address : String,
    val phoneNumber : String,
    val email : String
)