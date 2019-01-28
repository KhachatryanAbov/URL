package com.example.myurltester.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.myurltester.R
import com.example.myurltester.models.UrlItem
import kotlinx.android.synthetic.main.rv_item_url.view.*

class UrlsAdapter(private val mUrlItems: ArrayList<UrlItem>, private val mContext: Context) : RecyclerView.Adapter<ViewHolder>() {

    private val itemsCopy : ArrayList<UrlItem> = ArrayList()
    var onUrlItemInteractionListener : OnUrlItemInteractionListener? = null

    init {
        itemsCopy.addAll(mUrlItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.rv_item_url,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val urlItem : UrlItem = mUrlItems[position]
        holder.urlPathTv.text = urlItem.path
        holder.accessibilityIv.visibility = if(urlItem.isChecked) View.VISIBLE else View.INVISIBLE
        holder.checkingPb.visibility = if(urlItem.isChecked) View.INVISIBLE else View.VISIBLE
        holder.accessibilityIv.setColorFilter(if(urlItem.isAccessible) Color.GREEN else Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY)
        holder.responseTimeTv.text = mContext.getString(R.string.response_time, urlItem.responseTime.toString())

        holder.deleteIv.setOnClickListener {
            if(urlItem.isChecked) {
                removeItem(urlItem)
                onUrlItemInteractionListener?.onUrlItemDeleted(urlItem)
            }else{
                Toast.makeText(mContext, mContext.getString(R.string.message_url_is_now_being_checked), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mUrlItems.size
    }

    private fun removeItem(urlItem : UrlItem) {
        mUrlItems.remove(urlItem)
        notifyDataSetChanged()
        itemsCopy.remove(urlItem)
    }

    fun filter(text : String?){
        mUrlItems.clear()
        if (text != null) {
            if(text.isEmpty()){
                mUrlItems.addAll(itemsCopy)
            } else{
                itemsCopy.forEach {
                    if(it.path?.toLowerCase()!!.contains(text.toLowerCase())){
                        mUrlItems.add(it)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun addItem(item :UrlItem){
        mUrlItems.add(0,item)
        itemsCopy.add(0,item)
        notifyDataSetChanged()
    }

    fun refreshRv(){
        itemsCopy.clear()
        itemsCopy.addAll(mUrlItems)
        notifyDataSetChanged()
    }

    interface OnUrlItemInteractionListener {
        fun onUrlItemDeleted(urlItem : UrlItem )
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val urlPathTv: TextView = view.tv_url_path
    val checkingPb: ProgressBar = view.pb_url_checker
    val deleteIv: ImageView = view.iv_delete
    val responseTimeTv: TextView = view.tv_response_time
    val accessibilityIv: ImageView = view.iv_accessibility
}