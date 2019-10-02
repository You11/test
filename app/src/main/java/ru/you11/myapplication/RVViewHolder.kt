package ru.you11.myapplication

import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_list_rv.view.*
import kotlinx.android.synthetic.main.item_rv.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RVViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: RVClass, isSelected: Boolean = false) {
        containerView.rv_item_name.text = item.name

        if (isSelected) {
            containerView.rv_item_name.setTextColor(Color.RED)
        } else {
            containerView.rv_item_name.setTextColor(Color.GRAY)
        }
    }
}