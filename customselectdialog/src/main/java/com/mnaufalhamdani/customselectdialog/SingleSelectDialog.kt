package com.mnaufalhamdani.customselectdialog

import android.app.Dialog
import android.content.Context
import android.view.View
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
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = LytSingleSelectBinding.inflate(layoutInflater)
        dialog?.setContentView(binding.root)
        dialog?.setCancelable(isCancel)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

        binding.tvTitle.text = title
        if (isShowEmptyButton) {
            binding.viewDividerEmpty.visibility = View.VISIBLE
            binding.tvReset.visibility = View.VISIBLE
            binding.tvReset.setOnClickListener {
                callback.onReset()
                hideDialog()
            }
        }

        if (isHiddenSearch) {
            binding.etSearch.visibility = View.GONE
            binding.viewSearch.visibility = View.GONE
        }

        if (listItem.isNotEmpty()) {
            listItem.mapIndexed { index, domain ->
                domain.index = index
                domain
            }

            binding.recycler.apply {
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
                        if (it.message.lowercase().trim()
                                .contains(search.toString().lowercase().trim())
                        ) {
                            it.index = index
                            listItemSearch.add(it)
                        }
                        it
                    }
                }

                _itemAdapter.setItems(listItemSearch, search.toString().ifEmpty { null })
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