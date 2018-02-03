package com.example.buntafujikawa.rssreader

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.customtabs.CustomTabsIntent

import com.example.android.sample.myrssreader.WebPageFragment
import com.example.buntafujikawa.rssreader.data.Link
import com.example.buntafujikawa.rssreader.service.PollingService

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SiteListFragment.SiteListFragmentListener, LinkListFragment.LinkListFragmentListener {

    companion object {
        // 定期フェッチのジョブID
        private const val JOB_FETCH_FEED: Int = 1
        // ジョブの実行間隔(ms)
        private const val INTERVAL: Long = 60L * 60L * 1000L
        // 2ペインの画面かどうか
        var mIsDualPane: Boolean = false
        // ナビゲーションドロワーのトグル
        lateinit var mDrawerToggle: ActionBarDrawerToggle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mIsDualPane = DualPaneContainer != null

        // NavigationDrawerの設定を行う
        val drawerLayout: DrawerLayout = DrawerLayout as DrawerLayout
        mDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name)

        // ドロワーのトグルを有効にする
        mDrawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(mDrawerToggle)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.let {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }

        // 定期実行タスクを設定する
        setRepeatingTask()
    }

    @SuppressLint("NewApi")
    private fun setRepeatingTask() {
        // API Levelが21以上でないとJobSchedulerを使用できない
        if (SDK_INT >= LOLLIPOP) {
            val jobService: ComponentName = ComponentName(this, PollingJob::class.java)

            val fetchJob: JobInfo = JobInfo.Builder(JOB_FETCH_FEED, jobService)
                .setPeriodic(INTERVAL)
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()

            val scheduler: JobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            scheduler.schedule(fetchJob)

        } else {
            val serviceIntent: Intent = Intent(this, PollingService::class.java)

            val isExist: Boolean = PendingIntent.getService(this, JOB_FETCH_FEED, serviceIntent, PendingIntent.FLAG_NO_CREATE) != null
            if (!isExist) {
                val operation: PendingIntent = PendingIntent.getService(this, JOB_FETCH_FEED, serviceIntent, PendingIntent.FLAG_NO_CREATE)
                // AlarmManager
                val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val trigger: Long = SystemClock.elapsedRealtime() + INTERVAL

                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, trigger, INTERVAL, operation)
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // ドロワーのトグルの状態を同期する
        mDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (mDrawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onLinkClicked(link: Link) {
        if (mIsDualPane) {
            val fragment: WebPageFragment = WebPageFragment.newInstance(link.url)
            fragmentManager.beginTransaction().replace(R.id.DetailContainer, fragment).commit()
        } else {
            val colorPrimary: Int = ContextCompat.getColor(this, R.color.colorPrimary)
            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()

            val intent: CustomTabsIntent = builder.setShowTitle(true)
                .setToolbarColor(colorPrimary)
                .build()

            intent.launchUrl(this, Uri.parse(link.url))
        }
    }

    override fun onSiteDeleted(siteId: Long) {
        val fragment: LinkListFragment = fragmentManager.findFragmentById(R.id.MasterContainer) as LinkListFragment
        fragment.removeLinks(siteId)
    }

    override fun onSiteAdded() {
        val fragment = fragmentManager.findFragmentById(R.id.MasterContainer) as LinkListFragment
        fragment.reload()
    }
}
