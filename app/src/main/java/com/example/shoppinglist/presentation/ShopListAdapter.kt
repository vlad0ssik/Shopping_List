package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {
    companion object {
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1

        const val MAX_POOL_SIZE = 5
    }

    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopList,value)
            val diffResult = DiffUtil.calculateDiff(callback)
            field = value
            diffResult.dispatchUpdatesTo(this)
        }
    var onShopItemLongClickListener: ((ShopItem)-> Unit)? = null
    var onShopItemClickListener: ((ShopItem)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_enabled_card
            VIEW_TYPE_DISABLED -> R.layout.item_disabled_card
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        return ShopItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.nameTextView.text = shopItem.name
        holder.quantityTextView.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener { onShopItemLongClickListener?.invoke(shopItem)
        true}
        holder.itemView.setOnClickListener{ onShopItemClickListener?.invoke(shopItem)
            true}

        Log.d("Skoka view", "dohua")
    }

    class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nameTextView = itemView.findViewById<TextView>(R.id.shop_item_name)
        val quantityTextView = itemView.findViewById<TextView>(R.id.shop_item_quantity)
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }
}