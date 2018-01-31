package com.pub.todo.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pub.todo.R;


/**
 * Created by prabhu on 21/11/17.
 */

public class RecyclerViewItemDecorator extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int leftPadding;

    public RecyclerViewItemDecorator(Context context, int leftPadding) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        this.leftPadding = leftPadding;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}