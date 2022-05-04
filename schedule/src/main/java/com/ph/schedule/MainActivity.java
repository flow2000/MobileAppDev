package com.ph.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.ph.schedule.adapter.DBAdapter;
import com.ph.schedule.bean.Schedule;
import com.ph.schedule.fragment.HomePageFragment;
import com.ph.schedule.fragment.SchedulePageFragment;
import com.ph.schedule.fragment.SettingsPageFragment;
import com.ph.schedule.utils.JsonDataUtil;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    //初始化fragment
    private HomePageFragment mHomePageFragment;
    private SchedulePageFragment mSchedulePageFragment;
//    private SettingsPageFragment mSettingsPageFragment;

    //片段类容
    private FrameLayout mFlFragmentContent;
    //底部3个按钮
    private RelativeLayout mRlFirstLayout;
    private RelativeLayout mRlThirdLayout;
//    private RelativeLayout mRlFourLayout;

    private ImageView mIvFirstHome;
    private TextView mTvFirstHome;
    private ImageView mIvThirdRecommend;
    private TextView mTvThirdRecommend;
    private ImageView mIvFourMine;
    private TextView mTvFourMine;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private DBAdapter dbAdapter;
    private ArrayList<Schedule> scheduleList;

    private static Integer currentWeek = 1;
    private static Integer weekCount = 20;
    private static String startTime = "3/11";
    private static final int ADD_CODE = 7858;
    private static final int RETURN_CODE = 7859;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        initView(); // 初始化视图
        readConfig(); // 读取配置
        readJsonData(); // 读取json数据
        readSQLite(); // 读取数据库数据
        Intent intent = getIntent();
        int add_code = intent.getIntExtra("add", 0);
        int return_code = intent.getIntExtra("return", 1);
        if (add_code == ADD_CODE || return_code == RETURN_CODE) {
            mTransaction = mFragmentManager.beginTransaction(); //开启事务
            hideAllFragment(mTransaction);
            selected();
            mRlThirdLayout.setSelected(true);
            mSchedulePageFragment = new SchedulePageFragment(currentWeek, weekCount, startTime);
            mTransaction.add(R.id.fl_fragment_content, mSchedulePageFragment);    //通过事务将内容添加到内容页
            mTransaction.commit();
        }

    }

    /**
     * 读取SharedPreferences持久化的数据
     */
    private void readConfig() {
        SharedPreferences pref = getSharedPreferences("schedule", MODE_PRIVATE);
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
     * 读取json数据
     */
    private void readJsonData() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        String JsonData = JsonDataUtil.getJson(this, "schedule.json");//获取assets目录下的json文件数据
        scheduleList = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(JsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                Schedule entity = gson.fromJson(data.optJSONObject(i).toString(), Schedule.class);
                scheduleList.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMsg("读取json数据出错");
        }

        for (int i = 0; i < scheduleList.size(); i++) {
            Schedule schedule = scheduleList.get(i);
            Schedule[] schedules = dbAdapter.queryOne(schedule.getScheduleId());
            if (schedules == null) {
                dbAdapter.insert(schedule);
            }
        }
    }

    /**
     * 读取数据库数据
     */
    private void readSQLite() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        Schedule[] schedules = dbAdapter.queryAll();
        for (int i = 0; i < schedules.length; i++) {
            scheduleList.add(schedules[i]);
        }
    }

    private void initView() {
        mFlFragmentContent = (FrameLayout) findViewById(R.id.fl_fragment_content);
        mRlFirstLayout = (RelativeLayout) findViewById(R.id.rl_first_layout);
        mIvFirstHome = (ImageView) findViewById(R.id.iv_first_home);
        mTvFirstHome = (TextView) findViewById(R.id.tv_first_home);
        mRlThirdLayout = (RelativeLayout) findViewById(R.id.rl_third_layout);
        mIvThirdRecommend = (ImageView) findViewById(R.id.iv_third_recommend);
        mTvThirdRecommend = (TextView) findViewById(R.id.tv_third_recommend);
//        mRlFourLayout = (RelativeLayout) findViewById(R.id.rl_four_layout);
//        mIvFourMine = (ImageView) findViewById(R.id.iv_four_mine);
//        mTvFourMine = (TextView) findViewById(R.id.tv_four_mine);

        //给五个按钮设置监听器
        mRlFirstLayout.setOnClickListener(this);
        mRlThirdLayout.setOnClickListener(this);
//        mRlFourLayout.setOnClickListener(this);
        //默认第一个首页被选中高亮显示
        mRlFirstLayout.setSelected(true);
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.fl_fragment_content, new HomePageFragment());
        mTransaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        mTransaction = mFragmentManager.beginTransaction(); //开启事务
        hideAllFragment(mTransaction);
        switch (v.getId()) {
            //今日课程
            case R.id.rl_first_layout:
                selected();
                mRlFirstLayout.setSelected(true);
                mHomePageFragment = new HomePageFragment();
                mTransaction.add(R.id.fl_fragment_content, mHomePageFragment);    //通过事务将内容添加到内容页
                break;
            //课表
            case R.id.rl_third_layout:
                selected();
                mRlThirdLayout.setSelected(true);
                mSchedulePageFragment = new SchedulePageFragment(currentWeek, weekCount, startTime);
                mTransaction.add(R.id.fl_fragment_content, mSchedulePageFragment);    //通过事务将内容添加到内容页
                break;
            //设置
//            case R.id.rl_four_layout:
//                selected();
//                mRlFourLayout.setSelected(true);
//                mSettingsPageFragment = new SettingsPageFragment(currentWeek, weekCount, startTime);
//                mTransaction.add(R.id.fl_fragment_content, mSettingsPageFragment);    //通过事务将内容添加到内容页
//                break;
        }
        mTransaction.commit();
    }

    //设置所有按钮都是默认都不选中
    private void selected() {
        mRlFirstLayout.setSelected(false);
        mRlThirdLayout.setSelected(false);
//        mRlFourLayout.setSelected(false);
    }

    //删除所有fragmtne
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mHomePageFragment != null) {
            transaction.hide(mHomePageFragment);
        }
        if (mSchedulePageFragment != null) {
            transaction.hide(mSchedulePageFragment);
        }
//        if (mSettingsPageFragment != null) {
//            transaction.hide(mSettingsPageFragment);
//        }
    }

    /**
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
