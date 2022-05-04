package com.ph.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ph.schedule.adapter.DBAdapter;
import com.ph.schedule.bean.Schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddScheduleActivity extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private Schedule[] schedules;
    Schedule schedule;
    TextView title, btn_return, btn_save;
    EditText t_name, t_address, t_teacher, t_schedule_week;
    private List<String> timeList = new ArrayList<>();
    private List<String> startWeekList = new ArrayList<>();
    private List<String> endWeekList = new ArrayList<>();
    private Spinner t_schedule_time;
    private Spinner t_start_week;
    private Spinner t_end_week;
    private ArrayAdapter<String> adapterTime;
    private ArrayAdapter<String> adapterSWeek;
    private ArrayAdapter<String> adapterEWeek;
    private String selectedTime = "第1-2节";
    private int selectedSWeek = 1;
    private int selectedEWeek = 20;

    private Intent intent;
    private static final Map<String, String> startTimesMap = new HashMap<String, String>() {{
        put("第1-2节", "08:00");
        put("第3-4节", "10:00");
        put("第5-6节", "14:30");
        put("第7-8节", "16:25");
        put("第9-10节", "19:40");
    }};
    private static final Map<String, String> endTimesMap = new HashMap<String, String>() {{
        put("第1-2节", "09:40");
        put("第3-4节", "11:40");
        put("第5-6节", "16:05");
        put("第7-8节", "18:00");
        put("第9-10节", "21:20");
    }};

    private static final int ADD_CODE = 7858;
    private static final int RETURN_CODE = 7859;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        initView();
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        schedules = dbAdapter.queryAll();
        intent = getIntent();
        String s = intent.getStringExtra("schedule");
        if (s != null) {
            Gson gson = new Gson();
            schedule = gson.fromJson(s, Schedule.class);
            setSpinnerItemSelectedByValue(t_schedule_time, selectedTime);
        }
        if (schedule != null && schedule.getScheduleId() != -1) {
            setView();
        }else{
            title.setText("新建课程");
        }
        btn_return.setOnClickListener(view -> {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("return", RETURN_CODE);
            startActivity(intent);
        });
        btn_save.setOnClickListener(view -> {
            if (schedule == null) {
                schedule = new Schedule();
            }
            schedule.setScheduleName(t_name.getText().toString());
            schedule.setAddress(t_address.getText().toString());
            schedule.setTeacher(t_teacher.getText().toString());
            schedule.setStartTime(startTimesMap.get(selectedTime));
            schedule.setEndTime(endTimesMap.get(selectedTime));
            schedule.setStartWeek(selectedSWeek);
            schedule.setEndWeek(selectedEWeek);

            if (schedule.getScheduleId() == -1) {
                dbAdapter.insert(schedule);
            } else {
                dbAdapter.updateOne(schedule.getScheduleId(), schedule);
            }
            showMsg("成功");
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("add", ADD_CODE);
            startActivity(intent);
        });
    }

    private void initView() {
        title = findViewById(R.id.title);
        t_name = findViewById(R.id.schedule_name);
        t_address = findViewById(R.id.address);
        t_teacher = findViewById(R.id.teacher);
        t_schedule_time = findViewById(R.id.schedule_time);
        t_start_week = findViewById(R.id.start_week);
        t_end_week = findViewById(R.id.end_week);
        btn_return = findViewById(R.id.btn_return);
        btn_save = findViewById(R.id.btn_save);
        timeList.add("第1-2节");
        timeList.add("第3-4节");
        timeList.add("第5-6节");
        timeList.add("第7-8节");
        timeList.add("第9-10节");
        adapterTime = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeList);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t_schedule_time.setAdapter(adapterTime);
        t_schedule_time.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> argO, View arg1, int arg2, long arg3) {
                argO.setVisibility(View.VISIBLE);
                selectedTime = adapterTime.getItem(arg2);
            }

            public void onNothingSelected(AdapterView<?> argO) {
                argO.setVisibility(View.VISIBLE);
            }
        });

        for (int i = 1; i < 20; i++) {
            startWeekList.add(i + "");
        }

        adapterSWeek = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, startWeekList);
        adapterSWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t_start_week.setAdapter(adapterSWeek);
        t_start_week.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> argO, View arg1, int arg2, long arg3) {
                argO.setVisibility(View.VISIBLE);
                selectedSWeek = Integer.parseInt(adapterSWeek.getItem(arg2));
                for (int i = 0; i <= selectedSWeek; i++) {
                    for (int j = 0; j < endWeekList.size(); j++) {
                        if ((i + "").equals(endWeekList.get(j))) {
                            endWeekList.remove(j);
                        }
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> argO) {
                argO.setVisibility(View.VISIBLE);
            }
        });

        for (int i = selectedSWeek; i <= selectedEWeek; i++) {
            endWeekList.add(i + "");
        }

        adapterEWeek = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, endWeekList);
        adapterEWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t_end_week.setAdapter(adapterEWeek);
        t_end_week.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> argO, View arg1, int arg2, long arg3) {
                argO.setVisibility(View.VISIBLE);
                selectedEWeek = Integer.parseInt(adapterEWeek.getItem(arg2));
            }

            public void onNothingSelected(AdapterView<?> argO) {
                argO.setVisibility(View.VISIBLE);
            }
        });


    }

    private void setView() {
        t_name.setText(schedule.getScheduleName());
        t_address.setText(schedule.getAddress());
        t_teacher.setText(schedule.getTeacher());
        for (String key : startTimesMap.keySet()) {
            if (startTimesMap.get(key).equals(schedule.getStartTime())) {
                selectedTime = key;
            }
        }
        t_start_week.setSelection(schedule.getStartWeek(), true);
        t_end_week.setSelection(schedule.getEndWeek(), true);
    }

    /**
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }
}