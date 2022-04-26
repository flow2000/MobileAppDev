package com.ph.schedule;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //初始化fragment
    private HomePageFragment mHomePageFragment;
    private SchedulePageFragment mSchedulePageFragment;
    private SettingsPageFragment mSettingsPageFragment;

    //片段类容
    private FrameLayout mFlFragmentContent;
    //底部3个按钮
    private RelativeLayout mRlFirstLayout;
    private RelativeLayout mRlThirdLayout;
    private RelativeLayout mRlFourLayout;

    private ImageView mIvFirstHome;
    private TextView mTvFirstHome;
    private ImageView mIvThirdRecommend;
    private TextView mTvThirdRecommend;
    private ImageView mIvFourMine;
    private TextView mTvFourMine;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        initView(); // 初始化视图
    }

    private void initView() {
        mFlFragmentContent = (FrameLayout) findViewById(R.id.fl_fragment_content);
        mRlFirstLayout = (RelativeLayout) findViewById(R.id.rl_first_layout);
        mIvFirstHome = (ImageView) findViewById(R.id.iv_first_home);
        mTvFirstHome = (TextView) findViewById(R.id.tv_first_home);
        mRlThirdLayout = (RelativeLayout) findViewById(R.id.rl_third_layout);
        mIvThirdRecommend = (ImageView) findViewById(R.id.iv_third_recommend);
        mTvThirdRecommend = (TextView) findViewById(R.id.tv_third_recommend);
        mRlFourLayout = (RelativeLayout) findViewById(R.id.rl_four_layout);
        mIvFourMine = (ImageView) findViewById(R.id.iv_four_mine);
        mTvFourMine = (TextView) findViewById(R.id.tv_four_mine);

        //给五个按钮设置监听器
        mRlFirstLayout.setOnClickListener(this);
        mRlThirdLayout.setOnClickListener(this);
        mRlFourLayout.setOnClickListener(this);
        //默认第一个首页被选中高亮显示
        mRlFirstLayout.setSelected(true);
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.fl_fragment_content, new HomePageFragment());
        mTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        mTransaction = mFragmentManager.beginTransaction(); //开启事务
        hideAllFragment(mTransaction);
        switch (v.getId()) {
            //今日课程
            case R.id.rl_first_layout:
                selected();
                mRlFirstLayout.setSelected(true);
                if (mHomePageFragment == null) {
                    mHomePageFragment = new HomePageFragment();
                    mTransaction.add(R.id.fl_fragment_content, mHomePageFragment);    //通过事务将内容添加到内容页
                } else {
                    mTransaction.show(mHomePageFragment);
                }
                break;
            //课表
            case R.id.rl_third_layout:
                selected();
                mRlThirdLayout.setSelected(true);
                if (mSchedulePageFragment == null) {
                    mSchedulePageFragment = new SchedulePageFragment();
                    mTransaction.add(R.id.fl_fragment_content, mSchedulePageFragment);    //通过事务将内容添加到内容页
                } else {
                    mTransaction.show(mSchedulePageFragment);
                }
                break;
            //设置
            case R.id.rl_four_layout:
                selected();
                mRlFourLayout.setSelected(true);
                if (mSettingsPageFragment == null) {
                    mSettingsPageFragment = new SettingsPageFragment();
                    mTransaction.add(R.id.fl_fragment_content, mSettingsPageFragment);    //通过事务将内容添加到内容页
                } else {
                    mTransaction.show(mSettingsPageFragment);
                }
                break;
        }
        mTransaction.commit();
    }

    //设置所有按钮都是默认都不选中
    private void selected() {
        mRlFirstLayout.setSelected(false);
        mRlThirdLayout.setSelected(false);
        mRlFourLayout.setSelected(false);
    }

    //删除所有fragmtne
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mHomePageFragment != null) {
            transaction.hide(mHomePageFragment);
        }
        if (mSchedulePageFragment != null) {
            transaction.hide(mSchedulePageFragment);
        }
        if (mSettingsPageFragment != null) {
            transaction.hide(mSettingsPageFragment);
        }
    }
}
