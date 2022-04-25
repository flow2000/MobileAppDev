package com.ph.schedule;


import android.view.View;

public class SchedulePageFragment extends BasePageTitleFragent {
    @Override
    public View initView() {
        setTitleIcon("课表",true);
        return View.inflate(getContext(), R.layout.fg_schedulepage, null);
    }

    @Override
    public void initData() {

    }

}
