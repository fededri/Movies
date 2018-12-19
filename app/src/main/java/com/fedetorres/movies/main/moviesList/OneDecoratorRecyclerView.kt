package com.fedetorres.movies.main.moviesList

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class OneDecoratorRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)


    override fun addItemDecoration(decor: ItemDecoration) {
        if (itemDecorationCount == 0)
            super.addItemDecoration(decor)
        else return
    }
}