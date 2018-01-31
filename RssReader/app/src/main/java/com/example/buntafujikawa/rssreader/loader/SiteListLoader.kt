package com.example.buntafujikawa.rssreader.loader

import android.content.AsyncTaskLoader
import android.content.Context

import com.example.buntafujikawa.rssreader.data.Site
import com.example.buntafujikawa.rssreader.database.RssRepository

class SiteListLoader(context: Context) : AsyncTaskLoader<List<Site>>(context) {

    override fun loadInBackground(): List<Site> {
        // 登録されているサイトを全て取得
        return RssRepository.getAllSites(context)
    }
}
