package com.example.shoppinglist.domain

class AddShopItemUseCase (private  val shopListRepository: ShopListRepository) {
    suspend fun addShopList(shopItem: ShopItem){
shopListRepository.addShopList(shopItem)
    }
}