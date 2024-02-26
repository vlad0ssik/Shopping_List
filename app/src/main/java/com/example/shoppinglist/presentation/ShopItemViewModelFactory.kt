package com.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShopItemViewModelFactory(private val application: Application) : ViewModelProvider.Factory{

    override fun<T : ViewModel>create(modelClass:Class<T>):T{
        if(modelClass.isAssignableFrom(ShopItemViewModel::class.java)){
            return ShopItemViewModel(application)as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}