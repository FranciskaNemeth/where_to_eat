package com.example.wheretoeat.api

import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City
import com.example.wheretoeat.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.Query

// Interface for retrofit to retrieve restaurants and cities data from API
interface ApiInterface {
    @GET("restaurants")
    suspend fun getPost(@Query("city")city : String) : City

    @GET("restaurants")
    suspend fun getRestaurantsPaginated(@Query("city") city: String, @Query("page")page: Int): City

    @GET("cities")
    suspend fun getPostCities() : CitiesList
}