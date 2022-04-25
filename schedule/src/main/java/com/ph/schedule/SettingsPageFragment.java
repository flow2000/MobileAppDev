package com.ph.schedule;


import android.view.View;

public class SettingsPageFragment extends BasePageTitleFragent {
    @Override
    public View initView() {
        setTitleIcon("设置",true);
        return View.inflate(getContext(), R.layout.fg_settingspage, null);
    }

    @Override
    public void initData() {

    }


}
