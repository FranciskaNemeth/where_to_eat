package com.example.wheretoeat.repository

import com.example.wheretoeat.api.RetrofitInstance
import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City

// implementing threadsafe functions for data retrieval
// using try-catch for error handling in case no data is received or API is down
class Repository {
    // getting restaurants for a city
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

    // getting given page of restaurants for a given city
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

    // getting cities list
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