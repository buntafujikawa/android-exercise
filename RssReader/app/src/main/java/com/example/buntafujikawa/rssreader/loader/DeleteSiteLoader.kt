package com.example.buntafujikawa.rssreader.loader

import android.content.AsyncTaskLoader
import android.content.Context

import com.example.buntafujikawa.rssreader.database.RssRepository

class DeleteSiteLoader(context: Context, val id: Long) : AsyncTaskLoader<Int>(context) {

    override fun loadInBackground(): Int {
        return RssRepository.deleteSite(context, id)
    }
}
