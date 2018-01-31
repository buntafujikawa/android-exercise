package com.example.buntafujikawa.rssreader.service

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent

import com.example.buntafujikawa.rssreader.NotificationUtil
import com.example.buntafujikawa.rssreader.data.Site
import com.example.buntafujikawa.rssreader.database.RssRepository
import com.example.buntafujikawa.rssreader.net.HttpGet
import com.example.buntafujikawa.rssreader.parser.RssParser

@SuppressLint("Registered")

/**
 * 定期的に起動される、フィードの記事をダウンロードするService
 * JobSchedulerがAPI Level 21以上しか使えないため、こちらを並行して使用する
 */
class PollingService : IntentService(TAG) {

    companion object {
        private const val TAG: String = "PollingService"
    }

    override fun onHandleIntent(intent: Intent) {
        val sites: List<Site> = RssRepository.getAllSites(this)

        var newArticles: Int = 0

        // RSSフィードをダウンロード
        sites.forEach() {
            val httpGet: HttpGet = HttpGet(it.url)
            if (!httpGet.get()) return@forEach

            val parser: RssParser = RssParser()
            if (!parser.parse(httpGet.getResponse())) return@forEach

            newArticles += RssRepository.insertLinks(this, it.id, parser.getLinkList())
        }

        if (newArticles > 0) {
            // 新しいリンクがある場合には通知する
            NotificationUtil.notifyUpdate(this, newArticles)
        }
    }
}
