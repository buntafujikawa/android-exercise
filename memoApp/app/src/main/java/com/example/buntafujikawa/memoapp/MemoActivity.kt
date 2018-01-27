package com.example.buntafujikawa.memoapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class MemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        // 指定されたメモのURIを取得する
        val uri = intent.getParcelableExtra<Uri>(BUNDLE_KEY_URI)

        // 指定されたメモを読み込む
        val memoFragment = fragmentManager
            .findFragmentById(R.id.MemoFragment) as MemoFragment
        memoFragment.load(uri)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // メニューを生成する
        menuInflater.inflate(R.menu.menu_memo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivityForResult(intent, REQUEST_SETTING)
                return true
            }

            R.id.action_save -> {
                val memoFragment = fragmentManager.findFragmentById(R.id.MemoFragment) as MemoFragment
                memoFragment.save()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_SETTING && resultCode == Activity.RESULT_OK) {
            // 設定の変更を反映させる
            val memoFragment = fragmentManager
                .findFragmentById(R.id.MemoFragment) as MemoFragment
            memoFragment.reflectSettings()
        }
    }

    companion object {

        val BUNDLE_KEY_URI = "uri"

        private val REQUEST_SETTING = 0
    }
}
