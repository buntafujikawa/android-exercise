package com.example.buntafujikawa.rssreader.loader

import android.content.AsyncTaskLoader
import android.content.Context
import com.example.buntafujikawa.rssreader.data.Link
import com.example.buntafujikawa.rssreader.data.Site
import com.example.buntafujikawa.rssreader.database.RssRepository
import com.example.buntafujikawa.rssreader.net.HttpGet
import com.example.buntafujikawa.rssreader.parser.RssParser
import java.io.InputStream

/**
 * 記事の一覧を取得するためのLoader
 */

class AddSiteLoader(context: Context, private var url: String) : AsyncTaskLoader<Site>(context) {

    override fun loadInBackground(): Site? {
        if (this.url.isEmpty()) return null

        // RSSフィードをダウンロード
        val httpGet: HttpGet = HttpGet(this.url)
        if (!httpGet.get()) return null

        // レスポンスの解析
        val input: InputStream = httpGet.getResponse()
        val parser: RssParser = RssParser()

        if (!parser.parse(input)) return null

        val site: Site = parser.getSite()
        val links: List<Link> = parser.getLinkList()

        site.url = this.url
        site.linkCount = links.size as Long

        // サイトを登録
        val feedId: Long = RssRepository.insertSite(context, site)
        site.id = feedId

        if (feedId > 0 && links.isNotEmpty()) {
            // 記事の登録
            RssRepository.insertLinks(context, feedId, links)

            return site
        }

        return null
    }
}
