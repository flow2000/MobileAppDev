package com.ph.schedule.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ph.schedule.AddScheduleActivity;
import com.ph.schedule.MainActivity;
import com.ph.schedule.R;
import com.ph.schedule.adapter.DBAdapter;
import com.ph.schedule.bean.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulePageFragment extends BasePageTitleFragment {

    private View mFragmentView;

    private static Integer currentWeek = 1;
    private static Integer weekCount = 20;
    private static String startTime = "3/11";

    private final int[] dates = {0, R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday};
    private final String[] weeks = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private final String[] startTimes = {"", "08:00", "08:55", "10:00", "10:55", "", "14:30", "15:20", "16:25", "17:15", "19:40", "20:35", ""};
    private String[] endTimes = {"", "08:45", "09:40", "10:45", "11:40", "15:15", "16:05", "17:10", "18:00", "20:25", "21:20"};
    private String[] start = {"08:00", "10:00", "14:30", "16:25", "19:40"};
    private static final Map<String, String> timesMap = new HashMap<String, String>() {{
        put("08:00", "第1-2节");
        put("10:00", "第3-4节");
        put("14:30", "第5-6节");
        put("16:25", "第7-8节");
        put("19:40", "第9-10节");
    }};

    private int[][] classes = {
            new int[]{0, 0, 0, 0, 0, 0, 0, 0},
            new int[]{0, R.id.c1, R.id.c6, R.id.c11, R.id.c16, R.id.c21, R.id.c26, R.id.c31},
            new int[]{0, R.id.c2, R.id.c7, R.id.c12, R.id.c17, R.id.c22, R.id.c27, R.id.c32},
            new int[]{0, R.id.c3, R.id.c8, R.id.c13, R.id.c18, R.id.c23, R.id.c28, R.id.c33},
            new int[]{0, R.id.c4, R.id.c9, R.id.c14, R.id.c19, R.id.c24, R.id.c29, R.id.c34},
            new int[]{0, R.id.c5, R.id.c10, R.id.c15, R.id.c20, R.id.c25, R.id.c30, R.id.c35}
    };

    private static boolean isOnlyAdd = false;
    private static int currentId = 0;
    private static int lastId = 0;

    private static final String[] colorPool = new String[]{
            "#03A5EF", "#E6F4FF", "#3CB3C9", "#DEFBF7", "#7D7CE1", "#EEEDFF", "#FC6B50", "#FCEADC", "#ED5A74", "#FFEFF0"
    };

    private DBAdapter dbAdapter;
    private List<Schedule> scheduleList = new ArrayList<>();
    private static TextView textView = null;
    private GridLayout gridLayout;

    private static final int ADD_CODE = 7858;

    public SchedulePageFragment(Integer currentWeek, Integer weekCount, String startTime) {
        super();
        this.currentWeek = currentWeek;
        this.weekCount = weekCount;
        this.startTime = startTime;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();
    }

    @Override
    public void onPause(){
        super.onPause();
        setSchedule(); // 设置课程
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View initView() {
        mFragmentView = View.inflate(getActivity(), R.layout.fg_schedulepage, null);
        setSchedule(); // 设置课程
        return mFragmentView;
    }

    /**
     * 设置课程
     */
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void setSchedule() {
        setData();
        setDate();
        setWeek();
        gridLayout = mFragmentView.findViewById(R.id.gridLayoutId);
        int columnCount = gridLayout.getColumnCount();
        int rowCount = gridLayout.getRowCount() - 3;
        int index = 0;
        for (int i = 1; i < rowCount; i++) {
            for (int j = 1; j < columnCount; j++) {
                for (int k = 0; k < scheduleList.size(); k++) {
                    if (scheduleList.size() == 0) {
                        return;
                    }
                    Schedule s = scheduleList.get(k);
                    String startTime = startTimes[i];
                    String scheduleStartTime = s.getStartTime();
                    if (j == s.getScheduleWeek() && startTime.equals(scheduleStartTime)) {
                        int r = 1;
                        for (String t : start) {
                            if (t.equals(startTime)) {
                                break;
                            }
                            r++;
                        }
                        textView = gridLayout.findViewById(classes[r][j]);
                        textView.setText(s.getScheduleName() + "\n" + s.getAddress() + "\n" + s.getTeacher());
                        textView.setTextColor(Color.parseColor(colorPool[index++]));
                        textView.setBackgroundColor(Color.parseColor(colorPool[index++]));
                        textView.setOnLongClickListener(view -> {
                            AlertDialog dialog = createDialog(s);
                            dialog.show();
                            return false;
                        });
                        textView.setOnClickListener(view -> {
                            AlertDialog dialog = createDialog(s);
                            dialog.show();
                        });
                        if (index >= colorPool.length - 1) {
                            index = 0;
                        }
                        scheduleList.remove(k--);
                    } else {
                        try {
                            currentId = classes[i][j];
                        } catch (Exception e) {
                            continue;
                        }
                        int w = j;
                        textView = gridLayout.findViewById(currentId);
                        textView.setOnClickListener(view -> {
                            if (view.getId() != lastId) {
                                textView = gridLayout.findViewById(view.getId());
                                textView.setText("+");
                                textView.setTextSize(50);
                                textView.setTextColor(Color.parseColor("#BDBDBD"));
                                textView.setPadding(15, 20, 20, 20);
                                textView.setBackgroundColor(Color.parseColor("#F6F6F6"));
                                if (lastId != 0) {
                                    setTransparent(lastId);
                                }
                                lastId = view.getId();
                            } else {
                                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                                Schedule schedule = new Schedule();
                                schedule.setScheduleWeek(w);
                                schedule.setStartWeek(1);
                                schedule.setEndWeek(15);
                                intent.putExtra("schedule", schedule.toString());
                                startActivity(intent);
                                setTransparent(view.getId());
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * 设置格子透明
     *
     * @param id 视图id
     */
    private void setTransparent(int id) {
        textView = gridLayout.findViewById(id);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    /**
     * 创建弹窗
     *
     * @param s 课程对象
     * @return 弹窗对象
     */
    private AlertDialog createDialog(Schedule s) {
        String str = s.getStartWeek() + "-" + s.getEndWeek() + " | " +
                timesMap.get(s.getStartTime()) + " " + s.getStartTime() + "-" + s.getEndTime()
                + "\n" + s.getAddress() + " | " + s.getTeacher();
        return new AlertDialog.Builder(getActivity())
                .setTitle(s.getScheduleName())//标题
                .setMessage(str)//内容
                .setPositiveButton("编辑", (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                    intent.putExtra("schedule", s.toString());
                    startActivity(intent);
                })
                .setNeutralButton("删除", (dialog, which) -> {
                    dbAdapter.deleteOne(s.getScheduleId());
                    showMsg("删除成功");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("add", ADD_CODE);
                    startActivity(intent);
                    setSchedule();
                })
                .setNegativeButton("关闭", (dialog, which) -> {
                })
                .create();
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (dbAdapter != null) {
            Schedule[] schedules = dbAdapter.queryAll();
            for (int i = 0; i < schedules.length; i++) {
                Schedule schedule = schedules[i];
                scheduleList.add(schedule);
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
     * 设置本周日期
     */
    private void setDate() {
        Long[] dateArray = getTimeInterval(new Date());
        Long startTime = dateArray[0];
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 1; i < dates.length; i++) {
            TextView t = mFragmentView.findViewById(dates[i]);
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
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     */
    private static Long[] getTimeInterval(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
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

    /**
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
