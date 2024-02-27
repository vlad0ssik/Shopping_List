package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {
    suspend fun addShopList(shopItem: ShopItem)
    suspend fun deleteShopItem(shopItem: ShopItem)
    suspend fun getShopItem(shopItemId: Int):ShopItem
     fun getShopList(): LiveData<List<ShopItem>>
    suspend fun editShopList(shopItem: ShopItem)

}