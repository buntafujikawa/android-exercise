package com.example.buntafujikawa.rssreader

import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle

/**
 * 登録サイトを削除するダイアログ
 */

class DeleteSiteDialogFragment : DialogFragment() {

    companion object {
        private const val RESULT_KEY_SITE_ID: String = "site_id"

        // このダイアログフラグメントのインスタンスを生成して返す
        fun newInstance(target: Fragment, requestCode: Int, siteId: Long): DeleteSiteDialogFragment {
            val fragment: DeleteSiteDialogFragment = DeleteSiteDialogFragment()

            val args: Bundle = Bundle()
            args.putLong(RESULT_KEY_SITE_ID, siteId)
            fragment.arguments = args

            fragment.setTargetFragment(target, requestCode)
            return fragment
        }
    }

    // ダイアログを表示する
    override fun onCreateDialog(saveInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        // 表示するダイアログの設定
        builder.setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_message)
            .setPositiveButton(R.string.dialog_button_positive, // 「はい」ボタン
                DialogInterface.OnClickListener() { dialog, id ->
                    val fragment: Fragment? = targetFragment

                    fragment?.let {
                        // 受け取っていた「削除対象のID」を、そのまま返す
                        val args: Bundle = arguments
                        val data: Intent = Intent()
                        data.putExtras(args)

                        // 結果を返す先のフラグメントの、onActivityResult()を呼ぶ
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
