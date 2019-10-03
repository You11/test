package ru.you11.myapplication

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.fragment_cycle_rv.*
import kotlinx.android.synthetic.main.fragment_list_rv.*

class CycleRVFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cycle_rv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CycleRVAdapter()

        cycle_rv.adapter = adapter
        val lm = LinearLayoutManager(activity)
        cycle_rv.layoutManager = lm
        adapter.updateData(getTestData())

        to_list_activity.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, ListRVFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        (cycle_rv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

//        cycle_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val child = cycle_rv.findChildViewUnder(0.0f, cycle_root.height / 2.0f) ?: return
//                val pos = cycle_rv.getChildAdapterPosition(child)
//
//                adapter.setSelected(pos)
//            }
//        })

        cycle_rv.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                cycle_rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        cycle_start_moving_button.setOnClickListener {
            startMoving()
        }
    }

    private fun startMoving() {
        val adapter = cycle_rv.adapter as CycleRVAdapter
        smoothScrollToPositionCenter(adapter.itemCount / 2)
    }

    private fun smoothScrollToPositionCenter(position: Int) {
        cycle_rv.scrollToPosition(12)
        cycle_rv.scrollBy(0, cycle_rv.height / 2 - cycle_rv.computeVerticalScrollRange())
    }

    private fun getTestData(): ArrayList<RVClass> {
        val data = ArrayList<RVClass>()
        for (el in 1..20) {
            data.add(RVClass((el * 100000).toString()))
        }
        return data
    }
}