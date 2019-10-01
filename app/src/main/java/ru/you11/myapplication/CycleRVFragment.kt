package ru.you11.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_cycle_rv.*

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

        main_rv.adapter = adapter
        val lm = LinearLayoutManager(activity)
        main_rv.layoutManager = lm
        adapter.updateData(getTestData())

        main_rv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstItemPosition = lm.findFirstVisibleItemPosition()
                if (firstItemPosition != 1 && firstItemPosition % adapter.items.size == 1) {
                    lm.scrollToPosition(1)
                } else if (firstItemPosition == 0) {
                    lm.scrollToPositionWithOffset(adapter.items.size, -recyclerView.computeVerticalScrollOffset())
                }
            }
        })

        to_list_activity.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, ListRVFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun getTestData(): ArrayList<RVClass> {
        val data = ArrayList<RVClass>()
        for (el in 1..20) {
            data.add(RVClass((el * 11111).toString()))
        }
        return data
    }
}