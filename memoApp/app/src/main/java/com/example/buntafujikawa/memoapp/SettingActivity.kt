package com.example.buntafujikawa.memoapp

import android.app.ActionBar
import android.app.Activity
import android.os.Bundle
import android.view.MenuItem

class SettingActivity : Activity(), SettingFragment.SettingFragmentListener {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // アクションバーに戻るボタンを設定
        val actionBar: ActionBar = actionBar
        actionBar?.let {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }

        // Resultはデフォルトでキャンセルを設定
        setResult(RESULT_CANCELED)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // 戻るボタンを押されたら画面を閉じる
            this.finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onSettingChanged() {
        setResult(RESULT_OK)
    }
}
