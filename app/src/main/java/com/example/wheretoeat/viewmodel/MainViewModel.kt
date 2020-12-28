package com.example.wheretoeat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.model.CitiesList
import com.example.wheretoeat.model.City
import com.example.wheretoeat.model.Restaurant
import com.example.wheretoeat.repository.Repository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class MainViewModel(private val repository: Repository) : ViewModel() {
    private var lastSearchText: String? = null
    lateinit var cityName: String
    val myResponse : MutableLiveData<City> = MutableLiveData()
    val citiesResponse : MutableLiveData<CitiesList> = MutableLiveData()
    val allRestaurantsList : MutableLiveData<MutableList<Restaurant>> = MutableLiveData()
    val restaurantsFilteredList : MutableLiveData<MutableList<Restaurant>> = MutableLiveData()
    val citiesFilteredList : MutableLiveData<MutableList<String>> = MutableLiveData()
    val error : MutableLiveData<String> = MutableLiveData()
    val selectedRestaurant : MutableLiveData<Restaurant> = MutableLiveData()
    val selectedFavoriteRestaurant : MutableLiveData<FavoriteRestaurantsEntity> = MutableLiveData()
    var selectedRestaurantId by Delegates.notNull<Int>()

    // getting restaurants list for a city
    fun getPost(cityName : String) {
        this.cityName = cityName
        // using coroutine for loading data asynchronously
        viewModelScope.launch {
            val response : City? = repository.getPost(cityName)
            if (response == null) {
                error.value = "Server ERROR!"
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

    // getting restaurants for given page and city
    fun getRestaurantsPaginated(cityName: String, page:Int) {
        this.cityName = cityName
        // using coroutine for loading data asynchronously
        viewModelScope.launch {
            val response : City? = repository.getRestaurantsPaginated(cityName, page)
            if(response == null) {
                error.value = "ERROR: getting data from server!"
            }
            else {
                myResponse.value = response
                allRestaurantsList.value?.addAll(response.restaurants)
                filter(lastSearchText)
            }

        }
    }

    // getting cities list
    fun getPostCities() {
        // using coroutine for loading data asynchronously
        viewModelScope.launch {
            val response : CitiesList? = repository.getPostCities()

            if(response == null) {
                error.value = "ERROR: getting data from server!"
            }
            else {
                citiesResponse.value = response
                citiesFilteredList.value = response.cities
            }
        }
    }

    // filtering restaurants by their names
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

    // filtering cities by their names
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

    // saving selected restaurant from recyclerview
    fun setSelectedRestaurant(restaurant: Restaurant) {
        selectedRestaurant.value = restaurant
        selectedRestaurantId = restaurant.id
    }

    // saving selected favorite restaurant from recyclerview
    fun setSelectedFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) {
        selectedFavoriteRestaurant.value = favoriteRestaurant
        selectedRestaurantId = favoriteRestaurant.id
        selectedRestaurant.value = convertFavoriteRestaurantEntityToRestaurant(favoriteRestaurant)
    }

    // converting a restaurant to favorite restaurant(dislike case)
    fun convertRestaurantToFavoriteRestaurantEntity(restaurant : Restaurant) : FavoriteRestaurantsEntity {
        return FavoriteRestaurantsEntity(
                restaurant.id,
                restaurant.name,
                restaurant.address,
                restaurant.city,
                restaurant.state,
                restaurant.area,
                restaurant.postal_code,
                restaurant.country,
                restaurant.phone,
                restaurant.lat,
                restaurant.lng,
                restaurant.price,
                restaurant.reserve_url,
                restaurant.mobile_reserve_url,
                restaurant.image_url
        )
    }

    // converting favorite restaurant to restaurant(like case)
    fun convertFavoriteRestaurantEntityToRestaurant(favoriteRestaurant : FavoriteRestaurantsEntity) : Restaurant {
        return Restaurant(
                favoriteRestaurant.id,
                favoriteRestaurant.name,
                favoriteRestaurant.address,
                favoriteRestaurant.city,
                favoriteRestaurant.state,
                favoriteRestaurant.area,
                favoriteRestaurant.postal_code,
                favoriteRestaurant.country,
                favoriteRestaurant.phone,
                favoriteRestaurant.lat,
                favoriteRestaurant.lng,
                favoriteRestaurant.price,
                favoriteRestaurant.reserve_url,
                favoriteRestaurant.mobile_reserve_url,
                favoriteRestaurant.image_url
        )
    }
}