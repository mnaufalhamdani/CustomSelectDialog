package com.mnaufalhamdani.customselectdialog.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnaufalhamdani.customselectdialog.databinding.ItemMultipleSelectBinding
import com.mnaufalhamdani.customselectdialog.domain.MultipleSelectItemDomain

@SuppressLint("NotifyDataSetChanged")
class MultipleSelectItem(
    val listener: (position: Int, model: MultipleSelectItemDomain) -> Unit
) : RecyclerView.Adapter<MultipleSelectItem.MyViewHolder>() {

    private val listData: MutableList<MultipleSelectItemDomain> = mutableListOf()//default
    private val listDataShowing: MutableList<MultipleSelectItemDomain> = mutableListOf()//by filter
    private var searchQuery: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listItemBinding = ItemMultipleSelectBinding.inflate(v, parent, false)
        return MyViewHolder(listItemBinding)
    }

    override fun getItemCount(): Int {
        return listDataShowing.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listDataShowing[position], position)
    }

    inner class MyViewHolder(private val viewBinding: ItemMultipleSelectBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(model: MultipleSelectItemDomain, position: Int) {
            try {
                viewBinding.apply {
                    checkBox.isChecked = model.isChecked

                    val title = model.title
                    tvTitle.text = title
//                    if (!searchQuery.isNullOrBlank()) {
//                        title.apply {
//                            val spannable   = SpannableString(this)
//                            val startPos    = this.lowercase().indexOf(searchQuery.toString().lowercase())
//                            val endPos      = startPos + searchQuery.toString().length
//                            val spanColor   = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(root.context, R.color.colorPrimary)))
//                            val highlight   = TextAppearanceSpan(null, Typeface.BOLD, -1, spanColor, null)
//
//                            spannable.setSpan(
//                                highlight,
//                                startPos,
//                                endPos,
//                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                            )
//                            tvTitle.text = spannable
//                        }
//                    } else tvTitle.text = title

                    tvMessage.visibility = View.GONE
                    val message = model.message
                    if (!message.isNullOrBlank()) {
                        tvMessage.visibility = View.VISIBLE
                        tvMessage.text = message

//                        if (!searchQuery.isNullOrBlank()) {
//                            message.apply {
//                                val spannable   = SpannableString(this)
//                                val startPos    = this.lowercase().indexOf(searchQuery.toString().lowercase())
//                                val endPos      = startPos + searchQuery.toString().length
//                                val spanColor   = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(root.context, R.color.colorPrimary)))
//                                val highlight   = TextAppearanceSpan(null, Typeface.BOLD, -1, spanColor, null)
//
//                                spannable.setSpan(
//                                    highlight,
//                                    startPos,
//                                    endPos,
//                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                )
//                                tvMessage.text = spannable
//                            }
//                        } else tvMessage.text = message
                    }

//                    view.visibility = View.VISIBLE
//                    if (position == (listData.size - 1)){
//                        view.visibility = View.INVISIBLE
//                    }
                    item.setOnClickListener {
                        checkBox.isChecked = !checkBox.isChecked
                        model.isChecked = checkBox.isChecked
                        listener.invoke(position, model)
                    }
                }
            }catch (e: Exception){
                Log.e("error:", e.message.toString())
            }
        }
    }

    fun setItems(items: MutableList<MultipleSelectItemDomain>, searchQuery: String? = null) {
        this.searchQuery = searchQuery
        listData.clear()
        listDataShowing.clear()
        listData.addAll(items)
        listDataShowing.addAll(items)
        notifyDataSetChanged()
    }

    fun searchItems(items: MutableList<MultipleSelectItemDomain>, searchQuery: String? = null) {
        this.searchQuery = searchQuery
        items.map { item ->
            listData.find { temp -> item.codeOrId == temp.codeOrId }?.let { domain ->
                item.isChecked = domain.isChecked
                item.timeChecked = System.currentTimeMillis()
            }
            listDataShowing.find { temp -> item.codeOrId == temp.codeOrId }?.let { domain ->
                item.isChecked = domain.isChecked
                item.timeChecked = System.currentTimeMillis()
            }
            item
        }
        listDataShowing.clear()
        listDataShowing.addAll(items)
        notifyDataSetChanged()
    }

    fun updateAllChecked(isChecked: Boolean) {
        listDataShowing.map { data ->
            data.isChecked = isChecked
            data.timeChecked = System.currentTimeMillis()
            data
        }

        listData.map { all ->
            listDataShowing.find { data -> all.codeOrId == data.codeOrId }?.let {
                all.isChecked = it.isChecked
                all.timeChecked = System.currentTimeMillis()
            }
            all
        }

        notifyDataSetChanged()
    }

    fun updateChecked(domain: MultipleSelectItemDomain) {
        listData.map { all ->
            if (all.codeOrId == domain.codeOrId){
                all.isChecked = domain.isChecked
                all.timeChecked = System.currentTimeMillis()
            }
            all
        }

        listDataShowing.map { temp ->
            if (temp.codeOrId == domain.codeOrId){
                temp.isChecked = domain.isChecked
                temp.timeChecked = System.currentTimeMillis()
            }
            temp
        }
    }

    fun getDataChecked(): List<MultipleSelectItemDomain> {
        return listData.filter { it.isChecked }.sortedBy { it.timeChecked }
    }

    fun getDataShowing(): List<MultipleSelectItemDomain> {
        return listDataShowing.toList()
    }
}