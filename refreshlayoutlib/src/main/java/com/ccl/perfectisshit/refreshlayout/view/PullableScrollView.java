package com.ccl.perfectisshit.refreshlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.ccl.perfectisshit.refreshlayout.interf.Pullable;

public class PullableScrollView extends ScrollView implements Pullable {

    public PullableScrollView(Context context) {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return computeVerticalScrollOffset() == 0;
    }

    @Override
    public boolean canPullUp() {
        return computeVerticalScrollRange() == computeVerticalScrollOffset() + computeVerticalScrollExtent();
    }

}
