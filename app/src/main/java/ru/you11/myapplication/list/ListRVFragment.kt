package ru.you11.myapplication.list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list_rv.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_cycle_rv.*
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass
import ru.you11.myapplication.ScrollState


class ListRVFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var isManualScrollToPosition = false

    private var scrollState = ScrollState.NONE

    private val yRVCenterCoordinate by lazy { list_root.height / 2.0f - list_some_first_view.height }

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

        list_rv.itemAnimator = null

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
            isManualScrollToPosition = true
            scrollAtPosition(list_position_et.text.toString().toInt())
        }

        list_scroll_up_button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    onUpButtonActionDown()
                    true
                }

                MotionEvent.ACTION_UP -> {
                    onActionUp()
                    true
                }

                else -> false
            }
        }

        list_scroll_down_button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    onDownButtonActionDown()
                    true
                }

                MotionEvent.ACTION_UP -> {
                    onActionUp()
                    true
                }

                else -> false
            }
        }
    }

    private fun startScroll(isScrollUp: Boolean) {
        if (scrollState == ScrollState.NONE) {
            val maxScrollDistance = 200
            var scrollDistance = 40

            runnable = Runnable {
                scrollState = ScrollState.LONG
                if (isScrollUp) {
                    list_rv.smoothScrollBy(0, -scrollDistance)
                } else {
                    list_rv.smoothScrollBy(0, scrollDistance)
                }
                if (scrollDistance < maxScrollDistance) scrollDistance += 8
                handler.postDelayed(runnable, 100L)
            }

            smoothScrollAtOnePosition(isScrollUp)

            handler.postDelayed(runnable, 500L)
        }
    }

    private fun findCurrentPosition(): Int? {
        val child = list_rv.findChildViewUnder(0.0f, yRVCenterCoordinate) ?: return null
        return list_rv.getChildAdapterPosition(child)
    }

    private fun smoothScrollAtOnePosition(isScrollUp: Boolean) {
        val scrollYPos = resources.getDimension(R.dimen.cycle_rv_item_height) / 2

        val centerChild = list_rv.findChildViewUnder(0.0f, yRVCenterCoordinate) ?: return

        val pos = list_rv.getChildAdapterPosition(centerChild) + if (isScrollUp) -1 else 1
        val nextChild = list_rv.findViewHolderForAdapterPosition(pos) ?: return
        val scrollDistance = if (isScrollUp)
            centerChild.y - yRVCenterCoordinate - nextChild.itemView.height + scrollYPos
        else
            centerChild.y + centerChild.height - yRVCenterCoordinate + scrollYPos
        scrollState = ScrollState.SHORT
        list_rv.smoothScrollBy(0, scrollDistance.toInt())
    }

    private fun scrollAtPosition(position: Int) {
        val itemHeight = resources.getDimension(R.dimen.cycle_rv_item_height)
        val offset = list_root.height / 2 - list_some_first_view.height - itemHeight.toInt() / 2
        val lm = list_rv.layoutManager as LinearLayoutManager
        scrollState = ScrollState.INSTANT
        lm.scrollToPositionWithOffset(position, offset)
    }

    private fun scrollToNextElement() {
        val scrollYPos = resources.getDimension(R.dimen.cycle_rv_item_height) / 2

        val centerChild = list_rv.findChildViewUnder(0.0f, yRVCenterCoordinate) ?: return

        val scrollDistance = centerChild.y - yRVCenterCoordinate + scrollYPos
        scrollState = ScrollState.SHORT
        list_rv.smoothScrollBy(0, scrollDistance.toInt())
    }

    fun onActionUp() {
        if (scrollState != ScrollState.NONE) handler.removeCallbacksAndMessages(null)
        if (scrollState == ScrollState.LONG) {
            list_rv.stopScroll()
            scrollToNextElement()
        }

        scrollState = ScrollState.NONE
    }

    fun onUpButtonActionDown() {
        startScroll(true)
    }

    fun onDownButtonActionDown() {
        startScroll(false)
    }

    private fun getTestData(): ArrayList<RVDataClass> {
        val data = ArrayList<RVDataClass>()
        for (el in 1..100) {
            data.add(RVDataClass((el * 100000).toString()))
        }
        return data
    }
}
