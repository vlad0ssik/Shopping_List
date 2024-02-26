package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(
    application: Application,
) : ShopListRepository {

    private val db = AppDatabase.createInstance(application)
    private val shopListDao = db.shopListDao()
    private val shopListMapper = ShopListMapper()

    override fun addShopList(shopItem: ShopItem) {
        shopListDao.addShopItem(shopListMapper.mapEntityToDbModel(shopItem))
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItemId = shopItem.id)
    }

    override fun getShopItem(shopItemId: Int): ShopItem =
        shopListMapper.mapDbModelToEntity(shopListDao.getShopItem(shopItemId))


    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {
            addSource(shopListDao.getShopList()){
                value = shopListMapper.maDbModelToEntityList(it)
            }
    }

    override fun editShopList(shopItem: ShopItem) {
        shopListDao.addShopItem(shopListMapper.mapEntityToDbModel(shopItem))

    }
}