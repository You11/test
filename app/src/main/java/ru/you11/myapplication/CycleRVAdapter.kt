package ru.you11.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CycleRVAdapter : RecyclerView.Adapter<RVViewHolder>() {

    private var _selectedPosition = 0
    val selectedPosition: Int
        get() = _selectedPosition

    val items = ArrayList<RVClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        return RVViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.bind(getItemAtAdapterPosition(position), _selectedPosition == position)
    }

    override fun getItemCount() = Int.MAX_VALUE

    fun updateData(newData: List<RVClass>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun getItemAtAdapterPosition(position: Int) = items[position % items.size]

    fun setSelected(position: Int) {
        if (position == _selectedPosition) return
        notifyItemChanged(_selectedPosition)
        notifyItemChanged(position)
        _selectedPosition = position
    }
}