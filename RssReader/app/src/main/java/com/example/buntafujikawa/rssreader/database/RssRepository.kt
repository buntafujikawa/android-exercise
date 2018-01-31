package com.example.buntafujikawa.rssreader.database

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase

import com.example.buntafujikawa.rssreader.data.Link
import com.example.buntafujikawa.rssreader.data.Site

import java.util.ArrayList

/**
 * Feedや記事の登録・検索・削除を行う(サンプルプログラムから持ってきた)
 */
object RssRepository {

    fun insertSite(context: Context, site: Site): Long {
        val database = RssDBHelper(context).writableDatabase

        val values = ContentValues()
        values.put(RssDBHelper.Site.TITLE, site.title)
        values.put(RssDBHelper.Site.DESCRIPTION, site.description)
        values.put(RssDBHelper.Site.URL, site.url)

        val id = database.insert(RssDBHelper.Site.TABLE_NAME, null, values)

        database.close()

        return id
    }

    fun deleteSite(context: Context, id: Long): Int {
        val database = RssDBHelper(context).writableDatabase

        val affected = database.delete(
            RssDBHelper.Site.TABLE_NAME,
            RssDBHelper.Site.ID + " = ?",
            arrayOf(id.toString()))

        if (affected > 0) {
            // 記事も削除する
            database.delete(RssDBHelper.Link.TABLE_NAME,
                RssDBHelper.Link.SITE_ID + " = ?",
                arrayOf(id.toString()))
        }

        database.close()

        return affected
    }

    fun getAllSites(context: Context): List<Site> {
        val database = RssDBHelper(context).readableDatabase

        val sites: ArrayList<Site> = ArrayList<Site>()

        val cursor = database.query(RssDBHelper.Site.TABLE_NAME,
            RssDBHelper.Site.PROJECTION, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val feedId: Long = cursor.getLong(cursor.getColumnIndex(RssDBHelper.Site.ID))
            val title: String = cursor.getString(cursor.getColumnIndex(RssDBHelper.Site.TITLE))
            val description: String = cursor.getString(cursor.getColumnIndex(RssDBHelper.Site.DESCRIPTION))
            val url: String = cursor.getString(cursor.getColumnIndex(RssDBHelper.Site.URL))

            // 紐づくリンク情報の件数を取得する
            val linksCount = DatabaseUtils.queryNumEntries(database,
                RssDBHelper.Link.TABLE_NAME, RssDBHelper.Link.SITE_ID + " = ?",
                arrayOf(feedId.toString()))

            val site = Site(feedId, title, description, url, linksCount)

            sites.add(site)
        }

        cursor.close()

        database.close()

        return sites
    }

    fun insertLinks(context: Context, feedId: Long, links: List<Link>): Int {
        val database = RssDBHelper(context).readableDatabase

        var insertedRows: Int = 0

        for (link in links) {
            val values = ContentValues()
            values.put(RssDBHelper.Link.TITLE, link.title)
            values.put(RssDBHelper.Link.DESCRIPTION, link.description)
            values.put(RssDBHelper.Link.PUB_DATE, link.pubDate)
            values.put(RssDBHelper.Link.LINK_URL, link.url)
            values.put(RssDBHelper.Link.SITE_ID, feedId)

            val id = database.insertWithOnConflict(RssDBHelper.Link.TABLE_NAME, null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE)

            if (id >= 0) {
                // insert成功
                link.id
                insertedRows++
            }
        }

        database.close()

        return insertedRows
    }

    fun getAllLinks(context: Context): List<Link> {
        val database = RssDBHelper(context).readableDatabase

        val links = ArrayList<Link>()

        val cursor = database.query(RssDBHelper.Link.TABLE_NAME,
            RssDBHelper.Link.PROJECTION, null, null, null, null,
            RssDBHelper.Link.PUB_DATE + " DESC", null)

        while (cursor.moveToNext()) {
            val id: Long = cursor.getLong(cursor.getColumnIndex(RssDBHelper.Link.ID))
            val title: String = cursor.getString(cursor.getColumnIndex(RssDBHelper.Link.TITLE))
            val description: String = cursor.getString(cursor.getColumnIndex(RssDBHelper.Link.DESCRIPTION))
            val pubDate: Long = cursor.getLong(cursor.getColumnIndex(RssDBHelper.Link.PUB_DATE))
            val url: String = cursor.getString(cursor.getColumnIndex(RssDBHelper.Link.LINK_URL))
            val siteId: Long = cursor.getLong(cursor.getColumnIndex(RssDBHelper.Link.SITE_ID))

            val link = Link(id, title, description, pubDate, url, siteId)
            links.add(link)
        }

        cursor.close()

        database.close()

        return links
    }
}
