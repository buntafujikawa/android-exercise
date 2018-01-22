package com.example.buntafujikawa.memoapp

import android.app.ListFragment
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView

class MemoLoadFragment : ListFragment() {

    // リストアイテムがタップされたことをアクティビティに伝えるためのリスナー
    interface MemoLoadFragmentListener {
        fun onMemoSelected(uri: Uri?)
    }

    // インターフェースを実装しているかチェック
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is MemoLoadFragmentListener) {
            throw RuntimeException(context.javaClass.simpleName + " does not implement MemoLoadFragmentListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle) {
        super.onActivityCreated(savedInstanceState)

        // ヘッダーの追加
        val header: View = LayoutInflater.from(activity).inflate(R.layout.memo_list_create, null)
        listView.addHeaderView(header)

        // DB検索(本来はメインスレッド上で行わない)
        val cursor: Cursor = MemoRepository.query(activity)

        // アダプターをセット
        val mAdapter: MemoAdapter = MemoAdapter(activity, cursor, true)
        listAdapter = mAdapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        if (position == 0) {
            // ヘッダー
            (activity as MemoLoadFragmentListener).onMemoSelected(null)
        } else {
            val selectedItem: Uri = ContentUris.withAppendedId(MemoProvider.CONTENT_URI, id)
            (activity as MemoLoadFragmentListener).onMemoSelected(selectedItem)
        }
    }
}
