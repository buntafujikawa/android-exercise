//package com.example.buntafujikawa.memoapp
//
//import android.content.ContentValues
//import android.content.Context
//import android.net.Uri
//import android.os.Build
//import android.os.Environment
//import java.io.File
//import java.io.FileWriter
//import java.io.IOException
//import java.util.*
//
///**
// *  謎に消えたクラス
// */
//class MemoRepository_archive {
//
//    companion object {
//        // prefix-yyyy-mm-dd-HH-MM-SS.txt
//        private val MEMO_FILE_FORMAT: String = "%1$s-%2$tF-%2$tH-%2$tM-%2$tS.txt"
////        private val MEMO_FILE_FORMAT = "%1\$s-%2\$tF-%2\$tH-%2\$tM-%2\$tS.txt"
//
//        fun store(context: Context, memo: String): Uri? {
//            var outputDir: File
//
//            if (Build.VERSION.SDK_INT >= 19) {
//                // ドキュメントファイル用共有ディレクトリ
//                outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//            } else {
//                // Documentsディレクトリを作成して保存する
//                outputDir = File(context.getExternalFilesDir(null), "Documents")
//            }
//
//            if (outputDir == null) return null
//
//            var hasDirectory: Boolean = true
//            if (!outputDir.exists() || !outputDir.isDirectory) hasDirectory = outputDir.mkdir()
//            if (!hasDirectory) return null
//
//            var outputFile: File = saveAsFile(context, outputDir, memo)
//            if (outputFile == null) return null
//
//            // タイトルは本文から決定する
//            var title: String = if (memo.length > 10) memo.substring(0, 10) else memo
//
//            // DBに保存するためContentValuesに詰める
//            var values: ContentValues = ContentValues()
//            values.put(MemoDBHelper.TITLE, title)
//            values.put(MemoDBHelper.DATA, outputFile.absolutePath)
//            values.put(MemoDBHelper.DATE_ADDED, System.currentTimeMillis())
//
//            // ContentProviderに保存する
//            return context.contentResolver.insert(MemoProvider.CONTENT_URI, values)
//        }
//
//        private fun saveAsFile(context: Context, outputDir: File, memo: String): File? {
//            var fileNamePrefix: String = SettingPrefUtil.getFileNamePrefix(context)
//
//            // 現在日時
//            var now: Calendar = Calendar.getInstance()
//
//            // ファイル名の決定
//            var fileName: String = String.format(MEMO_FILE_FORMAT, fileNamePrefix, now)
//
//            var outputFile: File = File(outputDir, fileName)
//            var writer: FileWriter? = null
//
//            try {
//                writer = FileWriter(outputFile)
//                writer.write(memo)
//                writer.flush()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                return null
//            } finally {
//                if (writer != null) {
//                    try {
//                        writer.close()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            return outputFile
//        }
//    }
//}
