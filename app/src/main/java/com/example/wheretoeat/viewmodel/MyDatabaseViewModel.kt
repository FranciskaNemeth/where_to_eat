package com.example.wheretoeat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.wheretoeat.MainViewModel
import com.example.wheretoeat.MainViewModelFactory
import com.example.wheretoeat.data.MyDatabase
import com.example.wheretoeat.data.RestaurantImageEntity
import com.example.wheretoeat.data.UserEntity
import com.example.wheretoeat.repository.MyDatabaseRepository
import com.example.wheretoeat.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class MyDatabaseViewModel(application: Application) : AndroidViewModel(application) {
    var readUserData : LiveData<UserEntity>
    var restaurantImages: MutableLiveData<MutableList<RestaurantImageEntity>> = MutableLiveData<MutableList<RestaurantImageEntity>>()
    private val repository : MyDatabaseRepository
    lateinit var user : LiveData<UserEntity>

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
            repository.addRestaurantImage(restaurantImageEntity)
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
}