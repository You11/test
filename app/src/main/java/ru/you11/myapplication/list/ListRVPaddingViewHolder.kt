package ru.you11.myapplication.list

import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_list_rv.view.*
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass

class ListRVPaddingViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(isTop: Boolean) {

        containerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                containerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val height = (containerView.rootView.list_root.height - containerView.resources.getDimension(R.dimen.cycle_rv_item_height).toInt()) / 2 - containerView.rootView.list_some_first_view.height

                val lp = containerView.layoutParams
                lp.height = height
                containerView.layoutParams = lp
            }
        })
    }
}