package com.ph.schedule;


import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

public class SettingsPageFragment extends BasePageTitleFragment {

    private SharedPreferences sharedPreferences;

    private static Integer currentWeek = 1;
    private static Integer weekCount = 20;
    private static String startTime = "3/11";

    public SettingsPageFragment(Integer currentWeek, Integer weekCount, String startTime) {
        super();
        this.currentWeek = currentWeek;
        this.weekCount = weekCount;
        this.startTime = startTime;
    }


    @Override
    public View initView() {
        setTitleIcon("设置", true, "#FFFFFF");
        writeState("5", "20", "3/11");
        return View.inflate(getContext(), R.layout.fg_settingspage, null);
    }

    @Override
    public void initData() {

    }

    /**
     * 保存状态
     */
    private void writeState(String currentWeek, String weekCount, String startTime) {
        sharedPreferences = getActivity().getSharedPreferences("schedule", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("currentWeek", currentWeek);
        editor.putString("weekCount", weekCount);
        editor.putString("startTime", startTime);
        editor.apply();
    }

    /**
     * 读取SharedPreferences持久化的数据
     */
    private void readConfig() {
        SharedPreferences pref = getActivity().getSharedPreferences("schedule", Activity.MODE_PRIVATE);
        String str1 = pref.getString("currentWeek", null);
        String str2 = pref.getString("weekCount", null);
        String str3 = pref.getString("startTime", null);
        if (str1 != null && str2 != null && str3 != null) {
            currentWeek = Integer.parseInt(str1);
            weekCount = Integer.parseInt(str1);
            startTime = str3;
        }
    }

}
