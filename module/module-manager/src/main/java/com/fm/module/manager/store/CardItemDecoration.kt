package com.fm.module.manager.store

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class CardItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        super.getItemOffsets(outRect, itemPosition, parent)
        outRect.set(15, 15, 15, 15)
    }
}