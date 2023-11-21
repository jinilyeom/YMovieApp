package com.ymovie.app.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.ymovie.app.R
import com.ymovie.app.util.convertDpToPx

class CircularProgress(context: Context, attr: AttributeSet) : View(context, attr) {
    private val labelTextSize = convertDpToPx(10F, context.resources)
    private val indicatorStroke = convertDpToPx(2.5F, context.resources)

    private var progress: Float = DEFAULT_PROGRESS
    private var labelText: String = DEFAULT_LABEL_TEXT

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.user_score_background)
    }

    private val trackPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.user_score_track)
        style = Paint.Style.STROKE
        strokeWidth = indicatorStroke.toFloat()
    }

    private val indicatorPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.user_score_indicator)
        style = Paint.Style.STROKE
        strokeWidth = indicatorStroke.toFloat()
    }

    private val labelTextPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.white)
        textAlign = Paint.Align.CENTER
        textSize = labelTextSize.toFloat()
        typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }

    private val indicatorRect = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        indicatorRect.set(
            (indicatorStroke / 2F) + INDICATOR_OFFSET_FROM_BACKGROUND,
            (indicatorStroke / 2F) + INDICATOR_OFFSET_FROM_BACKGROUND,
            (width.toFloat() - (indicatorStroke / 2F)) - INDICATOR_OFFSET_FROM_BACKGROUND,
            (height.toFloat() - (indicatorStroke / 2F)) - INDICATOR_OFFSET_FROM_BACKGROUND
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.drawCircle(width / 2F, height / 2F, width / 2F, backgroundPaint)
            it.drawArc(indicatorRect, 0F, 360F, false, trackPaint)
            it.drawArc(indicatorRect, 270F, progress, false, indicatorPaint)
            it.drawText(
                labelText,
                width / 2F,
                (height / 2F) - ((labelTextPaint.descent() + labelTextPaint.ascent()) / 2F),
                labelTextPaint
            )
        }
    }

    fun setProgressBackgroundColor(color: Int) {
        backgroundPaint.color = color
    }

    fun setTrackColor(color: Int) {
        trackPaint.color = color
    }

    fun setIndicatorColor(color: Int) {
        indicatorPaint.color = color
    }

    fun setLabelTextColor(color: Int) {
        labelTextPaint.color = color
    }

    fun setProgress(pro: Int) {
        progress = (360F * pro.toFloat()) / 100F
    }

    fun setLabelText(text: String) {
        labelText = text
    }

    companion object {
        const val DEFAULT_PROGRESS = 0F
        const val DEFAULT_LABEL_TEXT = ""

        const val INDICATOR_OFFSET_FROM_BACKGROUND = 6F
    }
}