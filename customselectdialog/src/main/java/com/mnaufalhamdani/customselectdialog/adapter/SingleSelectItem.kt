package com.mnaufalhamdani.customselectdialog.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mnaufalhamdani.customselectdialog.databinding.ItemSingleSelectBinding
import com.mnaufalhamdani.customselectdialog.domain.SingleSelectItemDomain


@SuppressLint("NotifyDataSetChanged")
class SingleSelectItem(
    val listener: (position: Int, model: SingleSelectItemDomain) -> Unit
) : RecyclerView.Adapter<SingleSelectItem.MyViewHolder>() {

    private val listData: MutableList<SingleSelectItemDomain> = mutableListOf()
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

                    view.visibility = View.VISIBLE
                    if (position == (listData.size - 1)){
                        view.visibility = View.INVISIBLE
                    }
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
        listData.clear()

        listData.addAll(items)
        notifyDataSetChanged()
    }
}