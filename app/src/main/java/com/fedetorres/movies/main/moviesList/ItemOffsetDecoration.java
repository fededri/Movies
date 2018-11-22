package com.fedetorres.movies.main.moviesList;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Federico Torres on 8/11/2017.
 */

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {


    private int mItemOffset, mLeftOffset, mTopOffset, mRightOffset, mBottomOffset;
    private boolean equallyOffset;


    public ItemOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
        equallyOffset = true;
    }

    public ItemOffsetDecoration(int leftOffset, int topOffset, int rightOffset, int bottomOffset) {
        mLeftOffset = leftOffset;
        mTopOffset = topOffset;
        mRightOffset = rightOffset;
        mBottomOffset = bottomOffset;
        equallyOffset = false;
    }


    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,

                               RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        if (equallyOffset)
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        else
            outRect.set(mLeftOffset, mTopOffset, mRightOffset, mBottomOffset);

    }
}

