package com.ph.schedule;


import android.view.View;

public class SettingsPageFragment extends BasePageTitleFragment {
    @Override
    public View initView() {
        setTitleIcon("设置",true,"#FFFFFF");
        return View.inflate(getContext(), R.layout.fg_settingspage, null);
    }

    @Override
    public void initData() {

    }


}
