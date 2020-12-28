package com.example.wheretoeat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wheretoeat.repository.Repository

// creates a MainViewModel
class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  MainViewModel(repository) as T
    }
}