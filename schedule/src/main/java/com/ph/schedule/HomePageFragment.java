package com.ph.schedule;

import android.view.View;

public class HomePageFragment extends BasePageTitleFragent {

    @Override
    protected View initView() {
        setTitleIcon("今日课程",true);
        return View.inflate(getContext(), R.layout.fg_homepage, null);
    }

    @Override
    protected void initData() {

    }
}
