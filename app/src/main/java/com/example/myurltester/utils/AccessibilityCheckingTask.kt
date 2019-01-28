package com.example.myurltester.utils

import android.os.AsyncTask
import android.util.Log
import com.example.myurltester.models.UrlItem
import java.net.HttpURLConnection
import java.net.URL

class AccessibilityCheckingTask(
    private val listener: OnAccessibilityCheckingListener?,
    var mode: CheckMode = CheckMode.ALL
) : AsyncTask<Void, UrlItem, Void>() {

    enum class CheckMode {
        ALL, SINGLE,  CONTINUE_AFTER_FINISHING
    }


    override fun doInBackground(vararg values: Void?): Void? {
        listener?.getItems()?.forEach {
            if (!it.isChecked) {
                try {
                    val httpURLConnection = URL(it.path).openConnection() as HttpURLConnection
                    val timeStart = System.currentTimeMillis()
                    httpURLConnection.connectTimeout = 8000//todo make final
                    httpURLConnection.readTimeout= 8000//todo make final
                    httpURLConnection.connect()
                    it.responseTime = System.currentTimeMillis() - timeStart
                    it.isAccessible = httpURLConnection.responseCode == HttpURLConnection.HTTP_OK
                } catch (ex: Exception) {
                    it.responseTime = 0
                    it.isAccessible = false
                }
                it.isChecked = true
                publishProgress(it)
            }
        }
        return null
    }

    override fun onProgressUpdate(vararg values: UrlItem?) {
        super.onProgressUpdate(*values)
        listener?.onStepCompleted(values[0])
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        listener?.onTaskCompleted(mode)
    }

    interface OnAccessibilityCheckingListener {
        fun onStepCompleted(item: UrlItem?)
        fun onTaskCompleted(mode : CheckMode?)
        fun getItems() : ArrayList<UrlItem>?
    }
}