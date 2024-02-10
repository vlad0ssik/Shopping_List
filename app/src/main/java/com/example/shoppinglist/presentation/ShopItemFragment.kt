package com.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment() : Fragment() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var nameTextInputEditText: TextInputEditText
    private lateinit var countTextInputLayout: TextInputLayout
    private lateinit var countTextInputEditText: TextInputEditText
    private lateinit var buttonSave: Button
    private lateinit var onEditingFinishListener: OnEditingFinishListener
    private var shopItemID = ShopItem.UNDEFINED_ID
    private var screenMode: String = MODE_UNKNOWN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("experimental, onCreateView", "nu onCreateView")
        return inflater.inflate(R.layout.fragment_shop_item, container, false)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishListener) onEditingFinishListener = context
        else throw Exception("Activity must implement interface :\"onEditingFinishListener\"")
        Log.d("experimental, onAttach", "nu onAttach")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextListeners()
        chooseScreenMode()
        setObservesOnLiveData()
        Log.d("experimental, onViewCreated", "nu onViewCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
        Log.d("experimental, onCreate", "nu onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("experimental, onStart", "nu onStart")
    }

    private fun chooseScreenMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
            else -> throw Exception("ShopItemActivity: calling incorect mode")
        }
    }

    public interface OnEditingFinishListener {
        fun onEditingFinish()
    }

    private fun setObservesOnLiveData() {
        viewModel.shouldCloseActivity.observe(viewLifecycleOwner) {
            onEditingFinishListener?.onEditingFinish()
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it) {
                countTextInputLayout.error = "Type correct count"
            } else {
                countTextInputLayout.error = null
            }
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
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


    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("No any info about mode (ShopItemActivity)")
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("No any info about mode (ShopItemActivity)")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("No extra about item in edit mode (ShopItemActivity)")
            }
            shopItemID = args.getInt(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View) {
        with(view) {
            nameTextInputLayout = findViewById(R.id.nameTextInputLayout)
            nameTextInputEditText = findViewById(R.id.nameTextInputEditText)
            countTextInputLayout = findViewById(R.id.countTextInputLayout)
            countTextInputEditText = findViewById(R.id.countTextInputEditText)
            buttonSave = findViewById(R.id.buttonSave)
        }
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

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                    putInt(EXTRA_SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}