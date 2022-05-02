package com.ph.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ph.schedule.adapter.DBAdapter;
import com.ph.schedule.bean.Schedule;

import java.util.ArrayList;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private Schedule[] schedules;
    List<Schedule> scheduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        schedules = dbAdapter.queryAll();
        Intent intent = getIntent();
        String s = intent.getStringExtra("schedule");
        if (s != null) {
            Gson gson = new Gson();
            Schedule entity = gson.fromJson(s, Schedule.class);
            System.out.println(entity);
        }


    }

}