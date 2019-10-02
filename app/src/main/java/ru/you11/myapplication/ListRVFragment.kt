package ru.you11.myapplication

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_list_rv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListRVFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListRVAdapter()
        list_rv.adapter = adapter
        val lm = LinearLayoutManager(activity)
        list_rv.layoutManager = lm

        adapter.updateData(getTestData())

        list_rv.isNestedScrollingEnabled = false

        list_rv.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                list_rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val r = Rect()
                list_rv.getWindowVisibleDisplayFrame(r)

                val lpTop = list_top_space_view.layoutParams
                lpTop.height = (r.height() - list_rv[0].height) / 2 - list_some_first_view.height
                list_top_space_view.layoutParams = lpTop

                val lpBottom = list_bottom_space_view.layoutParams
                lpBottom.height = (r.height() - list_rv[0].height) / 2 - list_some_last_view.height
                list_bottom_space_view.layoutParams = lpBottom
            }
        })

        (list_rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        list_scroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val r = Rect()
            root.getWindowVisibleDisplayFrame(r)
            val childView = list_rv.findChildViewUnder(0.0f, scrollY.toFloat())
            if (childView != null) {
                val pos = list_rv.getChildAdapterPosition(childView)

                if ((scrollY % childView.height + childView.height / 2) > childView.height) {
                    adapter.setSelected(pos + 1)
                } else {
                    adapter.setSelected(pos)
                }
            }
        }

        list_start_moving_button.setOnClickListener {

            GlobalScope.launch(Dispatchers.Main) {
                moveWithDelay(1)
                moveWithDelay(3)
                moveWithDelay(5)
                moveWithDelay(10)
                moveWithDelay(0)
                moveWithDelay(2)
                moveWithDelay(99)
                moveWithDelay(50)
                moveWithDelay(97)
                moveWithDelay(40)
                moveWithDelay(43)
            }
        }
    }

    private suspend fun moveWithDelay(position: Int) {
        delay(500L)
        scrollToCenterPosition(position)
    }

    private fun scrollToCenterPosition(position: Int) {
        val r = Rect()
        root.getWindowVisibleDisplayFrame(r)
        val y = list_rv.y + list_rv.getChildAt(position).y - r.height() / 2 + list_rv.getChildAt(position).height / 2 + list_some_first_view.height
        list_scroll.smoothScrollTo(0, y.toInt())
    }

    private fun getTestData(): ArrayList<RVClass> {
        val data = ArrayList<RVClass>()
        for (el in 1..100) {
            data.add(RVClass((el * 100000).toString()))
        }
        return data
    }
}