package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem

class ShopListMapper {
    fun mapEntityToDbModel(shopItem: ShopItem): ShopItemDbModel = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enabled
    )
    fun mapDbModelToEntity(shopItem: ShopItemDbModel): ShopItem = ShopItem(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enabled
    )
    fun maDbModelToEntityList(list:List<ShopItemDbModel>):List<ShopItem> = list.map { mapDbModelToEntity(it) }

}