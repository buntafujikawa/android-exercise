//package com.example.android.sample.myrssreader.adapter
//
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//
//import com.example.android.sample.myrssreader.R
//import com.example.android.sample.myrssreader.data.Link
//
//import java.util.ArrayList
//
///**
// * 記事一覧用のAdapter
// */
//class LinkAdapter(private val mContext: Context) : RecyclerView.Adapter<*>() {
//    private val mInflater: LayoutInflater
//    private val mLinks: MutableList<Link>
//
//    // リストアイテムがクリックされた時のリスナー
//    private var mListener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//        // リストアイテムがクリックされた
//        fun onItemClick(link: Link)
//    }
//
//    init {
//        mInflater = LayoutInflater.from(mContext)
//        mLinks = ArrayList<Link>()
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        mListener = listener
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
//        if (viewType == VIEW_TYPE_LINK) {
//            val view = mInflater.inflate(R.layout.item_link, parent, false)
//            return LinkViewHolder(view, this)
//        }
//        return null
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is LinkViewHolder) {
//
//            val link = mLinks[position]
//
//            // タイトル
//            holder.title.setText(link.getTitle())
//            // 説明
//            holder.description.setText(link.getDescription())
//            // 発行日
//            holder.timeAgo.text = mContext.getString(R.string.link_publish_date,
//                link.getPubDate())
//        }
//    }
//
//    // データ件数を返す
//    override fun getItemCount(): Int {
//        return mLinks.size
//    }
//
//    fun addItems(links: List<Link>) {
//        mLinks.addAll(links)
//        notifyDataSetChanged()
//    }
//
//    fun removeItem(feedId: Long) {
//        val iterator = mLinks.iterator()
//        while (iterator.hasNext()) {
//            val link = iterator.next()
//            if (link.getSiteId() === feedId) {
//                iterator.remove()
//            }
//        }
//
//        notifyDataSetChanged()
//    }
//
//    fun clearItems() {
//        mLinks.clear()
//        notifyDataSetChanged()
//    }
//
//    // positionに対応するViewTypeを返す
//    override fun getItemViewType(position: Int): Int {
//        return VIEW_TYPE_LINK
//    }
//
//    /**
//     * 記事一覧のアイテム用ViewHolder
//     */
//    private class LinkViewHolder(itemView: View, private val adapter: LinkAdapter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//        private val title: TextView
//        private val description: TextView
//        private val timeAgo: TextView
//
//        init {
//            title = itemView.findViewById(R.id.Title) as TextView
//            description = itemView.findViewById(R.id.Description) as TextView
//            timeAgo = itemView.findViewById(R.id.TimeAgo) as TextView
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View) {
//            if (adapter.mListener != null) {
//                val position = layoutPosition
//                val data = adapter.mLinks[position]
//                adapter.mListener!!.onItemClick(data)
//            }
//        }
//    }
//
//    companion object {
//        // ViewType
//        private val VIEW_TYPE_LINK = 0
//    }
//}
