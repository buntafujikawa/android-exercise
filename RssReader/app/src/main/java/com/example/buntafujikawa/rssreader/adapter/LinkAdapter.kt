package com.example.buntafujikawa.rssreader.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.buntafujikawa.rssreader.R
import com.example.buntafujikawa.rssreader.data.Link

import kotlinx.android.synthetic.main.item_link.view.*

class LinkAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LINK: Int = 0
    }

    private val mContext: Context = context
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mLinks: MutableList<Link> = mutableListOf()

    // リストアイテムがタップされた時のリスナー
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(link: Link)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return mLinks.size
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_LINK
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == VIEW_TYPE_LINK) {
            val view: View = mInflater.inflate(R.layout.item_link, parent, false)
            return LinkViewHolder(view, this)
        }

        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder !is LinkViewHolder) return

        val articleHolder: LinkViewHolder = holder
        val link: Link = mLinks[position]

        articleHolder.title.text = link.title
        articleHolder.description.text = link.description
        articleHolder.timeAgo.text = mContext.getString(R.string.link_publish_date, link.pubDate)
    }

    fun addItems(links: List<Link>) {
        mLinks.addAll(links)
        notifyDataSetChanged()
    }

    fun removeItem(feedId: Long) {
        val iterator: MutableIterator<Link> = mLinks.iterator()

        while (iterator.hasNext()) {
            val link: Link = iterator.next()
            if (link.siteId == feedId) iterator.remove()
        }

        notifyDataSetChanged()
    }

    fun clearItems() {
        mLinks.clear()
        notifyDataSetChanged()
    }

    internal class LinkViewHolder(itemView: View, adapter: LinkAdapter) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val adapter: LinkAdapter = adapter
        val title: TextView = itemView.Title as TextView
        val description: TextView = itemView.Description as TextView
        val timeAgo: TextView = itemView.TimeAgo as TextView

        override fun onClick(v: View?) {
            val data: Link = adapter.mLinks[layoutPosition]
            adapter.mListener.onItemClick(data)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
