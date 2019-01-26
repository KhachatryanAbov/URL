package com.example.myurltester.ui.main_activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myurltester.R
import com.example.myurltester.ui.models.UrlItem
import kotlinx.android.synthetic.main.rv_item_url.view.*

class AnimalAdapter(val urls: ArrayList<UrlItem>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_url, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.urlPathTv?.text = urls.get(position).path
        holder.accessibilityIndicator.visibility = if(urls.get(position).isChecked) View.VISIBLE else View.INVISIBLE
        holder.urlProgress.visibility = if(urls.get(position).isChecked) View.INVISIBLE else View.VISIBLE
        holder.accessibilityIndicator?.background = ColorDrawable(if(urls.get(position).isAccessible) Color.RED else Color.GREEN)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val urlPathTv = view.tv_url_path
    val accessibilityIndicator = view.v_url_accessibility
    val urlProgress = view.pb_url_checker
}