package com.example.shoppinglist.presentation


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.domain.ShopItem

class ShopItemFragment : Fragment() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishListener: OnEditingFinishListener
    private var shopItemID = ShopItem.UNDEFINED_ID
    private var screenMode: String = MODE_UNKNOWN
    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishListener) onEditingFinishListener = context
        else throw Exception("Activity must implement interface :\"onEditingFinishListener\"")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextListeners()
        chooseScreenMode()
        setObservesOnLiveData()
        Log.d("experimental, onViewCreated", "nu onViewCreated")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

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

    interface OnEditingFinishListener {
        fun onEditingFinish()
    }

    private fun setObservesOnLiveData() {
        viewModel.shouldCloseActivity.observe(viewLifecycleOwner) {
            onEditingFinishListener.onEditingFinish()
        }
    }

    private fun addTextListeners() {
        binding.nameTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.countTextInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener {
            val name = binding.nameTextInputEditText.text?.toString()
            val count = binding.countTextInputEditText.text?.toString()
            viewModel.addShopItem(name, count)
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemID)
        binding.buttonSave.setOnClickListener {
            val name = binding.nameTextInputEditText.text?.toString()
            val count = binding.countTextInputEditText.text?.toString()
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