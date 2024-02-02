package com.example.shoppinglist.domain

class EditShopItemUseCase(private  val shopListRepository: ShopListRepository) {
    fun editShopList(shopItem: ShopItem){
        shopListRepository.editShopList(shopItem)
        TODO()
    }
}