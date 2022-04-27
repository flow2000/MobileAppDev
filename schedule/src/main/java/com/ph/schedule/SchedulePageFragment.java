package com.ph.schedule;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class SchedulePageFragment extends BasePageTitleFragment {

    private View mFragmentView;

    private static Integer currentWeek = 1;
    private static Integer weekCount = 20;
    private static String startTime = "3/11";

    private int[] times = {0, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday};
    private String[] weeks = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public SchedulePageFragment(Integer currentWeek, Integer weekCount, String startTime) {
        super();
        this.currentWeek = currentWeek;
        this.weekCount = weekCount;
        this.startTime = startTime;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View initView() {
        mFragmentView = View.inflate(getContext(), R.layout.fg_schedulepage, null);
        setWeek(); // 设置本周是第几周
        setDate(); // 设置本周日期
        setSchedule(); // 设置课程
        return mFragmentView;
    }

    /**
     * 设置本周日期
     */
    private void setDate() {
        Long[] dateArray = getTimeInterval(new Date());
        Long startTime = dateArray[0];
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 1; i < times.length; i++) {
            TextView t = mFragmentView.findViewById(times[i]);
            Date date = new Date(startTime + 1000L * 60 * 60 * 24 * (i - 1));
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            String str = weeks[i] + "\n" + month + "/" + calendar.get(Calendar.DAY_OF_MONTH);
            t.setText(str);
            if (i == week) {
                t.setTextColor(Color.parseColor("#4C93DA"));
            }
        }
    }

    /**
     * 设置本周是第几周
     */
    private void setWeek() {
        readConfig(); // 读取配置文件
        setTitleIcon("第" + currentWeek + "周", true, "#FFFFFF");
    }

    /**
     * 设置课程
     */
    private void setSchedule() {
        GridLayout gridLayout = mFragmentView.findViewById(R.id.gridLayoutId);
        int count = gridLayout.getColumnCount();
        for (int i = 0; i < times.length; i++) {
            View functionView = new View(getContext());
            //使用Spec定义子控件的位置和比重
            GridLayout.Spec rowSpec = GridLayout.spec(i / 3, 1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % 3, 1f);
            //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.height = 0;
            layoutParams.width = 0;
            //还可以根据位置动态定义子控件直接的边距，下面的意思是
            //第一行的子控件都有2dp的bottomMargin，中间位置的子控件都有2dp的leftMargin和rightMargin
            gridLayout.addView(functionView, layoutParams);
        }
    }

    /**
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     */
    private static Long[] getTimeInterval(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        Long imptimeBegin = cal.getTime().getTime();
        cal.add(Calendar.DATE, 6);
        Long imptimeEnd = cal.getTime().getTime();
        return new Long[]{imptimeBegin, imptimeEnd};
    }


    @Override
    public void initData() {

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
