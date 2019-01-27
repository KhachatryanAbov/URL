package com.example.myurltester.utils

import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import com.example.myurltester.adapters.ViewHolder
import com.example.myurltester.models.UrlItem
import java.net.HttpURLConnection
import java.net.URL

class AccessibilityCheckingTask(
    private val adapter: RecyclerView.Adapter<ViewHolder>?,
    private val urls: ArrayList<UrlItem>
) : AsyncTask<String, String, String>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: String?): String {

        urls.forEach {
            val timeStart = System.currentTimeMillis()
            val path: String = it.path.toString()
            try {
                val url = URL(path)
                val httpURLConnection = url.openConnection() as HttpURLConnection

                //httpURLConnection.readTimeout = 8000
                //httpURLConnection.connectTimeout = 8000
                httpURLConnection.doOutput = true
                httpURLConnection.connect()

                val responseCode: Int = httpURLConnection.responseCode

                it.responseTime = System.currentTimeMillis()-timeStart
                it.isAccessible = responseCode == 200
                it.isChecked = true
                publishProgress("s")

            } catch (ex: Exception) {
                it.isAccessible = false
                it.isChecked = true
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