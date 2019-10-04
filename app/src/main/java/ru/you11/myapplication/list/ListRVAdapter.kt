package ru.you11.myapplication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass

class ListRVAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _selectedPosition = 0
    val selectedPosition: Int
        get() = _selectedPosition

    private val paddingItemType = 0
    private val contentItemType = 1

    val items = ArrayList<RVDataClass>()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || position == items.size + 1) paddingItemType else contentItemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            ListRVPaddingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_padding, parent, false))
        } else {
            ListRVContentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListRVContentViewHolder)
            holder.bind(items[position], _selectedPosition == position)
    }

    override fun getItemCount() = items.size

    fun updateData(newData: List<RVDataClass>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun setSelected(position: Int) {
        if (position == _selectedPosition) return
        notifyItemChanged(_selectedPosition)
        notifyItemChanged(position)
        _selectedPosition = position
    }
}
