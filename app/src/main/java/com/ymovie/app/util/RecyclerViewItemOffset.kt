package com.ymovie.app.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemOffset(private val offset: Rect) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.apply {
            this.top = offset.top
            this.bottom = offset.bottom
            this.left = offset.left
            this.right = offset.right
        }
    }
}