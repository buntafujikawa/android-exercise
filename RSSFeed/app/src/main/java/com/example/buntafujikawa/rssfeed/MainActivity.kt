package com.example.buntafujikawa.rssfeed

import android.app.ProgressDialog
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.example.buntafujikawa.rssfeed.Adapter.FeedAdapter
import com.example.buntafujikawa.rssfeed.Common.HTTPDataHandler
import com.example.buntafujikawa.rssfeed.Model.RSSObject
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val RSS_link = "http://rss.nytimes.com/services/xml/rss/nyt/Science.xml"
    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "NEWS"
        setSupportActionBar(toolbar)

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        val string: String = "1 4"
        val input = Scanner(string)
        input.nextLine().split(" ")

        loadRSS()
    }

    private fun loadRSS() {
        val loadRSSAsync = object : AsyncTask<String, String, String>() {

            // Deprecated since API level 26
            // https://developer.android.com/reference/android/app/ProgressDialog.html
            internal var mDialog = ProgressDialog(this@MainActivity)

            override fun onPreExecute() {
                mDialog.setMessage("Please wait...")
                mDialog.show()
            }

            override fun onPostExecute(result: String?) {
                mDialog.dismiss()

                var rssObject: RSSObject
                rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java!!)

                val adapter = FeedAdapter(rssObject, baseContext)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun doInBackground(vararg params: String): String {
                val result: String
                val http = HTTPDataHandler()

                result = http.GetHTTPDataHandler(params[0])
                return result
            }
        }

        val url_get_data = StringBuilder(RSS_to_JSON_API)
        url_get_data.append(RSS_link)
        loadRSSAsync.execute(url_get_data.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
            loadRSS()
        }
        return true
    }
}
