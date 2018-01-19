package com.example.buntafujikawa.memoapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import java.util.*

/**
 * SharedPreferencesと、設定値がほしいクラスの橋渡しをする
 */
class SettingPrefUtil {
    companion object {
        // 保存先
        val PREF_FILE_NAME: String = "settings"

        // ファイル名プレイフィックスのkey
        private val KEY_FILE_NAME_PREFIX: String = "file.name.prefix"
        private val KEY_FILE_NAME_PREFIX_DEFAULT: String = "memo"
        private val KEY_SCREEN_REVERSE: String = "screen.reverse"

        fun getFileNamePrefix(context: Context): String {
            // モードは事実上このprivateのみ
            var sp: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

            // 設定値の取得
            return sp.getString(KEY_FILE_NAME_PREFIX, KEY_FILE_NAME_PREFIX_DEFAULT)
        }

        private val KEY_TEXT_SIZE: String = "text.size"
        private val TEXT_SIZE_LARGE: String = "text.size.large"
        private val TEXT_SIZE_MEDIUM: String = "text.size.medium"
        private val TEXT_SIZE_SMALL: String = "text.size.small"

        private val KEY_TEXT_STYLE: String = "text.style"
        private val TEXT_STYLE_BOLD: String = "text.style.bold"
        private val TEXT_STYLE_ITALIC: String = "text.style.italic"
        private val TEXT_STYLE_REVERSE: String = "screen.reverse"

        // フォントの取得
        fun getFontSize(context: Context) {
            var sp: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

            var storedSize: String = sp.getString(KEY_TEXT_SIZE, TEXT_SIZE_MEDIUM)

            when (storedSize) {
                TEXT_SIZE_LARGE -> {
                    context.resources.getDimension(R.dimen.settings_text_size_large)
                }

                TEXT_SIZE_MEDIUM -> {
                    context.resources.getDimension(R.dimen.settings_text_size_medium)
                }

                TEXT_SIZE_SMALL -> {
                    context.resources.getDimension(R.dimen.settings_text_size_small)
                }

                else -> {
                    context.resources.getDimension(R.dimen.settings_text_size_medium)
                }
            }
        }

        // 文字装飾の設定を取得する
        fun getTypeface(context: Context): Int {
            var sp: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            var storedTypeface: Set<String> = sp.getStringSet(KEY_TEXT_STYLE, Collections.emptySet())

            var typefaceBit: Int = Typeface.NORMAL
            storedTypeface.forEach() {
                when (it) {
                    TEXT_STYLE_ITALIC -> {
                        typefaceBit = typefaceBit or Typeface.ITALIC
                    }

                    TEXT_STYLE_BOLD -> {
                        typefaceBit = typefaceBit or Typeface.BOLD
                    }
                }
            }

            return typefaceBit
        }

        fun isScreenReverse(context: Context): Boolean {
            var sp: SharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            return sp.getBoolean(KEY_SCREEN_REVERSE, false)
        }
    }
}
