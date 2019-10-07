package ru.you11.myapplication.list

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list_rv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_cycle_rv.*
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass


class ListRVFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private var isManualScrollToPosition = false

    //used for scrolling position detection
    private var firstElementHalfHeight = 0

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
                val rvRect = Rect()
                list_rv.getWindowVisibleDisplayFrame(rvRect)

                firstElementHalfHeight = list_rv[0].height / 2
            }
        })

        (list_rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        list_rv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val pos = findCurrentPosition() ?: return

                //linear lm not updating adapter so for now this kostyl will have to do
                if (isManualScrollToPosition) {
                    isManualScrollToPosition = false
                    handler.post {
                        adapter.setSelected(pos)
                    }
                } else {
                    adapter.setSelected(pos)
                }
            }
        })

        list_start_moving_button.setOnClickListener {

        }

        list_scroll_up_button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startScroll(true)
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
                    startScroll(false)
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

    private fun startScroll(isScrollUp: Boolean) {

        val runnable = Runnable {

        }

        val adapter = list_rv.adapter as ListRVAdapter

        val pos = if (isScrollUp) {
            adapter.selectedPosition - 1
        } else {
            adapter.selectedPosition + 1
        }

        handler.postDelayed(runnable, 500L)
    }

    private fun findCurrentPosition(): Int? {
        val child = list_rv.findChildViewUnder(0.0f, list_root.height / 2.0f) ?: return null
        return list_rv.getChildAdapterPosition(child)
    }

    private fun getTestData(): ArrayList<RVDataClass> {
        val data = ArrayList<RVDataClass>()
        for (el in 1..100) {
            data.add(RVDataClass((el * 100000).toString()))
        }
        return data
    }
}