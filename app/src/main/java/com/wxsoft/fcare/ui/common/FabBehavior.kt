package com.wxsoft.fcare.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FabBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior(context,attrs) {

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(parent, child, directTargetChild, target,
                    nestedScrollAxes);
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: FloatingActionButton, dependency: View): Boolean {
        return dependency is RecyclerView

    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton, target: View, dxConsumed: Int,
        dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int
    ) {
        // TODO Auto-generated method stub
        super.onNestedScroll(
            coordinatorLayout, child, target, dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed
        )

        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.hide()
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }

    companion object {
        private val TAG = "FabBehavior"
    }
}