package com.example.myurltester.utils


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class UrlUtil {

    companion object{
        fun isValidUrl(context: Context, url : String):Boolean{
            val connMan : ConnectivityManager  =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager)
            val netInfo : NetworkInfo  = connMan.activeNetworkInfo as (NetworkInfo)
            if (netInfo.isConnected) {
                try {
                    val urlConn : HttpURLConnection  = URL(url).openConnection() as (HttpURLConnection)
                    urlConn.connectTimeout = 3000 //<- 3Seconds Timeout
                    urlConn.connect()
                    return urlConn.responseCode == 200
                    } catch (e1 : MalformedURLException) {
                    return false
                } catch (e : IOException) {
                    return false
                }
            }
            return false
        }
    }
}