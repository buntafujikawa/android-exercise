package com.example.buntafujikawa.memoapp

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class MemoAdapter(context: Context, c: Cursor, autoRequery: Boolean) : CursorAdapter(context, c, autoRequery) {

    private lateinit var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
    }

    // adapterで使用するviewHolder
    internal class ViewHolder(view: View) {
        var title: TextView
        var lastModified: TextView

        init {
            title = view.findViewById<View>(R.id.ContentText) as TextView
            lastModified = view.findViewById<View>(R.id.UpdateTimestamp) as TextView
        }
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        // リストの行のviewを生成する
        var view: View = mInflater.inflate(R.layout.memo_list_row, null)

        // viewないの各項目への参照を保持するためのviewHolderを作成
        var holder: ViewHolder = ViewHolder(view)

        view.setTag(holder)
        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val title: String = cursor.getString(cursor.getColumnIndex(MemoDBHelper.TITLE))
        val lastModified: String = cursor.getString(cursor.getColumnIndex(MemoDBHelper.DATE_MODIFIED))

        val holder: ViewHolder = view.getTag() as ViewHolder
        holder.title.setText(title)
        holder.lastModified.setText(lastModified)
    }
}
