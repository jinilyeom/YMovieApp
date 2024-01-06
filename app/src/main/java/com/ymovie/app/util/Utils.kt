package com.ymovie.app.util

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue

fun convertDpToPx(dp: Float, res: Resources): Int {
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.displayMetrics)

    return px.toInt()
}

fun setItemOffset(leftPx: Int = 0, topPx: Int = 0, rightPx: Int = 0, bottomPx: Int = 0): Rect {
    return Rect(leftPx, topPx, rightPx, bottomPx)
}