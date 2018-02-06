package com.example.buntafujikawa.rssfeed.Model

/**
 * Created by bunta.fujikawa on 2017/12/30.
 */

data class RSSObject(val status: String, val feed: Feed, val items: List<Item>)
