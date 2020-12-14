package com.example.wheretoeat.repository

import com.example.wheretoeat.api.RetrofitInstance
import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City
import com.example.wheretoeat.model.Restaurant

class Repository {
    suspend fun getPost(cityName : String) : City {
        return RetrofitInstance.api.getPost(cityName)
    }

    suspend fun getRestaurantsPaginated(cityName: String, page: Int): City {
        return RetrofitInstance.api.getRestaurantsPaginated(cityName, page)
    }

    suspend fun getPostCities() : CitiesList {
        return RetrofitInstance.api.getPostCities()
    }
}