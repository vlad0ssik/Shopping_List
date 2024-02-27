package com.example.shoppinglist.domain

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun editShopList(shopItem: ShopItem){
        shopListRepository.editShopList(shopItem)
    }
}