package com.example.wheretoeat.api

import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City
import com.example.wheretoeat.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("restaurants")
    suspend fun getPost(@Query("city")city : String) : City

    @GET("cities")
    suspend fun getPostCities() : CitiesList
}