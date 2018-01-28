package com.example.buntafujikawa.rssreader.loader

import android.content.AsyncTaskLoader
import android.content.Context
import com.example.buntafujikawa.rssreader.data.Link
import com.example.buntafujikawa.rssreader.database.RssRepository

class LinkListLoader(context: Context) : AsyncTaskLoader<List<Link>>(context) {
    override fun loadInBackground(): List<Link> {
        return RssRepository.getAllLinks(context)
    }
}