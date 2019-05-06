package com.wxsoft.fcare.ui.photo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import java.lang.IllegalArgumentException

class PhotoViewPager : ViewPager {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            Log.w(TAG, "onInterceptTouchEvent() ", ex)
            ex.printStackTrace()
        }

        return false
    }

    companion object {
        private val TAG = PhotoViewPager::class.java.simpleName
    }
}