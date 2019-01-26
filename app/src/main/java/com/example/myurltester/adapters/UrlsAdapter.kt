package com.example.myurltester.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myurltester.R
import com.example.myurltester.ui.models.UrlItem
import kotlinx.android.synthetic.main.rv_item_url.view.*

class UrlsAdapter(val urls: ArrayList<UrlItem>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    var onUrlItemInteractionListener : OnUrlItemInteractionListener? = null;

    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_item_url,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val urlItem : UrlItem = urls.get(position);
        holder.urlPathTv?.text = urlItem.path
        holder.accessibilityIndicator.visibility = if(urlItem.isChecked) View.VISIBLE else View.INVISIBLE
        holder.checkingPb.visibility = if(urlItem.isChecked) View.INVISIBLE else View.VISIBLE
        holder.accessibilityIndicator?.background = ColorDrawable(if(urlItem.isAccessible) Color.RED else Color.GREEN)
        holder.deleteIv.setOnClickListener(View.OnClickListener {
            removeItem(position)
            onUrlItemInteractionListener?.onUrlItemDeleted(urlItem)
        })
    }

    private fun removeItem(position: Int) {
        urls.removeAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, urls.size);
    }

    interface OnUrlItemInteractionListener {
        fun onUrlItemDeleted(urlItem : UrlItem )
    }
}



class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val urlPathTv = view.tv_url_path
    val accessibilityIndicator = view.v_url_accessibility
    val checkingPb = view.pb_url_checker
    val deleteIv = view.iv_delete
}