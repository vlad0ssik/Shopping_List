package com.example.shoppinglist.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupRecyclerView()
        viewModel.shopList.observe(this) {
            adapter.submitList(it)
        }
        val button = findViewById<FloatingActionButton>(R.id.button_add_item)
        button.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this.applicationContext)
            startActivity(intent)
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
        recyclerView.recycledViewPool.setMaxRecycledViews(
            R.layout.item_disabled_card,
            ShopListAdapter.MAX_POOL_SIZE
        )
        recyclerView.recycledViewPool.setMaxRecycledViews(
            R.layout.item_enabled_card,
            ShopListAdapter.MAX_POOL_SIZE
        )
    }

    private fun setupSwipeListener(recyclerView: RecyclerView?) {
        val itemTouchHelper = ItemTouchHelper(callbackMethod)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupClickListener() {
        adapter.onShopItemClickListener = {
            val intent = ShopItemActivity.newIntentEditItem(this.applicationContext, it.id)
            startActivity(intent)
        }
    }

    private fun setupLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    val callbackMethod =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = adapter.currentList[viewHolder.adapterPosition]
                when (direction) {
                    ItemTouchHelper.LEFT -> viewModel.deleteShopItem(pos)
                    ItemTouchHelper.RIGHT -> viewModel.deleteShopItem(pos)
                    else -> false
                }
            }
        }

}