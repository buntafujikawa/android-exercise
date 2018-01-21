package com.example.buntafujikawa.memoapp

import android.content.ContentValues
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import java.io.*
import java.util.*

/**
 * メモ管理用のContentProvider
 */
object MemoRepository {
    // prefix-yyyy-mm-dd-HH-MM-SS.txt
    private val MEMO_FILE_FORMAT = "%1\$s-%2\$tF-%2\$tH-%2\$tM-%2\$tS.txt"

    fun create(context: Context, memo: String): Uri? {
        var outputDir: File? = getOutputDir(context)

        outputDir ?: return null

        var outputFile: File? = getFileName(context, outputDir)

        if (outputFile == null || writeToFile(outputFile, memo)) return null

        // タイトルは本文から決定する
        var title: String = if (memo.length > 10) memo.substring(0, 10) else memo

        // DBに登録するその他の情報
        var values: ContentValues = ContentValues()
        values.put(MemoDBHelper.TITLE, title)
        values.put(MemoDBHelper.DATA, outputFile.absolutePath)
        values.put(MemoDBHelper.DATE_ADDED, System.currentTimeMillis())

        // ContentProviderに保存する
        return context.contentResolver.insert(MemoProvider.CONTENT_URI, values)
    }

    // 既存のメモの更新
    fun update(context: Context, uri: Uri, memo: String): Int {
        // 引数のURIを元にDB検索を行う
        var id: String = uri.lastPathSegment
        var cursor: Cursor? = context.contentResolver.query(uri,
            arrayOf(MemoDBHelper.DATA), MemoDBHelper._ID + " = ?", arrayOf(id), null)

        cursor ?: return 0

        var filePath: String? = null

        while (cursor.moveToNext()) {
            filePath = cursor.getString(cursor.getColumnIndex(MemoDBHelper.DATA))
        }

        cursor.close()

        // スマートキャスト
        if (filePath == null || filePath.isEmpty()) return 0

        var outputFile: File = File(filePath)

        if (writeToFile(outputFile, memo)) return 0

        return 1
    }

    // メモの読み込み
    fun findMemoByUri(context: Context, uri: Uri): String {
        var reader: BufferedReader? = null
        var builder: StringBuilder = StringBuilder()

        try {
            // ??
            var inputStream: InputStream? = context.applicationContext.openInputStream(uri)
            inputStream?.let {
                reader = BufferedReader(InputStreamReader(inputStream))

                var line: String
                while ((line = reader.readLine()) != null) {
                    builder.append(line)
                    builder.append("\n")
                }
            }

        } catch (fnfe: FileNotFoundException) {
            // ファイルが削除されるなどして見つからなかった場合
            fnfe.printStackTrace()
            return context.getString(R.string.error_memo_file_not_found)
        } catch (ioe: IOException) {
            // ファイルの読み込みに失敗した場合
            ioe.printStackTrace()
            return context.getString(R.string.error_memo_file_load_failed)
        } finally {
            reader?.let {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return builder.toString()
    }

    fun query(context: Context): Cursor {
        return context.contentResolver.query(
            MemoProvider.CONTENT_URI,
            null, null, null, MemoDBHelper.DATE_MODIFIED + " DESC"
        )
    }

    private fun getOutputDir(context: Context): File? {
        var outputDir: File?

        if (Build.VERSION.SDK_INT >= 19) {
            // ドキュメントファイル用共有ディレクトリ
            // このディレクトリを得るための定数が19以上にしか存在しない
            outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        } else {
            // Documentsディレクトリを作成して保存する
            outputDir = File(context.getExternalFilesDir(null), "Documents")
        }

        // 外部スレージがマウントされていない等
        outputDir ?: return null

        var isExist: Boolean = true
        if (!outputDir.exists() || outputDir.mkdirs()) isExist = outputDir.mkdirs()

        return if (isExist) outputDir else null
    }

    // 出力ファイルの取得
    private fun getFileName(context: Context, outputDir: File): File {
        var fileNamePrefix: String = SettingPrefUtil.getFileNamePrefix(context)

        // 現在日時
        var now: Calendar = Calendar.getInstance()

        // ファイル名の決定
        var fileName: String = String.format(MemoRepository.MEMO_FILE_FORMAT, fileNamePrefix, now)

        return File(outputDir, fileName)
    }

    // ファイルにメモを書き込む
    private fun writeToFile(outputFile: File, memo: String): Boolean {
        var writer: FileWriter? = null

        try {
            writer = FileWriter(outputFile)
            writer.write(memo)
            writer.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            writer?.let {
                try {
                    writer.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return true
    }
}
