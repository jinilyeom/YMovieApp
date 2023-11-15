package com.ymovie.app.util

import android.content.res.Resources
import android.util.TypedValue

fun convertDpToPx(dp: Float, res: Resources): Int {
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.displayMetrics)

    return px.toInt()
}