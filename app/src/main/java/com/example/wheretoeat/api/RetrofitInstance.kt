package com.example.wheretoeat.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object which uses retrofit
object RetrofitInstance {
    // creating retrofit instance
    // by lazy{} creates a delegate object in which the value is stored once at the first call and it's threadsafe
    private val retrofit by lazy{
        Retrofit.Builder()
                .baseUrl("https://ratpark-api.imok.space/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    // creating an implementation of the API endpoints, defined by the retrofit instance
    val api: ApiInterface by lazy{
        retrofit.create(ApiInterface :: class.java)
    }
}