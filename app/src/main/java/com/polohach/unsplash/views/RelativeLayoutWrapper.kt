package com.polohach.unsplash.views

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.view.MotionEvent
import com.polohach.unsplash.utils.printLogE


class RelativeLayoutWrapper : RelativeLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(event: MotionEvent) =
        try {
            super.onInterceptTouchEvent(event)
        } catch (e: IllegalArgumentException) {
            e.message.printLogE()
            false
        }
}
