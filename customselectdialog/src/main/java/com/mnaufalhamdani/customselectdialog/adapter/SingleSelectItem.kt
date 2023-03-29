package com.mnaufalhamdani.customselectdialog.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mnaufalhamdani.customselectdialog.R
import com.mnaufalhamdani.customselectdialog.databinding.ItemSingleSelectBinding
import com.mnaufalhamdani.customselectdialog.domain.SingleSelectItemDomain

@SuppressLint("NotifyDataSetChanged")
class SingleSelectItem(
    val listener: (position: Int, model: SingleSelectItemDomain) -> Unit
) : RecyclerView.Adapter<SingleSelectItem.MyViewHolder>() {

    private val listData: MutableList<SingleSelectItemDomain> = mutableListOf()
    private val listDataTemp: MutableList<SingleSelectItemDomain> = mutableListOf()
    private var searchQuery: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listItemBinding = ItemSingleSelectBinding.inflate(v, parent, false)
        return MyViewHolder(listItemBinding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    inner class MyViewHolder(private val viewBinding: ItemSingleSelectBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(model: SingleSelectItemDomain, position: Int) {
            try {
                viewBinding.apply {
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

                    item.setOnClickListener {
                        listener.invoke(position, model)
                    }
                }
            }catch (e: Exception){
                Log.e("error:", e.message.toString())
            }
        }
    }

    fun setItems(items: MutableList<SingleSelectItemDomain>, searchQuery: String? = null) {
        this.searchQuery = searchQuery
        listDataTemp.clear()
        listData.clear()

        listDataTemp.addAll(items)
        listData.addAll(listDataTemp)
        notifyDataSetChanged()
    }
}