package com.example.buntafujikawa.memoapp

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.text.TextUtils

class SettingFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    interface SettingFragmentListener {
        fun onSettingChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ファイル名を指定
        preferenceManager.sharedPreferencesName = SettingPrefUtil.PREF_FILE_NAME

        // 設定ファイルを指定
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle) {
        super.onActivityCreated(savedInstanceState)

        setTypefaceSummary(preferenceManager.sharedPreferences)
        setPrefixSummary(preferenceManager.sharedPreferences)
    }

    override fun onResume() {
        super.onResume()

        // 変更を監視するリスナーを登録
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        // リスナーの登録を解除
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    // SharedPreferencesの値が変更された場合に呼び出される
    override fun onSharedPreferenceChanged(prefs: SharedPreferences, key: String) {
        val activity: Activity = activity

        // SettingFragmentListenerを実装していれば通知する
        if (activity is SettingFragmentListener) {
            val listener = activity as SettingFragmentListener

            // Activityに変更通知
            listener.onSettingChanged()
        }

        // サマリーに反映
        if (activity.getString(R.string.key_text_style).equals(key)) {
            setTypefaceSummary(prefs)
        } else if (activity.getString(R.string.key_file_name_prefix).equals(key)) {
            setPrefixSummary(prefs)
        }
    }

    private fun setTypefaceSummary(prefs: SharedPreferences) {
        val key: String = activity.getString(R.string.key_text_style)
        val preference: Preference = findPreference(key)
        // 現在選択されている値を取得
        val selected: Set<String> = prefs.getStringSet(key, emptySet<String>())
        preference.summary = TextUtils.join("/", selected.toTypedArray())
    }

    // プレフィックスのサマリーを更新
    private fun setPrefixSummary(prefs: SharedPreferences) {
        val key: String = activity.getString(R.string.key_file_name_prefix)
        val preference: Preference = findPreference(key)
        val prefix: String = prefs.getString(key, "")
        preference.summary = prefix
    }
}
