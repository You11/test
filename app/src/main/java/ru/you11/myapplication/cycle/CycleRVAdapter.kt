package ru.you11.myapplication.cycle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass

class CycleRVAdapter : RecyclerView.Adapter<CycleRVViewHolder>() {

    private var _selectedPosition = 0
    val selectedPosition: Int
        get() = _selectedPosition

    val items = ArrayList<RVDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CycleRVViewHolder {
        return CycleRVViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_content,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CycleRVViewHolder, position: Int) {
        holder.bind(getItemAtAdapterPosition(position), _selectedPosition == position)
    }

    override fun getItemCount() = 1048576

    fun updateData(newData: List<RVDataClass>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun getFirstCenterItemPosition() = itemCount / 2 - itemCount / 2 % items.size

    fun getItemAtAdapterPosition(position: Int) = items[position % items.size]

    fun setSelected(position: Int) {
        if (position == _selectedPosition) return
        notifyItemChanged(_selectedPosition)
        notifyItemChanged(position)
        _selectedPosition = position
    }
}