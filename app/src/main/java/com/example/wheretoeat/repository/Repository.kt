package com.example.wheretoeat.repository

import com.example.wheretoeat.api.RetrofitInstance
import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City

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

    suspend fun getRestaurantsPaginated(cityName: String, page: Int): City? {
        lateinit var post : City
        try {
            post = RetrofitInstance.api.getRestaurantsPaginated(cityName, page)
        }
        catch (e : Exception) {
            return null
        }
        return post
    }

    suspend fun getPostCities() : CitiesList? {
        lateinit var post : CitiesList
        try {
            post = RetrofitInstance.api.getPostCities()
        }
        catch (e : Exception) {
            return null
        }
        return post
    }
}