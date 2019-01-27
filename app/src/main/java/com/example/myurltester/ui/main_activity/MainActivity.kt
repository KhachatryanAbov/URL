package com.example.myurltester.ui.main_activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.Toast
import com.example.myurltester.R
import com.example.myurltester.adapters.UrlsAdapter
import com.example.myurltester.models.UrlItem
import com.example.myurltester.utils.AccessibilityCheckingTask
import com.example.myurltester.utils.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UrlsAdapter.OnUrlItemInteractionListener, SearchView.OnQueryTextListener {
//todo check for internet connection

    enum class SortingMode {
        NAME_A, NAME_D, ACCESSIBILITY, RESPONSE_TIME_A, RESPONSE_TIME_D
    }

    private var mDbHandler: DatabaseHandler? = null
    private var mSortingMode : SortingMode = SortingMode.NAME_A
    private val mUrlItems: ArrayList<UrlItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initParams()
        initUrlRecyclerView()
        initViewClicks()
    }

    private fun initParams() {
        mDbHandler = DatabaseHandler(this)
        addAnimals()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem :MenuItem = menu.findItem(R.id.action_search)
        val searchView : SearchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {//todo remove ?????????????
        if(item?.itemId == R.id.action_search){

        }
        return true
    }

    private fun initViewClicks() {
        iv_sort_name_ascending.setOnClickListener {
            mSortingMode = SortingMode.NAME_A
        }

        iv_sort_name_descending.setOnClickListener {
            mSortingMode = SortingMode.NAME_D
        }

        iv_sort_accessibility.setOnClickListener {
            mSortingMode = SortingMode.ACCESSIBILITY
        }

        iv_sort_response_time_ascending.setOnClickListener {
            mSortingMode = SortingMode.RESPONSE_TIME_A
        }

        iv_sort_response_time_descending.setOnClickListener {
            mSortingMode = SortingMode.RESPONSE_TIME_D
        }

        tv_refresh.setOnClickListener {
            AccessibilityCheckingTask(rv_url_list.adapter as UrlsAdapter, mUrlItems).execute()
        }

        btn_check.setOnClickListener {
            it.hideKeyboard()
            val insertedUrl : String = edt_url_adding.text.toString()
            if(insertedUrl != ""){
                if(URLUtil.isValidUrl(insertedUrl)){
                    onNewUrlCreated(UrlItem(System.currentTimeMillis(), insertedUrl))
                    edt_url_adding.text?.clear();
                }else{
                    Toast.makeText(this@MainActivity, getString(R.string.invalid_URL), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initUrlRecyclerView() {
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
        (rv_url_list.adapter as UrlsAdapter).addItem(item)
    }

    private fun addNewUrlInDB(item: UrlItem) {
        mDbHandler?.addUrlItem(item)
    }

    private fun removeUrlFromDB(item: UrlItem) {
        mDbHandler?.deleteURL(item)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun addAnimals() {
        mDbHandler?.getAllURLs()?.let { mUrlItems.addAll(it) }
    }

    override fun onUrlItemDeleted(urlItem: UrlItem) {
        removeUrlFromDB(urlItem)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        (rv_url_list.adapter as UrlsAdapter).filter(query);
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        (rv_url_list.adapter as UrlsAdapter).filter(query);
        return true
    }
}