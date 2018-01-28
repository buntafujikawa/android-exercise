package com.example.buntafujikawa.rssreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.example.buntafujikawa.rssreader.R
import com.example.buntafujikawa.rssreader.data.Site

import kotlinx.android.synthetic.main.item_site.view.*

class SiteAdapter(context: Context) : BaseAdapter() {

    // 非パブリック、非スタティックフィールドの名前は m スタート
    private var mContext: Context = context
    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mSites: MutableList<Site> = mutableListOf()

    fun addItem(site: Site) {
        mSites.add(site)
        notifyDataSetChanged()
    }

    fun addAll(sites: List<Site>) {
        mSites.addAll(sites)
        notifyDataSetChanged()
    }

    fun removeItem(siteId: Long) {
        val size: Int = mSites.size
        val position: Int = -1

        for (i in 0 until size) {
            var site: Site = mSites[i]

            if (siteId == site.id) {
                mSites.removeAt(position)
                notifyDataSetChanged()
                return
            }
        }
    }

    override fun getCount(): Int {
        return mSites.size
    }

    override fun getItem(position: Int): Site {
        return mSites[position]
    }

    override fun getItemId(position: Int): Long {
        return mSites[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: SiteViewHolder
        val itemView: View

        if (convertView == null) {
            itemView = mInflater.inflate(R.layout.item_site, parent, false)
            holder = SiteViewHolder(itemView)
            itemView.setTag(holder)
        } else {
            itemView = convertView
            holder = convertView.tag as SiteViewHolder
        }

        val site: Site = getItem(position)

        holder.title.setText(site.title)
        holder.linksCount.setText(mContext.getString(R.string.site_link_count, site.linkCount))

        return itemView
    }

    // 記事一覧のアイテム用
    internal class SiteViewHolder(itemView: View) {
        val title: TextView = itemView.Title as TextView
        val linksCount: TextView = itemView.ArticlesCount as TextView
    }
}