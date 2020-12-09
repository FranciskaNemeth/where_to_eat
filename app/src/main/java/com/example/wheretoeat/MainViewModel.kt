package com.example.wheretoeat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val repository: Repository) : ViewModel() {
    val myResponse : MutableLiveData<City> = MutableLiveData()
    val citiesResponse : MutableLiveData<CitiesList> = MutableLiveData()
    val filteredList : MutableLiveData<MutableList<Restaurant>> = MutableLiveData()

    fun getPost(cityName : String) {
        viewModelScope.launch {
            val response : City = repository.getPost(cityName)
            myResponse.value = response
            filteredList.value = response.restaurants
        }
    }

    fun getPostCities() {
        viewModelScope.launch {
            val response : CitiesList = repository.getPostCities()
            citiesResponse.value = response
        }
    }

    fun filter(searchText : String) {
        val arrayList : ArrayList<Restaurant> = ArrayList()
        if (searchText.isNotEmpty()) {
            myResponse.value!!.restaurants.forEach{
                if (it.name.toLowerCase(Locale.getDefault()).startsWith(searchText.toLowerCase(Locale.getDefault()))) {
                    arrayList.add(it)
                }
            }
        }
        else {
            arrayList.addAll(myResponse.value!!.restaurants)
        }

        filteredList.value = arrayList
    }
}