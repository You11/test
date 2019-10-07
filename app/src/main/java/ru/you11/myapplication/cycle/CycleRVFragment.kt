package ru.you11.myapplication.cycle

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_cycle_rv.*
import ru.you11.myapplication.list.ListRVFragment
import ru.you11.myapplication.R
import ru.you11.myapplication.RVDataClass
import kotlin.math.abs
import kotlin.math.sqrt

class CycleRVFragment : Fragment() {

    val handler = Handler(Looper.getMainLooper())
    private var isManualScrollToPosition = false

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

        cycle_scroll_down_button.setOnTouchListener { _, motionEvent ->
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

        cycle_scroll_to_center_button.setOnClickListener {
            startRVAtCenter()
        }

        cycle_scroll_to_center_first_element_button.setOnClickListener {

        }
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

        (cycle_rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun findCurrentPosition(): Int? {
        val child = cycle_rv.findChildViewUnder(0.0f, cycle_root.height / 2.0f) ?: return null
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
        val offset = cycle_rv.height / 2 - itemHeight.toInt() / 2
        val lm = cycle_rv.layoutManager as LinearLayoutManager
        isManualScrollToPosition = true
        lm.scrollToPositionWithOffset(centerPosition, offset)
    }

    private fun startRVAtCenterFirstElement() {

    }

    private fun startScroll(isScrollUp: Boolean) {
        val runnable = Runnable {
            if (isScrollUp) {

            } else {

            }
        }

        smoothScrollByOnePosition(isScrollUp)
        handler.postDelayed(runnable, 500L)
    }

    private fun smoothScrollByOnePosition(isUp: Boolean) {
        val r = Rect()
        cycle_root.getWindowVisibleDisplayFrame(r)
        val y = resources.getDimension(R.dimen.cycle_rv_item_height)
        if (isUp) {
            cycle_rv.smoothScrollBy(0, -y.toInt())
        } else {
            cycle_rv.smoothScrollBy(0, y.toInt())
        }
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