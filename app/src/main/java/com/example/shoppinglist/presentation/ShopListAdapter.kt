package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter() : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback())
//    RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>()
{
    companion object {
        private const val VIEW_TYPE_ENABLED = 0
        private const val VIEW_TYPE_DISABLED = 1
        const val MAX_POOL_SIZE = 5
    }


    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

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

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.nameTextView.text = shopItem.name
        holder.quantityTextView.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
            true
        }

    }


    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }
}