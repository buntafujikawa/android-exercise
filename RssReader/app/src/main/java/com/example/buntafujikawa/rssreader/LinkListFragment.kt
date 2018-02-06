package com.example.buntafujikawa.rssreader

import android.app.Fragment
import android.app.LoaderManager
import android.content.Context
import android.content.Loader
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.buntafujikawa.rssreader.adapter.LinkAdapter
import com.example.buntafujikawa.rssreader.data.Link
import com.example.buntafujikawa.rssreader.loader.LinkListLoader

import kotlinx.android.synthetic.main.fragment_links.view.*

/**
 * 記事一覧を表示するFragment
 */
class LinkListFragment : Fragment(), LoaderManager.LoaderCallbacks<List<Link>>, LinkAdapter.OnItemClickListener {

    companion object {
        private const val LOADER_LINKS: Int = 1
    }

    // リストがタップされた時のリスナー
    interface LinkListFragmentListener {
        fun onLinkClicked(link: Link)
    }

    private lateinit var mAdapter: LinkAdapter

    // インターフェースを実装しているかチェックする
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is LinkListFragmentListener) {
            throw RuntimeException(context.javaClass.simpleName + " does not implement LinkListFragmentListener")
        }
    }

    // onCreateが完了した後に呼ばれる
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loaderManager.initLoader(LOADER_LINKS, null, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // ビューが破棄されたらloaderも不要なため
        loaderManager.destroyLoader(LOADER_LINKS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val v: View = inflater.inflate(R.layout.fragment_links, container, false)
        val context: Context = inflater.context
        val recyclerView: RecyclerView = v.LinkList as RecyclerView

        // LayoutManagerは必須
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter = LinkAdapter(context)
        mAdapter.setOnItemClickListener(this)
        recyclerView.adapter = mAdapter

        return v
    }

    // リストアイテムがタップされた時のイベント
    override fun onItemClick(link: Link) {
        // activityが取れない場合にnullになる??
        val listener: LinkListFragmentListener = activity as LinkListFragmentListener
        listener.onLinkClicked(link)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<Link>>? {
        if (id == LOADER_LINKS) {
            val loader: LinkListLoader = LinkListLoader(activity)
            loader.forceLoad()
            return loader
        }

        return null
    }

    override fun onLoadFinished(loader: Loader<List<Link>>, data: List<Link>?) {
        val id: Int = loader.id

        if (id == LOADER_LINKS && data != null && data.isNotEmpty()) {
            mAdapter.addItems(data)
        }
    }

    override fun onLoaderReset(loader: Loader<List<Link>>?) {

    }

    // RSS配信サイトが削除された時に、それに紐づく記事も削除する
    fun removeLinks(siteId: Long) {
        mAdapter.removeItem(siteId)
    }

    // RSS配信サイトが追加された時に、同時にそのフィードのリンクもリストに反映する
    fun reload() {
        mAdapter.clearItems()

        val loader: Loader<Int>? = loaderManager.getLoader<Int>(LOADER_LINKS)
        if (loader != null) {
            loader.forceLoad()
        } else {
            loaderManager.restartLoader(LOADER_LINKS, null, this)
        }
    }
}
