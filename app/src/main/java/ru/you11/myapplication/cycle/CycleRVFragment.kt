package ru.you11.myapplication.cycle

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_cycle_rv.*
import kotlinx.android.synthetic.main.fragment_list_rv.*
import ru.you11.myapplication.list.ListRVFragment
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass
import ru.you11.myapplication.ScrollState
import kotlin.math.abs
import kotlin.math.sqrt

class CycleRVFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var isManualScrollToPosition = false

    private var scrollState = ScrollState.NONE

    private val yRVCenterCoordinate by lazy { cycle_root.height / 2.0f - cycle_some_first_view.height }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cycle_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()

        to_list_activity.setOnClickListener {
            toNextFragment()
        }

        cycle_scroll_up_button.setOnTouchListener { _, motionEvent ->
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

        cycle_scroll_down_button.setOnTouchListener { _, motionEvent ->
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

        cycle_scroll_to_center_button.setOnClickListener {
            startRVAtCenter()
        }

        cycle_scroll_to_center_first_element_button.setOnClickListener {
            startRVAtCenterFirstElement()
        }
    }

    fun onActionUp() {
        if (scrollState != ScrollState.NONE) handler.removeCallbacksAndMessages(null)
        if (scrollState == ScrollState.LONG) {
            cycle_rv.stopScroll()
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

    private fun setupRV() {
        val adapter = CycleRVAdapter()
        cycle_rv.adapter = adapter
        val lm = LinearLayoutManager(activity)
        cycle_rv.layoutManager = lm

        adapter.updateData(getTestData())

        cycle_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        cycle_rv.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                cycle_rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                startRVAtCenter()
            }
        })

        cycle_rv.itemAnimator = null
    }

    private fun findCurrentPosition(): Int? {
        val child = cycle_rv.findChildViewUnder(0.0f, yRVCenterCoordinate) ?: return null
        return cycle_rv.getChildAdapterPosition(child)
    }

    private fun getTestData(): ArrayList<RVDataClass> {
        val data = ArrayList<RVDataClass>()
        for (el in 1..20) {
            data.add(RVDataClass((el * 100000).toString()))
        }
        return data
    }

    private fun startRVAtCenter() {
        val itemHeight = resources.getDimension(R.dimen.cycle_rv_item_height)
        val centerPosition = (cycle_rv.adapter?.itemCount ?: 0) / 2
        val offset = cycle_root.height / 2 - cycle_some_first_view.height - itemHeight.toInt() / 2
        val lm = cycle_rv.layoutManager as LinearLayoutManager
        isManualScrollToPosition = true
        scrollState = ScrollState.INSTANT
        lm.scrollToPositionWithOffset(centerPosition, offset)
        scrollState = ScrollState.NONE
    }

    private fun startRVAtCenterFirstElement() {
        val itemHeight = resources.getDimension(R.dimen.cycle_rv_item_height)
        val adapter = cycle_rv.adapter as CycleRVAdapter
        val centerPosition = adapter.getFirstCenterItemPosition()
        val offset = cycle_root.height / 2 - cycle_some_first_view.height - itemHeight.toInt() / 2
        val lm = cycle_rv.layoutManager as LinearLayoutManager
        isManualScrollToPosition = true
        scrollState = ScrollState.INSTANT
        lm.scrollToPositionWithOffset(centerPosition, offset)
        scrollState = ScrollState.NONE
    }

    private fun startScroll(isScrollUp: Boolean) {
        if (scrollState == ScrollState.NONE) {
            val maxScrollDistance = 250
            var scrollDistance = 50

            runnable = Runnable {
                scrollState = ScrollState.LONG
                if (isScrollUp) {
                    cycle_rv.smoothScrollBy(0, -scrollDistance)
                } else {
                    cycle_rv.smoothScrollBy(0, scrollDistance)
                }
                if (scrollDistance < maxScrollDistance) scrollDistance += 10
                handler.postDelayed(runnable, 100L)
            }

            smoothScrollAtOnePosition(isScrollUp)

            handler.postDelayed(runnable, 500L)
        }

    }

    private fun smoothScrollAtOnePosition(isScrollUp: Boolean) {
        val scrollYPos = resources.getDimension(R.dimen.cycle_rv_item_height) / 2

        val centerChild = cycle_rv.findChildViewUnder(0.0f, yRVCenterCoordinate) ?: return

        val pos = cycle_rv.getChildAdapterPosition(centerChild) + if (isScrollUp) -1 else 1
        val nextChild = cycle_rv.findViewHolderForAdapterPosition(pos) ?: return
        val scrollDistance = if (isScrollUp)
            centerChild.y - yRVCenterCoordinate - nextChild.itemView.height + scrollYPos
        else
            centerChild.y + centerChild.height - yRVCenterCoordinate + scrollYPos
        scrollState = ScrollState.SHORT
        cycle_rv.smoothScrollBy(0, scrollDistance.toInt())
    }

    private fun scrollToNextElement() {
        val scrollYPos = resources.getDimension(R.dimen.cycle_rv_item_height) / 2

        val centerChild = cycle_rv.findChildViewUnder(0.0f, yRVCenterCoordinate) ?: return

        val scrollDistance = centerChild.y - yRVCenterCoordinate + scrollYPos
        scrollState = ScrollState.SHORT
        cycle_rv.smoothScrollBy(0, scrollDistance.toInt())
    }

    private fun toNextFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.fragment_container,
                ListRVFragment()
            )
            ?.addToBackStack(null)
            ?.commit()
    }
}