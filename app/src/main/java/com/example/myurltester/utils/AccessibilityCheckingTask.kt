package com.example.myurltester.utils

import android.os.AsyncTask
import com.example.myurltester.models.UrlItem
import java.net.HttpURLConnection
import java.net.URL

class AccessibilityCheckingTask(
    private val listener: OnAccessibilityCheckingListener?,
    private var mode: CheckMode = CheckMode.ALL
) : AsyncTask<Void, UrlItem, Void>() {

    enum class CheckMode {
        ALL, SINGLE,  CONTINUE_AFTER_FINISHING
    }

    companion object {
        private const val  CONNECT_TIMEOUT = 800
    }


    override fun doInBackground(vararg values: Void?): Void? {
        listener?.getItems()?.forEach {
            if (!it.isChecked) {
                //TODO add internet checking functionality
                try {
                    val httpURLConnection = URL(it.path).openConnection() as HttpURLConnection
                    val timeStart = System.currentTimeMillis()
                    httpURLConnection.connectTimeout = CONNECT_TIMEOUT
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