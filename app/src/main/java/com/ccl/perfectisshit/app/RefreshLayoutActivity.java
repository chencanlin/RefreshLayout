package com.ccl.perfectisshit.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccl.perfectisshit.refreshlayout.view.PullableListView;
import com.ccl.perfectisshit.refreshlayout.view.RefreshLayout;

/**
 * Created by ccl on 2017/7/14.
 */

public class RefreshLayoutActivity extends Activity implements RefreshLayout.OnRefreshListener {
    private RefreshLayout mRefreshlayoutScrollView;
    private RefreshLayout mRefreshLayoutListView;
    private RefreshLayout mRefreshLayoutImageView;
    private PullableListView mPLV;
    public static final String COMMON_EXTRA_KEY_SHOW_LAYOUT = "common_extra_key_show_layout";
    public static final int REFRESHLAYOUT_SCROLLVIEW  = 0x001;
    public static final int REFRESHLAYOUT_LISTVIEW = 0x002;
    public static final int REFRESHLAYOUT_IMAGEVIEW = 0x003;

    private SparseArrayCompat<String> data = new SparseArrayCompat<>();
    private int mIntExtra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshlayout);
        init();
    }

    private void init() {
        initView();
        initData();
        setView();
    }

    private void setView() {
        MyPLVAdapter myPLVAdapter = new MyPLVAdapter();
        mPLV.setAdapter(myPLVAdapter);
        switch (mIntExtra){
            case REFRESHLAYOUT_SCROLLVIEW:
                mRefreshlayoutScrollView.setVisibility(View.VISIBLE);
                mRefreshLayoutListView.setVisibility(View.GONE);
                mRefreshLayoutImageView.setVisibility(View.GONE);
                break;
            case REFRESHLAYOUT_LISTVIEW:
                mRefreshLayoutListView.setVisibility(View.VISIBLE);
                mRefreshlayoutScrollView.setVisibility(View.GONE);
                mRefreshLayoutImageView.setVisibility(View.GONE);
                break;
            case REFRESHLAYOUT_IMAGEVIEW:
                mRefreshLayoutImageView.setVisibility(View.VISIBLE);
                mRefreshlayoutScrollView.setVisibility(View.GONE);
                mRefreshLayoutListView.setVisibility(View.GONE);
                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent.hasExtra(COMMON_EXTRA_KEY_SHOW_LAYOUT)){
            mIntExtra = intent.getIntExtra(COMMON_EXTRA_KEY_SHOW_LAYOUT, REFRESHLAYOUT_SCROLLVIEW);
        }
        for (int i = 0; i < 20; i++) {
            data.put(i, "Hello World ! " + i);
        }
    }

    private void initView() {
        mRefreshlayoutScrollView = findViewById(R.id.refreshlayout_scrollview);
        mRefreshLayoutListView = findViewById(R.id.refreshlayout_listview);
        mRefreshLayoutImageView = findViewById(R.id.refreshlayout_imageview);

        mPLV = findViewById(R.id.plv);

        mRefreshlayoutScrollView.setOnRefreshListener(this);
        mRefreshLayoutListView.setOnRefreshListener(this);
        mRefreshLayoutImageView.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(final RefreshLayout refreshLayout) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshStatus(RefreshLayout.COMPLETED);
                    }
                });
            }
        }).start();
    }


    private class MyPLVAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, null);
                viewHolder = new MyViewHolder(view);
            } else {
                viewHolder = (MyViewHolder) view.getTag();
            }
            viewHolder.mTv.setText(data.get(i));
            return view;
        }

        private class MyViewHolder {
            private final TextView mTv;

            MyViewHolder(View convertView) {
                mTv = convertView.findViewById(R.id.tv);
                convertView.setTag(this);
            }
        }
    }
}
