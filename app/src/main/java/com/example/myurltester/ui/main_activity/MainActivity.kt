package com.example.myurltester.ui.main_activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.Toast
import com.example.myurltester.R
import com.example.myurltester.adapters.UrlsAdapter
import com.example.myurltester.ui.models.UrlItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UrlsAdapter.OnUrlItemInteractionListener {



    private val mUrlItems: ArrayList<UrlItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addAnimals()
        initUrlInRecyclerView()

        btn_check.setOnClickListener {
            it.hideKeyboard()
            val insertedUrl : String = edt_url_adding.text.toString()
            if(insertedUrl != ""){
                if(!URLUtil.isValidUrl(insertedUrl)){
                    Toast.makeText(this@MainActivity, getString(R.string.invalid_URL), Toast.LENGTH_SHORT).show()
                }
                onNewUrlCreated(UrlItem(insertedUrl))
                edt_url_adding.text?.clear();
            }
        }
    }

    private fun initUrlInRecyclerView() {
        rv_url_list.layoutManager = LinearLayoutManager(this)
        rv_url_list.adapter = UrlsAdapter(mUrlItems, this)
        (rv_url_list.adapter as UrlsAdapter).onUrlItemInteractionListener = this
        rv_url_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun onNewUrlCreated(item: UrlItem) {
        addNewUrlInRecyclerView(item)
        addNewUrlInDB(item)
    }

    private fun addNewUrlInRecyclerView(item: UrlItem) {
        mUrlItems.add(item)
        rv_url_list.adapter?.notifyItemInserted(mUrlItems.size - 1);
        startChecking();
    }

    private fun startChecking() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun addNewUrlInDB(item: UrlItem) {
   //     TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    private fun removeUrlFromDB(item: UrlItem) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun addAnimals() {
        mUrlItems.add(UrlItem("dog", true, false))
        mUrlItems.add(UrlItem("dog", true, true))
        mUrlItems.add(UrlItem("dog", false, false))
        mUrlItems.add(UrlItem("dog", true, true))
        mUrlItems.add(UrlItem("dog", false, false))
        mUrlItems.add(UrlItem("dog", false, false))
        mUrlItems.add(UrlItem("dog", true, true))
        mUrlItems.add(UrlItem("dog", false, false))
        mUrlItems.add(UrlItem("dog", false, true))
        mUrlItems.add(UrlItem("dog", true, false))


    }


    override fun onUrlItemDeleted(urlItem: UrlItem) {
        removeUrlFromDB(urlItem)
    }




}
