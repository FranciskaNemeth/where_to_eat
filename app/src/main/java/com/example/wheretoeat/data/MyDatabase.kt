package com.example.wheretoeat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.entity.UserEntity

@Database(entities = [UserEntity::class, RestaurantImageEntity::class, FavoriteRestaurantsEntity::class], version = 3, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun userDao() : DatabaseDao

    companion object {
        // volatile means that the rights of this field are immediately made visible to other threads
        @Volatile
        private var INSTANCE : MyDatabase? = null

        fun getDatabase(context: Context) : MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // everything in synchronized block will be protected from concurrent execution by multiple threads
            // in this synchronized block we will create an instance of our room database
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}