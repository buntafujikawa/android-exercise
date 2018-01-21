package com.example.buntafujikawa.memoapp

import android.app.ActivityManager
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.ParcelFileDescriptor
import android.text.TextUtils
import java.util.*

/**
 * メモ管理用のContentProvider
 */

class MemoProvider : ContentProvider() {
    companion object {
        private val AUTHORITY: String = "com.example.buntafujikawa.memoapp.memo"

        private val CONTENT_PATH: String = "files"

        val MIME_DIR_PREFIX: String = "vnd.android.cursor.dir/"
        val MIME_ITEM_PREFIX: String = "vnd.android.cursor.item/"

        val MIME_ITEM: String = "vnd.memoapp.memo"
        val MIME_TYPE_MULTIPLE: String = MIME_DIR_PREFIX + MIME_ITEM
        val MIME_TYPE_SINGLE: String = MIME_ITEM_PREFIX + MIME_ITEM

        // このContentProviderがハンドルするURI
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$CONTENT_PATH")

        // メモリストのリクエスト
        private val URI_MATCH_MEMO_LIST: Int = 1
        // 単一のメモのリクエスト
        private val URI_MATCH_MEMO_ITEM: Int = 2

        // URIとの一致をチェックするUriMatcher
        private var sMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            // idが指定されていない場合
            sMatcher.addURI(AUTHORITY, CONTENT_PATH, URI_MATCH_MEMO_LIST)

            // idが指定されている場合
            sMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", URI_MATCH_MEMO_ITEM)
        }
    }

    private var mDatabase: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        val helper: MemoDBHelper = MemoDBHelper(context)
        mDatabase = helper.writableDatabase
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var match: Int = sMatcher.match(uri)
        var cursor: Cursor

        // uriがメモの一覧か個別のメモかをチェック
        when (match) {
            URI_MATCH_MEMO_LIST -> {
                cursor = mDatabase!!.query(MemoDBHelper.TABLE_NAME,
                    projection, selection, selectionArgs, null, null, sortOrder)
            }

            URI_MATCH_MEMO_ITEM -> {
                // IDがURIで指定されている場合
                var id: String = uri.lastPathSegment

                cursor = mDatabase!!.query(MemoDBHelper.TABLE_NAME,
                    projection, MemoDBHelper._ID + "=" + id + if ((TextUtils.isEmpty(selection))) ""
                else
                    " AND ($selection)",
                    selectionArgs, null, null, sortOrder)
            }

            else -> {
                throw IllegalArgumentException("invalid uri:" + uri)
            }
        }


        // 指定したURIへの通知イベントを受信するようにする
        var context: Context = context
        context?.let {
            cursor.setNotificationUri(context!!.contentResolver, uri)
        }

        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {

        // 違うアプリから呼び出された場合
        if (!checkSignaturePermission()) throw SecurityException()
        // 入力値の検証
        if (!validateInput(values)) throw IllegalArgumentException("invalid values")

        var match: Int = sMatcher.match(uri)
        if (match != URI_MATCH_MEMO_LIST) throw IllegalArgumentException("invalid uri: " + uri)

        var id: Long = mDatabase!!.insertOrThrow(MemoDBHelper.TABLE_NAME, null, values)

        when {
            id >= 0 -> {
                var newUri: Uri = Uri.withAppendedPath(CONTENT_URI, id.toString())

                var context: Context = context
                context?.let {
                    context.contentResolver.notifyChange(newUri, null)
                }

                return newUri
            }

            else -> {
                return null
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>): Int {
        if (!checkSignaturePermission()) throw SecurityException()

        var match: Int = sMatcher.match(uri)

        when (match) {
            URI_MATCH_MEMO_LIST -> {
                return mDatabase!!.delete(MemoDBHelper.TABLE_NAME, selection, selectionArgs)
            }

            URI_MATCH_MEMO_ITEM -> {
                var id: String = uri.lastPathSegment

                var affected: Int = mDatabase!!.delete(MemoDBHelper.TABLE_NAME,
                    MemoDBHelper._ID + "=" + id + if (selection!!.isEmpty()) "" else " AND ($selection)",
                    selectionArgs)

                var context: Context = context
                context?.let {
                    context.contentResolver.notifyChange(uri, null)
                }

                return affected
            }

            else -> {
                throw IllegalArgumentException("invalid uri: " + uri)
            }
        }
    }

    override fun update(uri: Uri, values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        if (!checkSignaturePermission()) throw SecurityException()

        if (!validateInput(values)) throw IllegalArgumentException("invalid values")

        var match: Int = sMatcher.match(uri)

        when (match) {
            URI_MATCH_MEMO_LIST -> {
                return mDatabase!!.update(MemoDBHelper.TABLE_NAME, values, selection, selectionArgs)
            }

            URI_MATCH_MEMO_ITEM -> {
                var id: String = uri.lastPathSegment

                var affected: Int = mDatabase!!.update(MemoDBHelper.TABLE_NAME, values,
                    MemoDBHelper._ID + "=" + id + if (selection!!.isEmpty()) "" else " AND ($selection)",
                    selectionArgs)

                var context: Context = context
                context?.let {
                    context.contentResolver.notifyChange(uri, null)
                }

                return affected
            }

            else -> {
                throw IllegalArgumentException("invalid uri: " + uri)
            }
        }
    }

    private fun checkSignaturePermission(): Boolean {
        // 自プロセスのID
        var myPid: Int = android.os.Process.myPid()
        // 呼び出し元のPID
        var callingPid: Int = Binder.getCallingPid()

        if (myPid == callingPid) return true

        var context: Context = context ?: return false

        var packageManager: PackageManager = context.packageManager
        var myPackage: String = context.packageName

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // API levelが19以上じゃないと使えない
            var callingPackage: String = callingPackage

            // 2つのアプリの署名が一致する場合
            return packageManager.checkSignatures(myPackage, callingPackage) == PackageManager.SIGNATURE_MATCH
        }

        var activitymanager: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var callerPackage: HashSet<String> = HashSet()

        // 実行中のプロセス情報の一覧を取得
        var processes: List<ActivityManager.RunningAppProcessInfo> = activitymanager.runningAppProcesses

        processes.forEach() {
            if (it.pid == callingPid) callerPackage.addAll(it.pkgList)
        }

        callerPackage.forEach() {
            if (packageManager.checkSignatures(myPackage, it) == PackageManager.SIGNATURE_MATCH) return true
        }

        return false
    }

    private fun validateInput(values: ContentValues): Boolean {
        // 省略
        return true
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor {
        // 異なる署名のアプリケーションが、書き込み用にファイルを開こうとした場合
        if (mode.isEmpty() && mode.contains("w") && !checkSignaturePermission()) throw SecurityException()

        var match: Int = sMatcher.match(uri)

        if (match == URI_MATCH_MEMO_ITEM) return openFileHelper(uri, mode)

        throw IllegalArgumentException("invalid uri: " + uri)
    }

    override fun getType(uri: Uri): String? {
        val match = sMatcher.match(uri)

        return when (match) {
            URI_MATCH_MEMO_LIST -> MIME_TYPE_MULTIPLE
            URI_MATCH_MEMO_ITEM -> MIME_TYPE_SINGLE
            else -> throw IllegalArgumentException("invalid uri: " + uri)
        }
    }
}
