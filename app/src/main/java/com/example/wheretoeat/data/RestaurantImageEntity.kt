package com.example.wheretoeat.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_image_table")
data class RestaurantImageEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    val rid: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val imageData : ByteArray
)