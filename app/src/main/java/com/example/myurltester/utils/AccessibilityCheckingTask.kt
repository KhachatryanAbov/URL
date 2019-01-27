package com.example.myurltester.utils

import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import com.example.myurltester.adapters.ViewHolder
import com.example.myurltester.models.UrlItem
import java.net.HttpURLConnection
import java.net.URL

class AccessibilityCheckingTask(
    private val adapter: RecyclerView.Adapter<ViewHolder>?,
    private val urls: ArrayList<UrlItem>,
    private val mDatabaseHandler  : DatabaseHandler?
) : AsyncTask<String, String, String>() {

    override fun onPreExecute() {//todo
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: String?): String {
        urls.forEach {
            if (!it.isChecked) {
                try {
                    val httpURLConnection = URL(it.path).openConnection() as HttpURLConnection
                    httpURLConnection.readTimeout = 3000
                    httpURLConnection.connectTimeout = 3000//todo ??

                    val timeStart = System.currentTimeMillis()
                    httpURLConnection.connect()
                    it.responseTime = System.currentTimeMillis() - timeStart
                    it.isAccessible = httpURLConnection.responseCode == 200 //todo final
                } catch (ex: Exception) {
                    it.responseTime = 0
                    it.isAccessible = false
                }
                it.isChecked = true
                mDatabaseHandler?.updateUrlItem(it)
                publishProgress("s")
            }
        }
        return "a"
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        adapter?.notifyDataSetChanged()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}