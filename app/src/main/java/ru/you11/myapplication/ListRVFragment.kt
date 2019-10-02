package ru.you11.myapplication

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_list_rv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListRVFragment : Fragment() {

    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    private var scrollState = ScrollState.IDLE

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

        list_scroll.setOnScrollChangeListener { v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
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

        list_scroll_up_button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startScrolling(true)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacksAndMessages(null)
                    true
                }

                else -> false
            }
        }

        list_scroll_down_button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startScrolling(false)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacksAndMessages(null)
                    true
                }

                else -> false
            }
        }
    }

    private suspend fun moveWithDelay(position: Int) {
        delay(500L)
        smoothScrollToPositionCenter(position)
    }

    private fun smoothScrollToPositionCenter(position: Int) {
        if (position !in 0 until (list_rv.adapter?.itemCount ?: 0)) return
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

    private fun startScrolling(toTop: Boolean) {
        val adapter = list_rv.adapter as ListRVAdapter

        val maxScrollDistance = 50
        var scrollDistance = 10

        if (toTop) {
            smoothScrollToPositionCenter(adapter.selectedPosition - 1)
        } else {
            smoothScrollToPositionCenter(adapter.selectedPosition + 1)
        }

        scrollState = ScrollState.SHORT

        runnable = Runnable {
            if (toTop) {
                list_scroll.smoothScrollBy(0, -scrollDistance)
            } else {
                list_scroll.smoothScrollBy(0, scrollDistance)
            }
            scrollState = ScrollState.LONG
            if (scrollDistance < maxScrollDistance) scrollDistance += 2
            handler.postDelayed(runnable, 100)
        }

        handler.postDelayed(runnable, 500L)
    }
}