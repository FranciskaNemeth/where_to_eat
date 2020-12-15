package com.example.wheretoeat

import android.app.AlertDialog
import android.util.Log
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
    private var lastSearchText: String? = null
    lateinit var cityName: String
    val myResponse : MutableLiveData<City> = MutableLiveData()
    val citiesResponse : MutableLiveData<CitiesList> = MutableLiveData()
    val allRestaurantsList : MutableLiveData<MutableList<Restaurant>> = MutableLiveData()
    val restaurantsFilteredList : MutableLiveData<MutableList<Restaurant>> = MutableLiveData()
    val citiesFilteredList : MutableLiveData<MutableList<String>> = MutableLiveData()
    val error : MutableLiveData<String> = MutableLiveData()

    fun getPost(cityName : String) {
        this.cityName = cityName
        viewModelScope.launch {
            val response : City? = repository.getPost(cityName)
            if (response == null) {
                error.value = "Server Error!"
            }
            else {
                myResponse.value = response
                if (allRestaurantsList.value?.isNotEmpty() == true) {
                    allRestaurantsList.value?.clear()
                }
                if (restaurantsFilteredList.value?.isNotEmpty() == true) {
                    restaurantsFilteredList.value?.clear()
                }
                allRestaurantsList.value = response.restaurants
                restaurantsFilteredList.value = response.restaurants
            }

        }
    }

    fun getRestaurantsPaginated(cityName: String, page:Int) {
        this.cityName = cityName
        viewModelScope.launch {
            val response : City = repository.getRestaurantsPaginated(cityName, page)
            myResponse.value = response
            allRestaurantsList.value?.addAll(response.restaurants)
            //TODO:
            // check this out
            filter(lastSearchText)
        }
    }

    fun getPostCities() {
        viewModelScope.launch {
            val response : CitiesList = repository.getPostCities()
            citiesResponse.value = response
            citiesFilteredList.value = response.cities
        }
    }

    fun filter(searchText : String?) {
        lastSearchText = searchText
        val arrayList : ArrayList<Restaurant> = ArrayList()
        if (searchText != null && searchText.isNotEmpty() && allRestaurantsList.value != null) {
            allRestaurantsList.value!!.forEach{
                if (it.name.toLowerCase(Locale.getDefault()).contains(searchText.toLowerCase(Locale.getDefault()))) {
                    arrayList.add(it)
                }
            }
        }
        else {
            arrayList.addAll(allRestaurantsList.value!!)
        }

        restaurantsFilteredList.value = arrayList
    }

    fun filterCities(searchText : String?) {
        val arrayList : ArrayList<String> = ArrayList()
        if (searchText != null && searchText.isNotEmpty() && citiesResponse.value != null) {
            citiesResponse.value!!.cities.forEach{
                if (it.toLowerCase(Locale.getDefault()).contains(searchText.toLowerCase(Locale.getDefault()))) {
                    arrayList.add(it)
                }
            }
        }
        else {
            arrayList.addAll(citiesResponse.value!!.cities)
        }

        citiesFilteredList.value = arrayList
    }
}