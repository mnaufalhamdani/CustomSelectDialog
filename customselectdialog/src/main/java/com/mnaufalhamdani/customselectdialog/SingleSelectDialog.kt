package com.mnaufalhamdani.customselectdialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.mnaufalhamdani.customselectdialog.adapter.SingleSelectItem
import com.mnaufalhamdani.customselectdialog.databinding.LytSingleSelectBinding
import com.mnaufalhamdani.customselectdialog.domain.SingleSelectItemDomain


class SingleSelectDialog(context: Context) : AppCompatDialog(context) {
    private lateinit var binding: LytSingleSelectBinding
    private var dialog: Dialog? = null
    private var listItem: MutableList<SingleSelectItemDomain> = mutableListOf()
    private var listItemSearch: MutableList<SingleSelectItemDomain> = mutableListOf()
    private var title: String = "Choose One Item"
    private var isShowEmptyButton: Boolean = false
    private var isHiddenSearch: Boolean = false
    private var isCancel: Boolean = true
    private lateinit var callback: SubmitCallbackListener

    fun listItem(listItem: MutableList<SingleSelectItemDomain>): SingleSelectDialog {
        this.listItem = listItem
        return this
    }

    fun setTitle(title: String): SingleSelectDialog {
        this.title = title
        return this
    }

    fun setShowEmptyButton(isShowEmptyButton: Boolean): SingleSelectDialog {
        this.isShowEmptyButton = isShowEmptyButton
        return this
    }

    fun setHiddenSearch(isHiddenSearch: Boolean): SingleSelectDialog {
        this.isHiddenSearch = isHiddenSearch
        return this
    }

    fun setCancelButton(isCancel: Boolean): SingleSelectDialog {
        this.isCancel = isCancel
        return this
    }

    fun start(callback: SubmitCallbackListener) {
        this.callback = callback

        hideDialog()
        showDialog()
    }

    private fun hideDialog() {
        if (dialog != null && dialog?.isShowing == true) {
            dialog?.dismiss()
            dialog = null
        }
    }

    private fun showDialog() {
        if (dialog == null) dialog = Dialog(context)
        binding = LytSingleSelectBinding.inflate(LayoutInflater.from(context))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(binding.root)
        dialog?.setCancelable(isCancel)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog?.show()

        binding.tvTitle.text = title
        if (isShowEmptyButton) {
            binding.btnReset.visibility = View.VISIBLE
            binding.btnReset.setOnClickListener {
                callback.onReset()
                hideDialog()
            }
        }

        if (isHiddenSearch) {
            binding.lytSearch.visibility = View.GONE
        }

        if (listItem.isNotEmpty()) {
            val sizeScreen = context.resources.displayMetrics
            val maxHeight = sizeScreen.heightPixels / 2
            val heightSpan = if (listItem.size > 4) maxHeight else ViewGroup.LayoutParams.WRAP_CONTENT

            listItem.mapIndexed { index, domain ->
                domain.index = index
                domain
            }

            binding.recycler.apply {
                val sizeRecycler = this.layoutParams
                sizeRecycler.height = heightSpan
                this.layoutParams = sizeRecycler

                hasFixedSize()
                layoutManager = LinearLayoutManager(binding.root.context)
                _itemAdapter.setItems(listItem)
                adapter = _itemAdapter
            }

            binding.etSearch.doAfterTextChanged { search ->
                listItemSearch.clear()
                if (search.isNullOrBlank()) {
                    listItemSearch.addAll(listItem)
                } else {
                    listItem.mapIndexed { index, it ->
                        if (it.title.lowercase().trim()
                                .contains(search.toString().lowercase().trim())
                            || it.message?.lowercase()?.trim()
                                ?.contains(search.toString().lowercase().trim()) == true
                        ) {
                            it.index = index
                            listItemSearch.add(it)
                        }
                    }
                }

                if (listItemSearch.isNotEmpty()){
                    binding.recycler.visibility = View.VISIBLE
                    binding.lytError.visibility = View.GONE
                    _itemAdapter.setItems(listItemSearch, search.toString().ifEmpty { null })
                }else{
                    binding.recycler.visibility = View.GONE
                    binding.lytError.visibility = View.VISIBLE
                }
            }
        } else {
            callback.onError("Data Not Found")
        }
    }

    private val _itemAdapter by lazy {
        SingleSelectItem(listener = { _, it ->
            callback.onSelected(it)
            hideDialog()
        })
    }

    interface SubmitCallbackListener {
        fun onSelected(item: SingleSelectItemDomain)
        fun onReset()
        fun onError(message: String)
    }

}