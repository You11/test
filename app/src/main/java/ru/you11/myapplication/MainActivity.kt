package ru.you11.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.you11.myapplication.cycle.CycleRVFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CycleRVFragment()).commit()
    }
}
