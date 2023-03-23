package com.mnaufalhamdani.customselectdialog.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mnaufalhamdani.customselectdialog.R
import com.mnaufalhamdani.customselectdialog.databinding.ItemMultipleSelectBinding
import com.mnaufalhamdani.customselectdialog.domain.MultipleSelectItemDomain

@SuppressLint("NotifyDataSetChanged")
class MultipleSelectItem(
    val listener: (position: Int, model: MultipleSelectItemDomain) -> Unit
) : RecyclerView.Adapter<MultipleSelectItem.MyViewHolder>() {

    private val listData: MutableList<MultipleSelectItemDomain> = mutableListOf()
    private val listDataTemp: MutableList<MultipleSelectItemDomain> = mutableListOf()
    private var searchQuery: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listItemBinding = ItemMultipleSelectBinding.inflate(v, parent, false)
        return MyViewHolder(listItemBinding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    inner class MyViewHolder(private val viewBinding: ItemMultipleSelectBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(model: MultipleSelectItemDomain, position: Int) {
            viewBinding.apply {
                checkBox.isChecked = model.isChecked

                val message = model.message
                if (!searchQuery.isNullOrBlank()) {
                    val spannable = SpannableString(message)
                    val endLength: Int = message.lowercase()
                        .indexOf(searchQuery.toString()) + searchQuery.toString().length
                    val highlightedColor = ColorStateList(
                        arrayOf(intArrayOf()),
                        intArrayOf(ContextCompat.getColor(this.root.context, R.color.black))
                    )
                    val textAppearanceSpan =
                        TextAppearanceSpan(null, Typeface.NORMAL, -1, highlightedColor, null)
                    spannable.setSpan(
                        textAppearanceSpan,
                        message.lowercase().indexOf(searchQuery.toString()),
                        endLength,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvItem.text = spannable
                } else tvItem.text = message

                //TESTING
                item.setOnClickListener {
                    checkBox.isChecked = !checkBox.isChecked
                    model.isChecked = checkBox.isChecked
                    listener.invoke(position, model)
                }
            }
        }
    }

    fun setItems(items: MutableList<MultipleSelectItemDomain>, searchQuery: String? = null) {
        this.searchQuery = searchQuery

        listDataTemp.clear()
        listData.clear()

        listDataTemp.addAll(items)
        listData.addAll(listDataTemp)
        notifyDataSetChanged()
    }

    fun searchItems(items: MutableList<MultipleSelectItemDomain>, searchQuery: String? = null) {
        this.searchQuery = searchQuery
        items.map { item ->
            listDataTemp.find { temp -> item.codeOrId == temp.codeOrId }?.let { domain ->
                item.isChecked = domain.isChecked
            }
            item
        }
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }

    fun updateAllChecked(isChecked: Boolean) {
        listData.map { data ->
            data.isChecked = isChecked
            data
        }

        listDataTemp.map { temp ->
            listData.find { data -> temp.codeOrId == data.codeOrId }?.let {
                temp.isChecked = it.isChecked
            }
            temp
        }

        notifyDataSetChanged()
    }

    fun updateChecked(domain: MultipleSelectItemDomain) {
        listDataTemp.map { temp ->
            if (temp.codeOrId == domain.codeOrId)
                temp.isChecked = domain.isChecked
            temp
        }
    }

    fun getDataChecked(): List<MultipleSelectItemDomain> {
        return listDataTemp.filter { it.isChecked }
    }

    fun getDataShowing(): List<MultipleSelectItemDomain> {
        return listData.toList()
    }
}