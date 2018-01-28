package com.example.buntafujikawa.rssreader.parser

import com.example.buntafujikawa.rssreader.data.Link
import com.example.buntafujikawa.rssreader.data.Site

import org.w3c.dom.Document

/**
 * RSS 1.0 / 2.0 / ATOM用パーサーのインターフェイス
 */
interface FeedParser {

    fun parse(document: Document): Boolean

    fun getSite(): Site

    fun getLinkList(): List<Link>
}
