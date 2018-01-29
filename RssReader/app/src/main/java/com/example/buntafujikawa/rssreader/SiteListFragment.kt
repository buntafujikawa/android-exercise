package com.example.buntafujikawa.rssreader

import android.app.Activity
import android.app.Fragment
import android.app.LoaderManager
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

import com.example.buntafujikawa.rssreader.data.Site
import com.example.buntafujikawa.rssreader.adapter.SiteAdapter
import com.example.buntafujikawa.rssreader.loader.AddSiteLoader
import com.example.buntafujikawa.rssreader.loader.DeleteSiteLoader
import com.example.buntafujikawa.rssreader.loader.SiteListLoader

/**
 * RSS配信サイトの一覧を表示するFragment
 */
class SiteListFragment : Fragment(), LoaderManager.LoaderCallbacks<*>, AdapterView.OnItemClickListener {

    companion object {
        private val LOADER_LOAD_SITES: Int = 1
        private val LOADER_ADD_SITE: Int = 2
        private val LOADER_DELELTE_SITE: Int = 3

        private val REQUEST_ADD_SITE: Int = 1
        private val REQUEST_DELETE_CONFIRM: Int = 2

        private val TAG_DIALOG_FRAGMENT : String = "dialog_fragment"
    }

    private lateinit var mAdapter: SiteAdapter


    // サイトの登録・削除をアクティビティに伝えるリスナー
    interface SiteListFragmentListener {
        fun onSiteDeleted(siteId: Long)
        fun onSiteAdded()
    }

    // Activityがインターフェースを実装しているかチェックする
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is SiteListFragmentListener) {
            throw RuntimeException(context.javaClass.simpleName + " does not implement SiteListFragmentListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle) {
        super.onActivityCreated(savedInstanceState)

        // Loaderを初期化する
        loaderManager.initLoader(LOADER_LOAD_SITES, null, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        loaderManager.destroyLoader(LOADER_LOAD_SITES)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View? {
        // Viewを生成する
        val v = inflater.inflate(R.layout.fragment_sites, container, false)
        val context = inflater.context
        val listView = v.findViewById(R.id.SiteList) as ListView
        val header = inflater.inflate(R.layout.header_add_site, null, false)

        // ヘッダーのクリックイベントを追加
        header.setOnClickListener(View.OnClickListener {
            // ダイアログフラグメントを生成して表示
            val dialog = AddSiteDialogFragment.newInstance(
                this@SiteListFragment, REQUEST_ADD_SITE)
            dialog.show(fragmentManager, TAG_DIALOG_FRAGMENT)
        })

        listView.addHeaderView(header, null, false)

        mAdapter = SiteAdapter(context)
        listView.adapter = mAdapter
        listView.onItemClickListener = this

        return v
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        // ヘッダーもpositionにカウントされるので、1減らす
        val site = mAdapter.getItem(position - 1)

        // 削除を確認するダイアログを表示する
        val dialog = DeleteSiteDialogFragment.newInstance(
            this@SiteListFragment, REQUEST_DELETE_CONFIRM, site.id)
        dialog.show(fragmentManager, TAG_DIALOG_FRAGMENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        // キャンセルされた場合などは何も行わない
        if (resultCode != Activity.RESULT_OK) return

        // RSS配信サイトを追加した場合
        when (requestCode) {
            REQUEST_ADD_SITE -> {
                // RSS配信サイトを追加するLoaderを呼ぶ
                val url = data.getStringExtra("url")
                val args = Bundle()
                args.putString("url", url)

                // すでに初期化済みの場合は再スタート、そうでない場合は初期化
                val loader = loaderManager.getLoader<Int>(LOADER_ADD_SITE)
                if (loader == null) {
                    loaderManager.initLoader(LOADER_ADD_SITE, args, this)
                } else {
                    loaderManager.restartLoader(LOADER_ADD_SITE, args, this)
                }
            }

            REQUEST_DELETE_CONFIRM -> {
                // 配信サイトを登録削除するLoaderを呼ぶ
                val targetFeedId = data.getLongExtra("site_id", -1L)
                val args = Bundle()
                args.putLong("targetId", targetFeedId)

                val loader = loaderManager.getLoader<Int>(LOADER_DELELTE_SITE)
                if (loader == null) {
                    loaderManager.initLoader(LOADER_DELELTE_SITE, args, this)
                } else {
                    loaderManager.restartLoader(LOADER_DELELTE_SITE, args, this)
                }
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<*>? {

        when (id) {
            LOADER_LOAD_SITES -> {
                // 登録済みのRSS配信サイトの一覧を取得する
                val loader = SiteListLoader(activity)
                loader.forceLoad()
                return loader
            }

            LOADER_ADD_SITE -> {
                // RSS配信サイトを登録する
                val url = args.getString("url")
                val loader = AddSiteLoader(activity, url)
                loader.forceLoad()
                return loader
            }

            LOADER_DELELTE_SITE -> {
                // 登録済みのRSS配信サイトを削除する
                val siteId = args.getLong("targetId")
                val loader = DeleteSiteLoader(activity, siteId)
                loader.forceLoad()
                return loader
            }

            else -> return null
        }
    }

    // Loaderの処理が終わった時に呼ばれる
    // TODO 引数の型を確認
    override fun onLoadFinished(loader: Loader<*>, data: Any?) {
        val id = loader.id

        when (id) {
            LOADER_LOAD_SITES -> {
                data?.let { mAdapter.addAll(data as List<Site>) }
            }

            LOADER_ADD_SITE -> {
                // RSS配信サイトを登録した場合、すぐに反映する
                if (data != null) {
                    mAdapter.addItem(data as Site)

                    val listener = activity as SiteListFragmentListener
                    listener.onSiteAdded()
                } else {
                    Toast.makeText(activity, "登録できませんでした", Toast.LENGTH_SHORT).show()
                }
            }

            LOADER_DELELTE_SITE -> {
                // RSS配信サイトを登録削除した場合、すぐに反映する
                val affected = data as Int

                if (affected > 0) {
                    val deleteLoader = loader as DeleteSiteLoader
                    // TODO targetIdが取れなかったので、idにしたから多分違う
                    val targetId = deleteLoader.id

                    mAdapter.removeItem(targetId)

                    val listener = activity as SiteListFragmentListener
                    listener.onSiteDeleted(targetId)
                } else {
                    Toast.makeText(activity, "削除できませんでした", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onLoadFinished(loader: Nothing, data: Nothing?) {

    }

    override fun onLoaderReset(loader: Nothing) {

    }
}
