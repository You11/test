package ru.you11.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class ListRVAdapter: RecyclerView.Adapter<RVViewHolder>() {

    private var _selectedPosition = 0
    val selectedPosition: Int
        get() = _selectedPosition

    val items = ArrayList<RVClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {

        val rand = Random.nextBoolean()

        return if (rand) {
            RVViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_rv,
                    parent,
                    false
                ))
        } else {
            RVViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_big_rv,
                    parent,
                    false
                ))
        }
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.bind(items[position], _selectedPosition == position)
    }

    override fun getItemCount() = items.size

    fun updateData(newData: List<RVClass>) {
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