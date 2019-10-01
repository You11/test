package ru.you11.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CycleRVAdapter : RecyclerView.Adapter<RVViewHolder>() {

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
        holder.bind(items[position % items.size])
    }

    override fun getItemCount(): Int = items.size * 2

    fun updateData(newData: List<RVClass>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }
}