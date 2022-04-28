package com.ph.schedule.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ph.schedule.R;

public abstract class BasePageTitleFragment extends Fragment {
    private View mFragmentView;//父控件(由父控件找到子控件)
    private ImageView mIvLogoPage;
    private TextView mTvTitlePage;
    private FrameLayout mFlTitleContentPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.base_top_title_page, container, false);   //通用布局(图片 充值)
        mIvLogoPage = (ImageView) mFragmentView.findViewById(R.id.iv_logo_page);
        mTvTitlePage = (TextView) mFragmentView.findViewById(R.id.tv_title_page);
        mFlTitleContentPage = (FrameLayout) mFragmentView.findViewById(R.id.fl_title_content_page);
        View view = initView();
        mFlTitleContentPage.addView(view);
        return mFragmentView;
    }

    public void setTitleIcon(String msg, boolean show, String color) {    //设置标题和图标
        RelativeLayout layout = mFragmentView.findViewById(R.id.topView);
        layout.setBackgroundColor(Color.parseColor(color));
        mTvTitlePage.setText(msg);  //设置标题
        mTvTitlePage.setVisibility(show ? View.VISIBLE : View.GONE);     //设置标题显示  true就是显示  false就是不显示
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract View initView();
    protected abstract void initData();
}
