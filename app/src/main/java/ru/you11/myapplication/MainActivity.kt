package ru.you11.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var isInitScroll = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = RVAdapter()

        rv.adapter = adapter
        val lm = LinearLayoutManager(this)
        rv.layoutManager = lm
        rv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val firstItemPosition = lm.findFirstVisibleItemPosition()
                if (firstItemPosition != 1 && firstItemPosition % adapter.items.size == 1) {
                    lm.scrollToPosition(1)
                } else if (firstItemPosition == 0 && !isInitScroll) {
                    lm.scrollToPositionWithOffset(adapter.items.size, -recyclerView.computeHorizontalScrollOffset())
                }
            }
        })
        adapter.updateData(getTestData())

        rv.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    rv.scrollBy(0, rv.height / 2)
                    isInitScroll = false
                }
            })

//        rv.waitForLayout {
//            rv.scrollBy(0, -rv.height / 2)
//        }
    }

    private fun getTestData(): ArrayList<RVClass> {
        val data = ArrayList<RVClass>()
        for (el in 1..20) {
            data.add(RVClass((el * 11111).toString()))
        }
        return data
    }

    inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
        addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                removeOnGlobalLayoutListener(this)
                f()
            }
        })
    }
}
