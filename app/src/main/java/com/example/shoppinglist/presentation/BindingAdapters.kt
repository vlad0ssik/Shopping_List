package com.example.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInName")
fun bindErrorInName(textInputLayout: TextInputLayout, boolean: Boolean ){
    if (boolean) {
        textInputLayout.error = "Type correct name"
    } else {
        textInputLayout.error = null
    }
}
@BindingAdapter("errorInCount")
fun bindErrorInCount(textInputLayout: TextInputLayout, boolean: Boolean ){
    if (boolean) {
        textInputLayout.error = "Type correct count"
    } else {
        textInputLayout.error = null
    }
}