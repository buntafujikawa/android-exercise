package com.example.buntafujikawa.memoapp

import android.app.FragmentManager
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity(), MemoLoadFragment.MemoLoadFragmentListener,
    SettingFragment.SettingFragmentListener {

    private var isDualpane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isDualpane = (findViewById(R.id.FragmentContainer) != null)

        if (isDualpane) {
            fragmentManager.beginTransaction()
                .replace(R.id.FragmentContainer, MemoLoadFragment())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_memo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId

        when (id) {
            R.id.action_settings -> {
                if (!isDualpane) {
                    // 2ペインでない場合には設定画面を起動する
                    val intent: Intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    return true
                }

                val manager: FragmentManager = fragmentManager
                if (manager.findFragmentById(R.id.FragmentContainer) is SettingFragment) {
                    fragmentManager.beginTransaction()
                        .replace(R.id.FragmentContainer, MemoLoadFragment())
                        .commit()
                } else {
                    fragmentManager.beginTransaction()
                        .replace(R.id.FragmentContainer, SettingFragment())
                        .commit()
                }

                return true
            }

            R.id.action_save -> {
                val memoFragment: MemoFragment = fragmentManager.findFragmentById(R.id.MemoFragment) as MemoFragment
                memoFragment.save()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSettingChanged() {
        val memoFragment: MemoFragment = fragmentManager.findFragmentById(R.id.MemoFragment) as MemoFragment
        memoFragment.reflectSettings()
    }

    override fun onMemoSelected(uri: Uri?) {
        if (isDualpane) {
            val memoFragment: MemoFragment = fragmentManager.findFragmentById(R.id.MemoFragment) as MemoFragment
            memoFragment.load(uri!!)
        } else {
            val intent: Intent = Intent(this, MemoActivity::class.java)
            intent.putExtra(MemoActivity.BUNDLE_KEY_URI, uri)
            startActivity(intent)
        }
    }
}
