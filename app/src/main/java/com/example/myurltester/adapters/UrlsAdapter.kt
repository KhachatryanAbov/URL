package com.example.myurltester.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myurltester.R
import com.example.myurltester.models.UrlItem
import kotlinx.android.synthetic.main.rv_item_url.view.*

class UrlsAdapter(val urls: ArrayList<UrlItem>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private val itemsCopy : ArrayList<UrlItem> = ArrayList<UrlItem>()
    var onUrlItemInteractionListener : OnUrlItemInteractionListener? = null;

    init {
        itemsCopy.addAll(urls)
    }

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
        holder.accessibilityIv.visibility = if(urlItem.isChecked) View.VISIBLE else View.INVISIBLE
        holder.checkingPb.visibility = if(urlItem.isChecked) View.INVISIBLE else View.VISIBLE
        holder.accessibilityIv.setColorFilter(if(urlItem.isAccessible) Color.GREEN else Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.responseTimeTv.text = urlItem.responseTime.toString()

        holder.deleteIv.setOnClickListener(View.OnClickListener {
            if(urlItem.isChecked) {
                removeItem(urlItem)
                onUrlItemInteractionListener?.onUrlItemDeleted(urlItem)
            }else{
                Toast.makeText(context, context.getString(R.string.message_url_is_now_being_checked), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeItem(urlItem : UrlItem) {
        urls.remove(urlItem);
        notifyDataSetChanged()
        itemsCopy.remove(urlItem);
    }

    fun filter(text : String?){
        urls.clear();
        if (text != null) {
            if(text.isEmpty()){
                urls.addAll(itemsCopy);
            } else{
                itemsCopy.forEach {
                    if(it.path?.toLowerCase()!!.contains(text.toLowerCase())){
                        urls.add(it);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    fun addItem(item :UrlItem){
        urls.add(0,item)
        itemsCopy.add(0,item)
        notifyDataSetChanged()
    }

    fun refreshRv(){
        itemsCopy.clear()
        itemsCopy.addAll(urls)
        notifyDataSetChanged()
    }

    interface OnUrlItemInteractionListener {
        fun onUrlItemDeleted(urlItem : UrlItem )
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val urlPathTv = view.tv_url_path
    val checkingPb = view.pb_url_checker
    val deleteIv = view.iv_delete
    val responseTimeTv = view.tv_response_time
    val accessibilityIv = view.iv_accessibility
}