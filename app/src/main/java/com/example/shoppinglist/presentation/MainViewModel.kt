package com.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application = application)
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)

    val sc = viewModelScope
    val shopList =
        getShopListUseCase.getShopList()



    fun editShopItem(shopItem: ShopItem) {
        sc.launch {
            editShopItemUseCase.editShopList(shopItem)
        }

    }

     fun deleteShopItem(shopItem: ShopItem) {
         sc.launch {
             deleteShopItemUseCase.deleteShopItem(shopItem)
         }

    }

     fun changeEnableState(shopItem: ShopItem) {
         sc.launch {
             val newItem = shopItem.copy(enabled = !shopItem.enabled)
             editShopItem(newItem)
         }

    }


}