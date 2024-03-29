package ru.you11.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import ru.you11.myapplication.cycle.CycleRVFragment
import ru.you11.myapplication.list.ListRVFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    var isLongPress = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CycleRVFragment()).commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (event?.keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (!isLongPress) isLongPress = true else return false
                when (val fragment = supportFragmentManager.fragments[0]) {
                    is ListRVFragment -> {
                        fragment.onUpButtonActionDown()
                    }

                    is CycleRVFragment -> {
                        fragment.onUpButtonActionDown()
                    }
                }
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (!isLongPress) isLongPress = true else return false
                when (val fragment = supportFragmentManager.fragments[0]) {
                    is ListRVFragment -> {
                        fragment.onDownButtonActionDown()
                    }

                    is CycleRVFragment -> {
                        fragment.onDownButtonActionDown()
                    }
                }
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (event?.keyCode) {
            KeyEvent.KEYCODE_DPAD_UP,
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                isLongPress = false
                when (val fragment = supportFragmentManager.fragments[0]) {
                    is ListRVFragment -> {
                        fragment.onActionUp()
                    }

                    is CycleRVFragment -> {
                        fragment.onActionUp()
                    }
                }
            }
        }

        return super.onKeyUp(keyCode, event)
    }
}
