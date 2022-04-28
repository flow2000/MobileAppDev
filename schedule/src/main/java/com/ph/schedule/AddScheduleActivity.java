package com.ph.schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ph.schedule.adapter.DBAdapter;
import com.ph.schedule.bean.Schedule;

public class AddScheduleActivity extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private Schedule[] schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        schedules = dbAdapter.queryAll();
    }
}