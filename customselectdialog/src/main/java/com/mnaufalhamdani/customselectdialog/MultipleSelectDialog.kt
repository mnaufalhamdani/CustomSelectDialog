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
import com.mnaufalhamdani.customselectdialog.adapter.MultipleSelectItem
import com.mnaufalhamdani.customselectdialog.databinding.LytMultipleSelectBinding
import com.mnaufalhamdani.customselectdialog.domain.MultipleSelectItemDomain

class MultipleSelectDialog(context: Context) : AppCompatDialog(context) {
    private lateinit var binding: LytMultipleSelectBinding
    private var dialog: Dialog? = null
    private var listItem: MutableList<MultipleSelectItemDomain> = mutableListOf()
    private var listItemSelected: MutableList<String> = mutableListOf()
    private var listItemSearch: MutableList<MultipleSelectItemDomain> = mutableListOf()
    private var title: String = "Choose Multiple Item"
    private var isHiddenSearch: Boolean = false
    private var isCancel: Boolean = true
    private var isHiddenSelectAll: Boolean = false
    private lateinit var callback: SubmitCallbackListener

    fun listItem(listItem: MutableList<MultipleSelectItemDomain>): MultipleSelectDialog {
        this.listItem = listItem
        return this
    }

    fun listItemSelected(listItemSelected: MutableList<String>): MultipleSelectDialog {
        this.listItemSelected = listItemSelected
        return this
    }

    fun setTitle(title: String): MultipleSelectDialog {
        this.title = title
        return this
    }

    fun setHiddenSearch(isHiddenSearch: Boolean): MultipleSelectDialog {
        this.isHiddenSearch = isHiddenSearch
        return this
    }

    fun setCancelButton(isCancel: Boolean): MultipleSelectDialog {
        this.isCancel = isCancel
        return this
    }

    fun setHiddenSelectAll(isHiddenSelectAll: Boolean): MultipleSelectDialog {
        this.isHiddenSelectAll = isHiddenSelectAll
        return this
    }

    fun start(callback: SubmitCallbackListener) {
        this.callback = callback

        hideDialog()
        showDialog()
    }

    private fun hideDialog() {
        if (dialog != null && dialog?.isShowing == true) {
            listItem = mutableListOf()
            dialog?.dismiss()
            dialog = null
        }
    }

    private fun showDialog() {
        if (dialog == null) dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = LytMultipleSelectBinding.inflate(LayoutInflater.from(context))
        dialog?.setContentView(binding.root)
        dialog?.setCancelable(isCancel)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog?.show()

        binding.tvTitle.text = title

        binding.btnReset.setOnClickListener {
            callback.onReset()
            hideDialog()
        }

        binding.btnSave.setOnClickListener {
            val listData = _itemAdapter.getDataChecked()
            if (listData.isEmpty())
                callback.onError("Please Choose Item")
            else {
                var codeOrId = ""
                var title = ""
                listData.map {
                    codeOrId += "${it.codeOrId},"
                    title += "${it.title}, "
                }
                callback.onSelected(
                    listData,
                    codeOrId.substring(0, codeOrId.length - 1).trim(),
                    title.substring(0, title.length - 2)
                )
                hideDialog()
            }
        }

        if (isHiddenSearch) {
            binding.lytSearch.visibility = View.GONE
        }

        if (isHiddenSelectAll) {
            binding.lytSelectAll.visibility = View.GONE
        }

        if (listItem.isNotEmpty()) {
            binding.lytSelectAll.setOnClickListener {
                setCheckBox(!binding.checkBox.isChecked)
                _itemAdapter.updateAllChecked(binding.checkBox.isChecked)
            }

            setUpData()

            binding.etSearch.doAfterTextChanged { search ->
                listItemSearch.clear()
                if (search.isNullOrBlank()) {
                    listItemSearch.addAll(listItem)
                } else {
                    listItem.mapIndexed { _, it ->
                        if (it.title.lowercase().trim()
                                .contains(search.toString().lowercase().trim())
                            || it.message?.lowercase()?.trim()
                                ?.contains(search.toString().lowercase().trim()) == true
                        ) {
                            listItemSearch.add(it)
                        }
                        it
                    }
                }

                _itemAdapter.searchItems(listItemSearch, search.toString().ifEmpty { null })

                if (listItemSearch.isNotEmpty()) {
                    binding.lytSelectAll.visibility = View.VISIBLE
                    binding.recycler.visibility = View.VISIBLE
                    binding.lytError.visibility = View.GONE
                    setCheckBox(!_itemAdapter.getDataShowing().any { !it.isChecked })
                } else {
                    binding.lytSelectAll.visibility = View.GONE
                    binding.recycler.visibility = View.GONE
                    binding.lytError.visibility = View.VISIBLE
                }

            }
        } else {
            callback.onError("Data Not Found")
        }
    }

    private fun setUpData() {
        val sizeScreen = context.resources.displayMetrics
        val maxHeight = sizeScreen.heightPixels / 2
        val heightSpan = if (listItem.size > 4) maxHeight else ViewGroup.LayoutParams.WRAP_CONTENT

        listItem.mapIndexed { index, domain ->
            domain.index = index
            domain.isChecked = false
            listItemSelected.find { selected -> selected == domain.codeOrId }?.let {
                domain.isChecked = true
            }
            domain
        }
        setCheckBox(!listItem.any { !it.isChecked })

        binding.recycler.apply {
            val sizeRecycler = this.layoutParams
            sizeRecycler.height = heightSpan
            this.layoutParams = sizeRecycler

            hasFixedSize()
            layoutManager = LinearLayoutManager(binding.root.context)
            _itemAdapter.setItems(listItem)
            adapter = _itemAdapter
        }
    }

    private fun setCheckBox(isChecked: Boolean) {
        binding.checkBox.isChecked = isChecked
        binding.tvCheckBox.text =
            if (binding.checkBox.isChecked) "Deselect All" else "Select All"
    }

    private val _itemAdapter by lazy {
        MultipleSelectItem(
            listener = { _, it ->
                updateChecked(it)
            }
        )
    }

    private fun updateChecked(domain: MultipleSelectItemDomain) {
        _itemAdapter.updateChecked(domain)
        setCheckBox(!_itemAdapter.getDataShowing().any { !it.isChecked })
    }

    interface SubmitCallbackListener {
        fun onSelected(items: List<MultipleSelectItemDomain>, codeOrId: String, result: String)
        fun onReset()
        fun onError(message: String)
    }

}