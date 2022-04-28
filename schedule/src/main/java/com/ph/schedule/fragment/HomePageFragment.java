package com.ph.schedule.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ph.schedule.R;
import com.ph.schedule.adapter.CurrentScheduleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageFragment extends BasePageTitleFragment {

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private View mFragmentView;//父控件(由父控件找到子控件)
    private TextView topDateView;
    private TextView topTextView;
    private static final int[] viewId = {R.id.schedule_start_time, R.id.schedule_end_time, R.id.schedule_name, R.id.schedule_detail, R.id.schedule_status};
    private static final String[] dataName = {"start_time", "end_time", "title", "detail", "status"};
    private static final String[] title = {"日", "一", "二", "三", "四", "五", "六", ""};

    @Override
    protected View initView() {
        setTitleIcon("", true,"#2ABFFD");
        mFragmentView = View.inflate(getContext(), R.layout.fg_homepage, null);
        setData();
        listView = mFragmentView.findViewById(R.id.my_list);
        listView.setDivider(null);
        simpleAdapter = new CurrentScheduleAdapter(getActivity(), getData(), R.layout.activity_listview, dataName, viewId);
        listView.setAdapter(simpleAdapter);
        return mFragmentView;
    }

    private void setData() {
        topDateView = mFragmentView.findViewById(R.id.timeText);
        topTextView = mFragmentView.findViewById(R.id.comeText);
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String str = month + "月" + day + "日 星期" + title[week];
        topDateView.setText(str);
        topTextView.setText("今天也要加油哦");
    }

    @Override
    protected void initData() {

    }

    private List<Map<String, Object>> getData() {
        String[] startTimes = {"08:00", "10:00", "14:30", "16:25"};
        String[] endTimes = {"09:40", "11:40", "16:05", "18:00"};
        String[] titles = {"专业英语", "专业英语", "专业英语", "专业英语"};
        String[] details = {"第3-4节 学友楼401 刘美玲", "第3-4节 学友楼401 刘美玲", "第3-4节 学友楼401 刘美玲", "第3-4节 学友楼401 刘美玲"};
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < startTimes.length; i++) {
            Map map = new HashMap();
            map.put("start_time", startTimes[i]);
            map.put("end_time", endTimes[i]);
            map.put("title", titles[i]);
            map.put("detail", details[i]);
            map.put("status", "未开始");
            list.add(map);
        }
        return list;
    }

}
