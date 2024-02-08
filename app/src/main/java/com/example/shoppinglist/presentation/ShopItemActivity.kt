package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var nameTextInputEditText: TextInputEditText
    private lateinit var countTextInputLayout: TextInputLayout
    private lateinit var countTextInputEditText: TextInputEditText
    private lateinit var buttonSave: Button
    private var screenMode = MODE_UNKNOWN
    private var shopItemID = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews()
        addTextListeners()
        chooseScreenMode()
        setObservesOnLiveData()
    }

    private fun chooseScreenMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
            else -> throw Exception("ShopItemActivity: calling incorect mode")
        }
    }

    private fun setObservesOnLiveData() {
        viewModel.shouldCloseActivity.observe(this) {
            finish()
        }
        viewModel.errorInputCount.observe(this) {
            if (it) {
                countTextInputLayout.error = "Type correct count"
            } else {
                countTextInputLayout.error = null
            }
        }
        viewModel.errorInputName.observe(this) {
            if (it) {
                nameTextInputLayout.error = "Type correct name"
            } else {
                nameTextInputLayout.error = null
            }
        }
    }

    private fun addTextListeners() {
        nameTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        countTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            val name = nameTextInputEditText.text?.toString()
            val count = countTextInputEditText.text?.toString()
            viewModel.addShopItem(name, count)
        }
    }

    private fun launchEditMode() {

        fillEditTexts()
        buttonSave.setOnClickListener {
            val name = nameTextInputEditText.text?.toString()
            val count = countTextInputEditText.text?.toString()
            viewModel.editShopItem(name, count)

        }
    }

    private fun setLiveDataObserves() {

    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("No any info about mode (ShopItemActivity)")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("No any info about mode (ShopItemActivity)")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("No extra about item in edit mode (ShopItemActivity)")
            }
            shopItemID = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews() {
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout)
        nameTextInputEditText = findViewById(R.id.nameTextInputEditText)
        countTextInputLayout = findViewById(R.id.countTextInputLayout)
        countTextInputEditText = findViewById(R.id.countTextInputEditText)
        buttonSave = findViewById(R.id.buttonSave)
    }


    private fun fillEditTexts() {

        val item = viewModel.getShopItem(shopItemID)
        Log.d("Item: ", item.toString())
        nameTextInputEditText.setText(item.name)
        countTextInputEditText.setText(item.count.toString())

    }


    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""
        private const val MODE_ADD = "mode_add"


        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            Log.d("Item: ", shopItemId.toString())
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}