package com.ccl.perfectisshit.refreshlayout.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ccl.perfectisshit.refreshlayout.R;
import com.ccl.perfectisshit.refreshlayout.interf.Pullable;


/**
 * Created by ccl on 2017/7/11.
 */

public class RefreshLayout extends ViewGroup {
    public static final int REFRESHING = 0;
    public static final int COMPLETED = 1;
    private int mRefreshStatus = COMPLETED;

    public static final int DEFAULT_HEADER_LAYOUT_ID = R.layout.layout_refreshlayout_head;
    public static final int DEFAULT_FOOTER_LAYOUT_ID = R.layout.layout_refreshlayout_footer;


    private Drawable loadingImgOne;
    private Drawable loadingImgTwo;
    private Drawable loadingImgThree;
    private int mHeaderLayoutId;
    private int mFooterLayoutId;
    private ViewGroup mHeader;
    private ViewGroup mFooter;
    private View mPullableView;
    private View mPullUpView;
    private ViewFlipper mViewFlipper;

    private boolean isCanPullDown = true;
    private boolean isCanPullUp = true;
    private boolean isInit = false;
    private ViewDragHelper mViewDragHelper;

    private int mTop;
    private int mHeaderMeasuredHeight;
    private int mFooterMeasuredHeight;

    private View hideTitle;
    private OnRefreshListener mListener;
    private ValueAnimator mValueAnimator;
    private TextView mTvDesc;


    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
        mHeaderLayoutId = typedArray.getResourceId(R.styleable.RefreshLayout_refreshlayout_header_layout, DEFAULT_HEADER_LAYOUT_ID);
        mFooterLayoutId = typedArray.getResourceId(R.styleable.RefreshLayout_refreshlayout_footer_layout, DEFAULT_FOOTER_LAYOUT_ID);
        loadingImgOne = typedArray.getDrawable(R.styleable.RefreshLayout_refreshlayout_loading_img_one);
        loadingImgTwo = typedArray.getDrawable(R.styleable.RefreshLayout_refreshlayout_loading_img_two);
        loadingImgThree = typedArray.getDrawable(R.styleable.RefreshLayout_refreshlayout_loading_img_three);
        if(loadingImgOne == null || loadingImgTwo == null || loadingImgThree == null){
            loadingImgOne = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.loading_view_01));
            loadingImgTwo = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.loading_view_02));
            loadingImgThree = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.loading_view_03));
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        mHeader = (ViewGroup) LayoutInflater.from(getContext()).inflate(mHeaderLayoutId, this, false);
        mFooter = (ViewGroup) LayoutInflater.from(getContext()).inflate(mFooterLayoutId, this, false);
        mViewFlipper = mHeader.findViewById(R.id.refreshlayout_header_viewflipper_id);
        ((ImageView) mViewFlipper.findViewById(R.id.iv_one)).setImageDrawable(loadingImgOne);
        ((ImageView) mViewFlipper.findViewById(R.id.iv_two)).setImageDrawable(loadingImgTwo);
        ((ImageView) mViewFlipper.findViewById(R.id.iv_three)).setImageDrawable(loadingImgThree);
        mTvDesc = mHeader.findViewById(R.id.tv_desc);
        mPullUpView = mFooter.findViewById(R.id.refreshlayout_footer_imageview_id);
        addView(mHeader);
        addView(mFooter);
        MyViewDragHelperCallBack myViewDragHelperCallBack = new MyViewDragHelperCallBack();
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, myViewDragHelperCallBack);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isInit) {
            mHeaderMeasuredHeight = mHeader.getMeasuredHeight();
            mFooterMeasuredHeight = mFooter.getMeasuredHeight();
            mHeader.layout(0, -mHeaderMeasuredHeight, getMeasuredWidth(), 0);
            mFooter.layout(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight() + mFooterMeasuredHeight);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child instanceof Pullable) {
                    mPullableView = child;
                    mPullableView.layout(0, 0, mPullableView.getMeasuredWidth(), mPullableView.getMeasuredHeight());
                    isInit = true;
                    return;
                }
            }
        } else {
            mPullableView.layout(0, mPullableView.getTop(), getMeasuredWidth(), mPullableView.getTop() + mPullableView.getMeasuredHeight());
            mTop = mPullableView.getTop();
            mHeader.layout(0, -mHeaderMeasuredHeight + mTop, getMeasuredWidth(), mTop);
            mFooter.layout(0, getMeasuredHeight() + mTop, getMeasuredWidth(), getMeasuredHeight() + mTop + mFooterMeasuredHeight);
            float mFooterScale = Math.abs((float) mTop / (float) mFooterMeasuredHeight);
            mPullUpView.setScaleX(mFooterScale);
            mPullUpView.setScaleY(mFooterScale);
            mPullUpView.setTranslationY((Math.abs(mTop) - mPullUpView.getMeasuredHeight()) / 2);
            mPullUpView.setAlpha(mFooterScale);

            float tempTop = (float) (mTop > mHeaderMeasuredHeight ? mHeaderMeasuredHeight : mTop);
            float mHeaderScale = Math.abs(tempTop / (float) mHeaderMeasuredHeight);
            mViewFlipper.setScaleX(mHeaderScale);
            mViewFlipper.setScaleY(mHeaderScale);
            mViewFlipper.setTranslationY(Math.abs(mHeaderMeasuredHeight - (tempTop + mViewFlipper.getMeasuredHeight()) / 2));
            mViewFlipper.setAlpha(mHeaderScale);
            if (hideTitle != null) {
                hideTitle.setVisibility(mTop == 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    private class MyViewDragHelperCallBack extends ViewDragHelper.Callback {
        /*
        * 指定捕获的view
        * */
        @Override
        public boolean tryCaptureView(View view, int i) {
            return view == mPullableView;
        }

        /*
        * view移动状态改变回调
        * */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        /*
        * 被捕获view回调
        * */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /*
        * 移动释放的回调
        * */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (mTop < mHeader.getMeasuredHeight() / 2) {
                mViewDragHelper.settleCapturedViewAt(0, 0);
            } else if (mTop >= mHeader.getMeasuredHeight() / 2) {
                mViewDragHelper.settleCapturedViewAt(0, mHeader.getMeasuredHeight());
                if (mRefreshStatus == COMPLETED) {
                    mRefreshStatus = REFRESHING;
                    mViewFlipper.startFlipping();
                    mTvDesc.setText(R.string.pull_down_refreshing_desc);
                    if (mListener != null) {
                        mListener.onRefresh(RefreshLayout.this);
                    }
                }
            }
            invalidate();
            requestLayout();
        }

        /*
        * 边缘触摸回调
        * */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        /*
        * 边缘锁定
        * */
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }

        /*
        * 边缘移动，强制移动指定view
        * */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mViewDragHelper.captureChildView(mPullableView, pointerId);
        }

        /*
       * 返回想移动view的水平移动范围
       * */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        /*
        * 返回想移动view的垂直移动范围
        * */
        @Override
        public int getViewVerticalDragRange(View child) {
            if (child == null)
                return 0;
            return child == mPullableView ? mPullableView.getHeight() : 0;
        }

        /*
        * 被水平移动view的位置
        * */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        /*
        * 被垂直移动view的位置
        * */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if ((top > 0 && ((Pullable) mPullableView).canPullDown() && isCanPullDown) || (top <= 0 && ((Pullable) mPullableView).canPullUp() && isCanPullUp)) {
                if (top >= -mFooter.getMeasuredHeight() && top <= mHeader.getMeasuredHeight() * 2) {
                    mTop = top;
                    if(mTop>=0 && mTop <= mHeader.getMeasuredHeight() ){
                        mTvDesc.setText(R.string.pull_down_desc);
                    }else if (mTop>mHeader.getMeasuredHeight()){
                        mTvDesc.setText(R.string.pull_down_release_desc);
                    }
                } else if (top < -mFooter.getMeasuredHeight()) {
                    mTop = -mFooter.getMeasuredHeight();
                } else {
                    mTop = mHeader.getMeasuredHeight() * 2;
                }
                requestLayout();
                return mTop;
            } else {
                return super.clampViewPositionVertical(child, top, dy);
            }
        }
    }

    /*
    * 由viewdraghelper判断是否需要拦截触摸事件
    * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    /*
    * viewdraghelper需要获取到触摸事件
    * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    /*
    * invalidate()会触发此方法
    * */
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            requestLayout();
            invalidate();
        }
    }

    /**
     * 刷新加载回调接口
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(RefreshLayout refreshLayout);
    }

    /*
    * 设置是否能够上拉
    * */
    public void setCanPullUp(boolean flag) {
        isCanPullUp = flag;
    }

    /*
    * 设置是否能够下拉
    * */
    public void setCanPullDown(boolean flag) {
        isCanPullDown = flag;
    }

    /*
    * 设置下拉过程中有需要隐藏的头或者view，刷新完成会险显示此view
    * */
    public void setHideTitle(View view) {
        hideTitle = view;
    }

    /*
    * 设置刷新回调
    * */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /*
    * 设置刷新状态，刷新完成可以设置为COMPLETED
    * */
    public void setRefreshStatus(int status) {
        mRefreshStatus = status;
        if (status == COMPLETED && mTop > 0) {
            mViewFlipper.stopFlipping();
            if (mValueAnimator == null) {
                mValueAnimator = ObjectAnimator.ofInt(mPullableView, "top", 0);
            }
            if (mValueAnimator.isRunning()) {
                mValueAnimator.cancel();
            }
            if (mValueAnimator != null) {
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        requestLayout();
                    }
                });
                mValueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clear();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            mValueAnimator.setDuration(600);
            mValueAnimator.setStartDelay(800);
            mValueAnimator.start();
        }
    }

    public void clear() {
        if (mValueAnimator == null) {
            return;
        }
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator.removeAllListeners();
        mValueAnimator = null;
    }
}
