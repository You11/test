package ru.you11.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListRVAdapter: RecyclerView.Adapter<RVViewHolder>() {

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
        val shouldAddTopPadding = position == 0

        val shouldAddBottomPadding = position == items.size - 1

        holder.bind(items[position], shouldAddTopPadding = shouldAddTopPadding, shouldAddBottomPadding = shouldAddBottomPadding)
    }

    override fun getItemCount() = items.size

    fun updateData(newData: List<RVClass>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }
}