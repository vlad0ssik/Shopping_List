package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopListRepositoryImpl(
    application: Application,
) : ShopListRepository {

    private val db = AppDatabase.createInstance(application)
    private val shopListDao = db.shopListDao()
    private val shopListMapper = ShopListMapper()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override suspend fun addShopList(shopItem: ShopItem) {
        coroutineScope.launch {
            shopListDao.addShopItem(shopListMapper.mapEntityToDbModel(shopItem))
        }
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItemId = shopItem.id)
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem =
        shopListMapper.mapDbModelToEntity(shopListDao.getShopItem(shopItemId))


    override fun getShopList(): LiveData<List<ShopItem>> =
        MediatorLiveData<List<ShopItem>>().apply {
            addSource(shopListDao.getShopList()) {
                value = shopListMapper.maDbModelToEntityList(it)
            }
        }

    override suspend fun editShopList(shopItem: ShopItem) {
        shopListDao.addShopItem(shopListMapper.mapEntityToDbModel(shopItem))

    }
}