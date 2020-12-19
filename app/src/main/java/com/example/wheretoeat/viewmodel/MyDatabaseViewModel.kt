package com.example.wheretoeat.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wheretoeat.data.MyDatabase
import com.example.wheretoeat.data.UserEntity
import com.example.wheretoeat.repository.MyDatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyDatabaseViewModel(application: Application) : AndroidViewModel(application) {
    var readUserData : LiveData<UserEntity>
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
}