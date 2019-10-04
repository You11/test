package ru.you11.myapplication.cycle

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_content.view.*
import ru.you11.myapplication.RVDataClass

class CycleRVViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(item: RVDataClass, isSelected: Boolean = false) {
        containerView.rv_item_name.text = item.name

        if (isSelected) {
            containerView.rv_item_name.setTextColor(Color.RED)
        } else {
            containerView.rv_item_name.setTextColor(Color.GRAY)
        }
    }
}