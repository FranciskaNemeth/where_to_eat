package com.example.wheretoeat.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.wheretoeat.data.MyDatabase
import com.example.wheretoeat.entity.FavoriteRestaurantsEntity
import com.example.wheretoeat.entity.RestaurantImageEntity
import com.example.wheretoeat.entity.UserEntity
import com.example.wheretoeat.repository.MyDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyDatabaseViewModel(application: Application) : AndroidViewModel(application) {
    var readUserData : LiveData<UserEntity>
    var restaurantImages: MutableLiveData<MutableList<RestaurantImageEntity>> = MutableLiveData<MutableList<RestaurantImageEntity>>()
    var favoriteRestaurantsList : MutableLiveData<MutableList<FavoriteRestaurantsEntity>> = MutableLiveData<MutableList<FavoriteRestaurantsEntity>>()

    private val repository : MyDatabaseRepository

    init {
        val databaseDao = MyDatabase.getDatabase(application).userDao()
        repository = MyDatabaseRepository(databaseDao)

        readUserData = repository.readUserData

    }

    // adding a user to the database
    fun addUser(user: UserEntity) {
        // using coroutine for saving data asynchronously to the database and storing user data from database
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
            readUserData = repository.readUserData
        }
    }

    // updating a user in the database
    fun updateUser(user: UserEntity) {
        // using coroutine for updating data asynchronously in the database and storing user data from database
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
            readUserData = repository.readUserData
        }
    }

    // adding a restaurant image to the database
    fun addRestaurantImage(restaurantImageEntity: RestaurantImageEntity) {
        // using coroutine for saving data asynchronously to the database
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages.postValue(repository.addRestaurantImage(restaurantImageEntity))
        }
    }

    // retrieving restaurant images from database
    fun getRestaurantImages(rid: Int) {
        // using coroutine for storing data asynchronously from database
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages.postValue(repository.getRestaurantImages(rid))
        }
    }

    // "generating" next picture's id
    fun getNextPictureId(): Int {
        return repository.getNextPictureId()
    }

    // deleting restaurant image
    fun deleteRestaurantImageId(imageId : Int, rid : Int) {
        // using coroutine for updating data asynchronously in database
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages.postValue(repository.deleteRestaurantImage(imageId, rid))
        }
    }

    // deleting a favorite restaurant
    fun deleteFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) {
        // using coroutine for updating data asynchronously in database
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRestaurantsList.postValue(repository.deleteFavoriteRestaurant(favoriteRestaurant))
        }
    }

    // adding a favorite restaurant o the database
    fun addFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) {
        // using coroutine for storing data asynchronously in database
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRestaurantsList.postValue(repository.addFavoriteRestaurant(favoriteRestaurant))
        }
    }

    // retrieving favorite restaurants from database
    fun getFavoriteRestaurants() {
        // using coroutine for retrieving data asynchronously from database
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRestaurantsList.postValue(repository.getFavoriteRestaurants())
        }
    }

    // retrieving all images of restaurants(for profile picture)
    fun getAllRestaurantImages() {
        // using coroutine for retrieving data asynchronously from database
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages.postValue(repository.getAllRestaurantImages())
        }
    }
}