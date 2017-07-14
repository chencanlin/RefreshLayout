package com.ccl.perfectisshit.refreshlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.ccl.perfectisshit.refreshlayout.interf.Pullable;

public class PullableListView extends ListView implements Pullable {

    public PullableListView(Context context) {
        super(context);
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getCount() == 0) {
            // item 为0时也可以下拉
            return true;
        } else if (computeVerticalScrollOffset() == 0) {
            // 滑动到顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getCount() == 0) {
            // item 为0时也可以上拉
            return true;
        } else if (computeVerticalScrollOffset() + computeVerticalScrollExtent() == computeVerticalScrollRange()) {
            // 滑动到底部了
            return true;
        }
        return false;
    }
}
