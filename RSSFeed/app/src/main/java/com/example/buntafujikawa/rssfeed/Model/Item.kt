package com.example.buntafujikawa.rssfeed.Model

/**
 * Created by bunta.fujikawa on 2017/12/30.
 */

data class Item(val title: String, val pubDate: String, val link: String, val guid: String, val author: String, val thumbnail: String, val description: String, val content: String, val enclosure: Object, val categories: List<String>)
