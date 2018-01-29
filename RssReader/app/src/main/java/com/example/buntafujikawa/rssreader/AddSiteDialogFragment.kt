package com.example.buntafujikawa.rssreader

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText

/**
 * URL入力用のFragment
 */
class AddSiteDialogFragment: DialogFragment() {

    private lateinit var mEditText: EditText

    companion object {
        private const val RESULT_KEY_URL: String = "url"

        // このフラグメントのインスタンスを返す
        fun newInstance(target: Fragment, requestCode: Int): AddSiteDialogFragment {
            val fragment: AddSiteDialogFragment = AddSiteDialogFragment()

            // 結果を返す先のフラグメントを設定しておく
            fragment.setTargetFragment(target, requestCode)

            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {

        val context: Context = activity

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val contentView: View = inflater.inflate(R.layout.dialog_input_url, null)

        mEditText = contentView.findViewById(R.id.URLEditText) as EditText

        // ダイアログ生成用のビルダー
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.add_site)
            .setView(contentView)
            .setPositiveButton(R.string.dialog_button_add, // 「はい」ボタン
                DialogInterface.OnClickListener() { dialog, id ->
                    val fragment: Fragment? = targetFragment

                    fragment?.let {
                        val data: Intent = Intent()
                        data.putExtra(RESULT_KEY_URL, mEditText.text.toString())

                        fragment.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
                    }
                })
            .setNegativeButton(R.string.dialog_button_cancel, // 「キャンセル」ボタン
                DialogInterface.OnClickListener() { dialog, id ->
                    val fragment: Fragment? = targetFragment

                    fragment?.let {
                        fragment.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                    }
                })

        // ビルダーからダイアログを生成して返す
        return builder.create()
    }


}
