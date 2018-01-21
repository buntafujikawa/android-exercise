package com.example.buntafujikawa.memoapp

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.EditText

/**
 * 罫線を表示するEditText
 */
class MemoEditText(context: Context, attrs: AttributeSet, defStyleAttr: Int) : EditText(context, attrs, defStyleAttr) {

    companion object {
        private val SOLID: Int = 1
        private val DASH: Int = 2
        private val NORMAL: Int = 4
        private val BOLD: Int = 8
    }

    // viewの横幅
    private var mMeasuredWidth: Int = 0

    // 1行の高さ
    private var mLineHeight: Int = 0

    // 表示可能な行数
    private var mDisplayLineCount: Int = 0

    private var mPath: Path? = null
    private var mPaint: Paint? = null

    private fun init(context: Context, attrs: AttributeSet?) {
        mPath = Path()
        mPaint = Paint()
        mPaint!!.style = Paint.Style.STROKE

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
                mPaint!!.setPathEffect(effect)
            }

            var strokeWidth: Float
            if (lineEffectBit and (BOLD) == BOLD) {
                // 太線が設定
                strokeWidth = resources.getDimension(R.dimen.text_rule_width_bold)
            } else {
                strokeWidth = resources.getDimension(R.dimen.text_rule_width_normal)
            }

            mPaint!!.setStrokeWidth(strokeWidth)

            mPaint!!.setColor(lineColor)
        }
    }

    override protected fun onDraw(canvas: Canvas) {
        var paddingTop: Int = extendedPaddingTop
        var scrollY: Int = scrollY
        var firstVisibleLine: Int = layout.getLineForVertical(scrollY)
        var lastVisibleLine: Int = firstVisibleLine + mDisplayLineCount

        mPath!!.reset()

        for (i in firstVisibleLine..lastVisibleLine) {
            mPath!!.moveTo(0f, (i * mLineHeight + paddingTop).toFloat())
            mPath!!.lineTo(mMeasuredWidth.toFloat(), (i * mLineHeight + paddingTop).toFloat())
        }

        canvas.drawPath(mPath!!, mPaint!!)

        super.onDraw(canvas)
    }

    // Viewの横幅と高さを決定する際に呼ばれるメソッド
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mMeasuredWidth = measuredWidth
        var measuredHeight: Int = measuredHeight
        mLineHeight = lineHeight
        mDisplayLineCount = measuredHeight / mLineHeight
    }
}
