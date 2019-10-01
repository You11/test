package ru.you11.myapplication

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rv.view.*

class RVViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RVClass, shouldAddTopPadding: Boolean = false, shouldAddBottomPadding: Boolean = false) {
        containerView.rv_item_name.text = item.name
        val topPadding = if (shouldAddTopPadding) 200 else containerView.paddingTop
        val bottomPadding = if (shouldAddBottomPadding) 200 else containerView.paddingBottom

        Log.d("meow", "$adapterPosition")
        Log.d("meow padding", "top = $topPadding, bottom = $bottomPadding")
        Log.d("meow should", "top = $shouldAddTopPadding, bottom = $shouldAddBottomPadding")

        containerView.setPadding(containerView.paddingLeft, topPadding, containerView.paddingRight, bottomPadding)
    }
}