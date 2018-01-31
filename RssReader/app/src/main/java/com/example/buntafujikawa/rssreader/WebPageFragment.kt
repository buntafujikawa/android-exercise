package com.example.android.sample.myrssreader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewFragment

/**
 * 記事ページを開くFragment
 */
class WebPageFragment : WebViewFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        val webView = super.onCreateView(inflater, container, savedInstanceState) as WebView

        val args = arguments
        val url = args.getString("url")

        // 指定したURLをロードする
        webView.loadUrl(url)

        return webView
    }

    companion object {

        // このフラグメントのインスタンスを返す
        fun newInstance(url: String): WebPageFragment {
            val fragment = WebPageFragment()

            val args = Bundle()
            args.putString("url", url)
            fragment.arguments = args

            return fragment
        }
    }
}
