package ru.you11.myapplication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass
import kotlin.random.Random

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
            val rand = Random.nextBoolean()
            if (rand) {
                ListRVContentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false))
            } else {
                ListRVContentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_big_content, parent, false))
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListRVContentViewHolder -> {
                holder.bind(items[position.toCorrectPosition()], _selectedPosition == position)
            }

            is ListRVPaddingViewHolder -> {
                val isTop = (position == 0)
                holder.bind(isTop)
            }
        }
    }

    override fun getItemCount() = items.size + 2

    fun updateData(newData: List<RVDataClass>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun setSelected(newPosition: Int) {
        if (newPosition == _selectedPosition) return
        notifyItemChanged(_selectedPosition)
        notifyItemChanged(newPosition)
        _selectedPosition = newPosition
    }

    private fun Int.toCorrectPosition() = this - 1
}
