package ru.you11.myapplication

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rv.view.*

class RVViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RVClass) {
        containerView.rv_item_name.text = item.name
    }
}