package com.example.buntafujikawa.calculator

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_another_calc.*

class AnotherCalcActivity : AppCompatActivity(), View.OnClickListener {

    private var numberInput1: EditText? = null
    private var numberInput2: EditText? = null
    // TODO spinnerがnullになることがあるのかを確認
    private var operatorSelector: Spinner? = null
    private var calcResult: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (!checkEditTextInput()) {
            setResult(Activity.RESULT_CANCELED)
        } else {
            var result: Int = calc()
            var data: Intent = Intent()
            data.putExtra("result", result)
            setResult(Activity.RESULT_OK, data)
        }

        finish()
    }

    private fun checkEditTextInput(): Boolean {
        var input1: String = numberInput1?.getText().toString()
        var input2: String = numberInput2?.getText().toString()

        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2)
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
}
