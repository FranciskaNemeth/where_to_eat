package com.example.wheretoeat.repository

import android.util.Log
import com.example.wheretoeat.api.RetrofitInstance
import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City
import com.example.wheretoeat.model.Restaurant

class Repository {
    suspend fun getPost(cityName : String) : City? {
        lateinit var post : City
        try {
            post = RetrofitInstance.api.getPost(cityName)
        }
        catch (e: Exception){
            return null
        }
        return post
    }

    suspend fun getRestaurantsPaginated(cityName: String, page: Int): City {
        return RetrofitInstance.api.getRestaurantsPaginated(cityName, page)
    }

    suspend fun getPostCities() : CitiesList {
        return RetrofitInstance.api.getPostCities()
    }
}