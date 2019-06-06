package com.dani.dialogflowassistant.vista.bottomsheet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class DisableBottomSheet<V extends View> extends BottomSheetBehavior<V> {
    private boolean disabled;

    public DisableBottomSheet() {
        super();
    }

    public DisableBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        if (disabled)
            return false;
        else
            return super.onInterceptTouchEvent(parent, child, event);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {
        if (disabled)
            return false;
        else
            return super.onTouchEvent(parent, child, event);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (disabled)
            return false;
        else
            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                    axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (!disabled)
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        if (!disabled)
            super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY) {
        if (disabled)
            return false;
        else
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    public void setDisabled() {
        if (!disabled)
            disabled = true;
    }

    public void setEnabled() {
        if (disabled)
            disabled = false;
    }


    public boolean isDisabled() {
        return disabled;
    }
}
