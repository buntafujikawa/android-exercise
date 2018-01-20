package com.example.buntafujikawa.memoapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 保存したメモファイルに関するデータを保存・管理する
 */
class MemoDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private val DB_NAME: String = "memo.db"
        private val DB_VERSION: Int = 1
        val TABLE_NAME: String = "memo"
        val _ID: String = "_id"
        val TITLE: String = "title"
        val DATA: String = "_data"
        val DATE_ADDED: String = "date_added"
        val DATE_MODIFIED: String = "date_modified"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable: String =
            "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                DATA + " TEXT, " +
                DATE_ADDED + " INTEGER NOT NULL, " +
                DATE_MODIFIED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL " +
                ")";
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO バージョン管理
    }
}
