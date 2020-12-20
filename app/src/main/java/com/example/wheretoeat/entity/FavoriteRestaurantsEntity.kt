package com.example.wheretoeat.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_restaurants_table")
data class FavoriteRestaurantsEntity(
        @PrimaryKey(autoGenerate = false)
        var id : Int,
        val name : String,
        val address : String,
        val city : String,
        val state : String,
        val area : String,
        val postal_code : String,
        val country : String,
        val phone : String,
        val lat : Double,
        val lng : Double,
        val price : Double,
        val reserve_url : String,
        val mobile_reserve_url : String,
        val image_url : String
)