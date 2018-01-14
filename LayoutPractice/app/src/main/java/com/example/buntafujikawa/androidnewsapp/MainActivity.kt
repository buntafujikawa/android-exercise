package com.example.buntafujikawa.androidnewsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myListView: ListView = findViewById(R.id.myListView)


        /*
        // データを準備
        val items: ArrayList<String> = ArrayList<String>()
        for (i in 1..30) {
//            items.add("item-" + i)
        }

        // Adapter
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.list_item, items)

        // ListViewに表示
        // データがない場合
        myListView.setEmptyView(findViewById(R.id.emptyView))
        myListView.setAdapter(adapter)
    }
         */

    }
}
