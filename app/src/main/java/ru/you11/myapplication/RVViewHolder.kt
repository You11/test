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
//        containerView.setPadding(0, 0, 0, 0)

        if (shouldAddTopPadding) {
            Log.d("RVVIEWHOLDER", "top, pos = $adapterPosition")
            containerView.setPadding(0, 200, 0, 0)
        }

        if (shouldAddBottomPadding) {
            Log.d("RVVIEWHOLDER", "bottom, pos = $adapterPosition")
            containerView.setPadding(0, 0, 0, 200)
        }
    }
}