package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val nameTextView = itemView.findViewById<TextView>(R.id.shop_item_name)
    val quantityTextView = itemView.findViewById<TextView>(R.id.shop_item_quantity)
}