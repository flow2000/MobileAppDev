package com.ph.schedule.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ph.schedule.R;
import com.ph.schedule.adapter.CurrentScheduleAdapter;
import com.ph.schedule.adapter.DBAdapter;
import com.ph.schedule.bean.Schedule;

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
    private static final Map<String, String> timesMap = new HashMap<String, String>() {{
        put("08:00", "第1-2节");
        put("10:00", "第3-4节");
        put("14:30", "第5-6节");
        put("16:25", "第7-8节");
        put("19:40", "第9-10节");
    }};
    private DBAdapter dbAdapter;

    @Override
    protected View initView() {
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.open();
        mFragmentView = View.inflate(getContext(), R.layout.fg_homepage, null);
        setData();
        listView = mFragmentView.findViewById(R.id.my_list);
        listView.setDivider(null);
        simpleAdapter = new CurrentScheduleAdapter(getActivity(), getData(), R.layout.activity_listview, dataName, viewId);
        listView.setAdapter(simpleAdapter);
        return mFragmentView;
    }

    /**
     * 设置数据
     */
    private void setData() {
        setTitleIcon("", true, "#2ABFFD");
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

    /**
     * 获取今日课表数据数据
     * @return List<Map<String,Object>
     */
    private List<Map<String, Object>> getData() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        List<Map<String, Object>> list = new ArrayList<>();
        if (dbAdapter != null) {
            Schedule[] schedules = dbAdapter.queryAll();
            for (Schedule s : schedules) {
                if (s.getScheduleWeek() == week) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("start_time", s.getStartTime());
                    map.put("end_time", s.getEndTime());
                    map.put("title", s.getScheduleName());
                    map.put("detail", timesMap.get(s.getStartTime()) + " " + s.getAddress() + " " + s.getTeacher());
                    map.put("status", "未开始");
                    list.add(map);
                }
            }
        }
        return list;
    }

}
