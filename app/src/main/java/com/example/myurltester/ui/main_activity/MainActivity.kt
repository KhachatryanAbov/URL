package com.example.myurltester.ui.main_activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.myurltester.R
import com.example.myurltester.ui.models.UrlItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val animals: ArrayList<UrlItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addAnimals()
        rv_url_list.layoutManager = LinearLayoutManager(this)
        rv_url_list.adapter = AnimalAdapter(animals, this)
    }

    fun addAnimals() {
        animals.add(UrlItem("dog", true, false))
        animals.add(UrlItem("dog", true, true))
        animals.add(UrlItem("dog", false, false))
        animals.add(UrlItem("dog", true, true))
        animals.add(UrlItem("dog", false, false))
        animals.add(UrlItem("dog", false, false))
        animals.add(UrlItem("dog", true, true))
        animals.add(UrlItem("dog", false, false))
        animals.add(UrlItem("dog", false, true))
        animals.add(UrlItem("dog", true, false))


    }
}
