package com.example.buntafujikawa.memoapp

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class MemoFragment : Fragment() {
    private var mMemoEditText: MemoEditText? = null

    private var mMemoUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View {
        // レイアウトXMLからViewを生成
        val view: View = inflater.inflate(R.layout.fragment_memo, container, false)
        mMemoEditText = view.findViewById(R.id.Memo) as MemoEditText
        return view
    }

    fun reflectSettings() {
        val context: Context? = activity

        context?.let {
            setFontSize(SettingPrefUtil.getFontSize(it))
            setTypeFace(SettingPrefUtil.getTypeface(it))
            setMemoColor(SettingPrefUtil.isScreenReverse(it))
        }
    }

    private fun setFontSize(fontSizePx: Float) {
        mMemoEditText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizePx)
    }

    private fun setTypeFace(typeface: Int) {
        mMemoEditText?.setTypeface(Typeface.DEFAULT, typeface)
    }

    private fun setMemoColor(reverse: Boolean) {
        val backgroundColor: Int = if (reverse) Color.BLACK else Color.WHITE
        val textColor: Int = if (reverse) Color.WHITE else Color.BLACK

        mMemoEditText?.setBackgroundColor(backgroundColor)
        mMemoEditText?.setTextColor(textColor)
    }

    // メモの読み込み、書き込み
    fun save() {
        if (mMemoUri != null) {
            MemoRepository.update(activity, mMemoUri!!, mMemoEditText?.text.toString())
        } else {
            MemoRepository.create(activity, mMemoEditText?.text.toString())
        }

        // A toast is a view containing a quick little message for the user. The toast class helps you create and show those.
        // https://developer.android.com/reference/android/widget/Toast.html
        Toast.makeText(activity, "保存しました", Toast.LENGTH_SHORT).show()
    }

    fun load(uri: Uri) {
        val mMemoUri = uri

        if (uri != null) {
            val memo:String = MemoRepository.findMemoByUri(activity, uri)
            mMemoEditText?.setText(memo)
        } else {
            // メモをクリアする
            mMemoEditText?.setText(null)
        }
    }
}