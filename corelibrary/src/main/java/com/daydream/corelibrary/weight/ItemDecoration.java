package com.daydream.corelibrary.weight;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gjc on 2018/5/27.
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int mLeft;
    private int mRight;
    private int mTop;
    private int mBottom;
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mLeft;
        outRect.right = mRight;
        outRect.bottom = mBottom;
        outRect.top = mTop;
        /*if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }*/

    }
    public ItemDecoration(int space) {
        this.mLeft = space;
        this.mRight = space;
        this.mTop = space;
        this.mBottom = space;
    }

    public ItemDecoration(int left, int right, int top, int bottom) {
        this.mLeft = left;
        this.mRight = right;
        this.mTop = top;
        this.mBottom = bottom;
    }
}
