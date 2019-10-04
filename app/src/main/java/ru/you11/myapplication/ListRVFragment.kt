package ru.you11.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.animation.AccelerateInterpolator
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_list_rv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sqrt
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DefaultItemAnimator



class ListRVFragment : Fragment() {

    //used for scrolling position detection
    private var firstElementHalfHeight = 0

    private val animators = AnimatorSet()

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

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

                val lpTop = list_top_space_view.layoutParams
                lpTop.height = (rvRect.height() - list_rv[0].height) / 2 - list_some_first_view.height
                list_top_space_view.layoutParams = lpTop

                val lpBottom = list_bottom_space_view.layoutParams
                lpBottom.height = (rvRect.height() - list_rv[0].height) / 2 - list_some_last_view.height
                list_bottom_space_view.layoutParams = lpBottom

                animators.interpolator = AccelerateInterpolator()

                for (el in 0..adapter.itemCount) {
                    Log.d("meow", adapter.getItemViewType(el).toString())
                }
            }
        })

        val animator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        animator.supportsChangeAnimations = false
        list_rv.itemAnimator = animator

        list_scroll.setOnScrollChangeListener { v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val childView = list_rv.findChildViewUnder(0.0f, scrollY.toFloat() + firstElementHalfHeight)
            if (childView != null) {
                val pos = list_rv.getChildAdapterPosition(childView)
                adapter.setSelected(pos)
            }
        }

        list_start_moving_button.setOnClickListener {
            startMovingAroundList()
        }

        list_scroll_up_button.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startScroll(true)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacksAndMessages(null)
                    animators.cancel()
                    centerCurrentlySelectedElement()
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
                    animators.cancel()
                    centerCurrentlySelectedElement()
                    true
                }

                else -> false
            }
        }
    }

    private fun startScroll(isScrollUp: Boolean) {
        runnable = Runnable {
            if (isScrollUp) {
                setYAnim(true)
            } else {
                setYAnim(false)
            }

            animators.start()
        }

        val adapter = list_rv.adapter as ListRVAdapter

        val pos = if (isScrollUp) {
            adapter.selectedPosition - 1
        } else {
            adapter.selectedPosition + 1
        }

        smoothScrollToPositionCenter(pos)
        handler.postDelayed(runnable, 500L)
    }

    private fun setYAnim(isUp: Boolean) {
        val scrollValue = if (isUp) 0 else list_rv.height
        animators.duration = calculateDuration(list_scroll.scrollY, scrollValue)
        val yAnim = ObjectAnimator.ofInt(list_scroll, "scrollY", list_scroll.scrollY, scrollValue)
        animators.play(yAnim)
    }

    private fun centerCurrentlySelectedElement() {
        val adapter = list_rv.adapter as ListRVAdapter
        smoothScrollToPositionCenter(adapter.selectedPosition)
    }

    private fun calculateDuration(currentScroll: Int, destination: Int): Long {
        return (sqrt(abs(currentScroll - destination).toDouble()) * 50).toLong()
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
        list_scroll.scrollTo(0, y.toInt())
    }

    private fun getTestData(): ArrayList<RVClass> {
        val data = ArrayList<RVClass>()
        for (el in 1..100) {
            data.add(RVClass((el * 100000).toString()))
        }
        return data
    }

    private fun startMovingAroundList() {
        GlobalScope.launch(Dispatchers.Main) {
            moveWithDelay(1)
        }
    }
}