package com.example.buntafujikawa.memoapp

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.widget.EditText
import org.w3c.dom.Attr
import java.nio.file.Path
import java.util.jar.Attributes

/**
 * Created by bunta.fujikawa on 2018/01/18.
 */

public class MemoEditText(context: Context, attrs: Attributes, defStyleAttr: Int) : EditText() {

    companion object {
        private val SOLID: Int = 1
        private val DASH: Int = 2
        private val NORMAL: Int = 4
        private val BOLD: Int = 8
    }

    // viewの横幅
    private var mMeasureWidth: Int = 0

    // 1行の高さ
    private var mLineHeight: Int = 0

    // 表示可能な行数
    private var mDisplayLineCount: Int = 0

    private lateinit var mPath: Path
    private lateinit var mPaint: Paint

    init {
//        mPath = Path()
//        mPaint = Paint()
//        mPaint.style(Paint.Style.STROKE)

        if (attrs != null && !isInEditMode) {
            var lineEffectBit: Int
            var lineColor: Int

            var resources: Resources = context.resources
            var typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MemoEditText)

            try {
                lineEffectBit = typedArray.getInteger(R.styleable.MemoEditText_lineColor, Color.GRAY)
                lineColor = typedArray.getColor(R.styleable.MemoEditText_lineColor, Color.GRAY)
            } finally {
                typedArray.recycle()
            }

            // Javaの&がkotlinのand
            if (lineEffectBit and DASH == DASH) {
                // 破線が設定
                var effect: DashPathEffect = DashPathEffect(floatArrayOf(
                    resources.getDimension(R.dimen.text_rule_interval_on),
                    resources.getDimension(R.dimen.text_rule_interval_off)),
                    0f)
                mPaint.setPathEffect(effect)
            }

            var strokeWidth: Float
            if (lineEffectBit and (BOLD) == BOLD) {
                // 太線が設定
                strokeWidth = resources.getDimension(R.dimen.text_rule_width_bold)
            } else {
                strokeWidth = resources.getDimension(R.dimen.text_rule_width_normal)
            }

            mPaint.setStrokeWidth(strokeWidth)

            mPaint.setColor(lineColor)
        }
    }
}
