package com.ph.schedule;


import android.view.View;

public class SchedulePageFragment extends BasePageTitleFragment {
    @Override
    public View initView() {
        setTitleIcon("课表",true,"#FFFFFF");
        return View.inflate(getContext(), R.layout.fg_schedulepage, null);
    }

    @Override
    public void initData() {

    }

}
