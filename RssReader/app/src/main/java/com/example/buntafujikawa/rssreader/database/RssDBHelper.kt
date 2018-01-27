package com.example.buntafujikawa.rssreader.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Feedを管理するためのSQLiteOpenHelper(サンプルプログラムから持ってきた)
 */
class RssDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    object Site {
        val TABLE_NAME: String = "SITE"

        val ID: String = "id"
        val TITLE: String = "title"
        val DESCRIPTION: String = "description"
        val URL: String = "url"

        val PROJECTION: Array<String> = arrayOf(ID, TITLE, DESCRIPTION, URL)

        val CREATE_TABLE_SQL: String = ("CREATE TABLE " + TABLE_NAME + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT NOT NULL, "
            + DESCRIPTION + " TEXT, "
            + URL + " TEXT NOT NULL UNIQUE "
            + ")")
    }

    object Link {
        val TABLE_NAME: String = "LINK"

        val ID: String = "id"
        val TITLE: String = "title"
        val DESCRIPTION: String = "description"
        val PUB_DATE: String = "pubDate"
        val LINK_URL: String = "linkUrl"
        val SITE_ID: String = "siteId"
        val REGISTER_TIME: String = "register_time"

        val PROJECTION: Array<String> = arrayOf(ID, TITLE, DESCRIPTION, PUB_DATE, LINK_URL, SITE_ID, REGISTER_TIME)

        val CREATE_TABLE_SQL: String = ("CREATE TABLE " + TABLE_NAME + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT NOT NULL, "
            + DESCRIPTION + " TEXT, "
            + PUB_DATE + " INTEGER, "
            + LINK_URL + " TEXT NOT NULL UNIQUE, "
            + SITE_ID + " INTEGER NOT NULL, "
            + REGISTER_TIME + " TIMESTAMP DEFAULT (DATETIME('now', 'localtime'))"
            + ")")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Site.CREATE_TABLE_SQL)
        db.execSQL(Link.CREATE_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {

        private val DB_NAME: String = "RssDB"
        private val DB_VERSION: Int = 1
    }
}
