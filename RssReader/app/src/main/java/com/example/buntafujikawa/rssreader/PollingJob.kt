package com.example.buntafujikawa.rssreader

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.AsyncTask
import android.os.Build

import com.example.buntafujikawa.rssreader.data.Site
import com.example.buntafujikawa.rssreader.database.RssRepository
import com.example.buntafujikawa.rssreader.net.HttpGet
import com.example.buntafujikawa.rssreader.parser.RssParser

/**
 * 定期的に起動される、フィードの記事をダウンロードするJob
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class PollingJob : JobService() {

    private lateinit var params: JobParameters

    override fun onStartJob(params: JobParameters): Boolean {
        return false
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }

    private inner class DownloadTask : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit): Unit? {
            val sites: List<Site> = RssRepository.getAllSites(this@PollingJob)

            var newArticles: Int = 0

            sites.forEach() {
                var httpGet: HttpGet = HttpGet(it.url)
                if (!httpGet.get()) return@forEach

                val parser: RssParser = RssParser()
                if (!parser.parse(httpGet.getResponse())) return@forEach

                newArticles += RssRepository.insertLinks(this@PollingJob, it.id, parser.getLinkList())
            }

            if (newArticles > 0) {
                // 新しいリンクがある場合には通知する
                NotificationUtil.notifyUpdate(this@PollingJob, newArticles)
            }

            return null
        }

        override fun onPostExecute(result: Unit) {
            jobFinished(params, false)
        }
    }
}
