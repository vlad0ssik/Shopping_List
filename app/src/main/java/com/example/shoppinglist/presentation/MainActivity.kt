package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupRecyclerView()
        viewModel.shopList.observe(this) {
            adapter.shopList = it
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ShopListAdapter()
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.recycledViewPool.setMaxRecycledViews(R.layout.item_disabled_card,ShopListAdapter.MAX_POOL_SIZE)
        recyclerView.recycledViewPool.setMaxRecycledViews(R.layout.item_enabled_card,ShopListAdapter.MAX_POOL_SIZE)
    }

    private fun setupSwipeListener(recyclerView: RecyclerView?) {
        val itemTouchHelper = ItemTouchHelper(callbackMethod)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupClickListener() {
        adapter.onShopItemClickListener = {
            TODO("CALL NEW INTENT FOR REDO AN ITEM")
        }
    }

    private fun setupLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    val callbackMethod = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = adapter.shopList[viewHolder.adapterPosition]
            when(direction){
                ItemTouchHelper.LEFT -> viewModel.deleteShopItem(pos)
                    ItemTouchHelper.RIGHT ->viewModel.deleteShopItem(pos)
            else ->false
            }
        }
    }

}