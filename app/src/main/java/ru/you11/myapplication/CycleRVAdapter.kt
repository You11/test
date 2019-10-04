package ru.you11.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CycleRVAdapter : RecyclerView.Adapter<SmallRVViewHolder>() {

    var selectedPosition = -1

    val items = ArrayList<RVClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallRVViewHolder {
        return SmallRVViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SmallRVViewHolder, position: Int) {
        holder.bind(getItemAtAdapterPosition(position), selectedPosition == position)
    }

    override fun getItemCount() = 1048576

    fun updateData(newData: List<RVClass>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun getItemAtAdapterPosition(position: Int) = items[position % items.size]

    fun setSelected(position: Int) {
        if (position == selectedPosition) return
        notifyItemChanged(selectedPosition)
        notifyItemChanged(position)
        selectedPosition = position
    }
}