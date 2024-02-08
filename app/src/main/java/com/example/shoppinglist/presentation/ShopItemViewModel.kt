package com.example.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.EditShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseActivity = MutableLiveData<Unit>()
    val shouldCloseActivity: LiveData<Unit>
        get() = _shouldCloseActivity


    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopList(shopItem)
            finishWork()
        }
}

fun editShopItem(inputName: String?, inputCount: String?) {
    val name = parseName(inputName)
    val count = parseCount(inputCount)
    val fieldsValid = validateInput(name, count)
    if (fieldsValid) {
        _shopItem.value?.let {
            val item = it.copy(name = name, count = count)
            editShopItemUseCase.editShopList(item)
            finishWork()
        }
    }
}

fun getShopItem(shopItemId: Int): ShopItem {
    val item = getShopItemUseCase.getShopItem(shopItemId)
    _shopItem.value = item
    return item
}

fun parseCount(inputCount: String?): Int {
    return try {
        inputCount?.trim()?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }
}

private fun parseName(inputName: String?): String {
    return inputName?.trim() ?: ""
}

private fun validateInput(name: String, count: Int): Boolean {
    var result = true
    if (name.isBlank()) {
        result = false /// do message to edit texts
        _errorInputName.value = true

    }
    if (count <= 0) {
        result = false
        _errorInputCount.value = true
    }
    return result
}

public fun resetErrorInputName() {
    _errorInputName.value = false
}

public fun resetErrorInputCount() {
    _errorInputCount.value = false
}

private fun finishWork() {
    _shouldCloseActivity.value = Unit
}
}
