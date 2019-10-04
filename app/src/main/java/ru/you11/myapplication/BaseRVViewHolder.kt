package ru.you11.myapplication

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rv.view.*

abstract class BaseRVViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(item: RVClass, isSelected: Boolean = false) {
        containerView.rv_item_name.text = item.name

        if (isSelected) {
            containerView.rv_item_name.setTextColor(Color.RED)
        } else {
            containerView.rv_item_name.setTextColor(Color.GRAY)
        }
    }
}