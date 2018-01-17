package com.example.buntafujikawa.calculator

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
// android extension
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {

    private var numberInput1: EditText? = null
    private var numberInput2: EditText? = null
    // TODO spinnerがnullになることがあるのかを確認
    private var operatorSelector: Spinner? = null
    private var calcResult: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // リスナークラスの登録
        calcButton1.setOnClickListener(this)
        calcButton2.setOnClickListener(this)
        nextButton.setOnClickListener(this)

        // 文字入力イベントの受け取り
        numberInput1?.addTextChangedListener(this)
        numberInput2?.addTextChangedListener(this)

        // 演算子の選択
        // TODO findViewByIdを使っているところはandroid extensionで省略して書く
        operatorSelector = findViewById(R.id.operatorSelector) as? Spinner

        // 計算結果表示
        calcResult = findViewById(R.id.calcResult) as? TextView
    }

    companion object {
        val REQUEST_ANOTHER_CALC_1: Int = 1
        val REQUEST_ANOTHER_CALC_2: Int = 2
    }

    override fun onClick(v: View) {
        var id: Int = v.id

        when (id) {
            R.id.calcButton1 -> {
                // 上の計算ボタン
                var intent1: Intent = Intent(this, AnotherCalcActivity::class.java)
                // 別のアクティビティの起動 ForResultで遷移先アクティビティから結果を受け取る
                startActivityForResult(intent1, REQUEST_ANOTHER_CALC_1)
            }

            R.id.calcButton2 -> {
                // 下の計算ボタン
                var intent2: Intent = Intent(this, AnotherCalcActivity::class.java)
                startActivityForResult(intent2, REQUEST_ANOTHER_CALC_2)
            }

            R.id.nextButton -> {
                // 続けて計算するボタン
                if (checkEditTextInput()) {
                    var result: Int = calc()

                    numberInput1?.setText(result)

                    refreshResult()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 戻るボタンで戻ってきた場合には何もしない
        if (resultCode != Activity.RESULT_OK) return

        // 怪しいかも
        var resultBundle: Bundle = data?.getExtras()!!

        // 結果に所定のキーが含まれていない場合
        if (!resultBundle.containsKey("result")) return

        var result: Int = resultBundle.getInt("result")

        when (requestCode) {
            REQUEST_ANOTHER_CALC_1 -> {
                numberInput1?.setText(result)
            }

            REQUEST_ANOTHER_CALC_2 -> {
                numberInput2?.setText(result)
            }
        }

        refreshResult()
    }

    private fun checkEditTextInput(): Boolean {
        var input1: String = numberInput1?.getText().toString()
        var input2: String = numberInput2?.getText().toString()

        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2)
    }

    override fun afterTextChanged(s: Editable) {
        refreshResult()
    }

    private fun refreshResult() {
        if (checkEditTextInput()) {
            var result: Int = calc()

            var resultText: String = getString(R.string.calc_result_text, result)
            calcResult?.setText(resultText)
        } else {
            calcResult?.setText(R.string.calc_result_default)
        }
    }

    private fun calc(): Int {

        var input1: String = numberInput1?.getText().toString()
        var input2: String = numberInput2?.getText().toString()

        val number1: Int = Integer.parseInt(input1)
        val number2: Int = Integer.parseInt(input2)

        // TODO operatorを一度からにできるかどうか確認
        val operator: Int = operatorSelector!!.selectedItemPosition

        when (operator) {
            0 -> {
                return number1 + number2
            }

            1 -> {
                return number1 - number2
            }

            2 -> {
                return number1 * number2
            }

            2 -> {
                return number1 / number2
            }

            else -> throw RuntimeException()
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
