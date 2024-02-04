package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayout = findViewById(R.id.ll_layout)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            Log.d("MainActivityTest", it.toString())
            showList(it)
        }
    }

    private fun showList(list: List<ShopItem>) {
      linearLayout.removeAllViews()
        for(shopItem in list) {
            val view = if(shopItem.enabled) LayoutInflater.from(this).inflate(R.layout.item_enabled_card, linearLayout,false)
            else LayoutInflater.from(this).inflate(R.layout.item_disabled_card, linearLayout,false)

            val shopItemName =view.findViewById<TextView>(R.id.shop_item_name)
            val shopItemQuantity = view.findViewById<TextView>(R.id.shop_item_quantity)
            shopItemName.text = shopItem.name
            shopItemQuantity.text = shopItem.count.toString()
            view.setOnLongClickListener { viewModel.changeEnableState(shopItem)
            true}

            linearLayout.addView(view)

        }





//        }
    }
}