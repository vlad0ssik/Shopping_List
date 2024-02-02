package com.example.shoppinglist.domain

interface ShopListRepository {
    fun addShopList(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int):ShopItem
    fun getShopList(): List<ShopItem>
    fun editShopList(shopItem: ShopItem)

}