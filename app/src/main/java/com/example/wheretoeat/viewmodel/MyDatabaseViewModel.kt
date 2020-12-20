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

    fun addUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
            readUserData = repository.readUserData
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
            readUserData = repository.readUserData
        }
    }

    fun addRestaurantImage(restaurantImageEntity: RestaurantImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages.postValue(repository.addRestaurantImage(restaurantImageEntity))
        }
    }

    fun getRestaurantImages(rid: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages.postValue(repository.getRestaurantImages(rid))
        }
    }

    fun getNextPictureId(): Int {
        return repository.getNextPictureId()
    }

    fun deleteRestaurantImageId(imageId : Int, rid : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantImages.postValue(repository.deleteRestaurantImage(imageId, rid))
        }
    }

    fun deleteFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRestaurantsList.postValue(repository.deleteFavoriteRestaurant(favoriteRestaurant))
        }
    }

    fun addFavoriteRestaurant(favoriteRestaurant: FavoriteRestaurantsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRestaurantsList.postValue(repository.addFavoriteRestaurant(favoriteRestaurant))
        }
    }

    fun getFavoriteRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteRestaurantsList.postValue(repository.getFavoriteRestaurants())
        }
    }
}