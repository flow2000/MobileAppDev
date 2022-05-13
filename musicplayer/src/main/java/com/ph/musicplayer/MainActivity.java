package com.ph.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MusicConnection conn;
    private String TAG = "DetailsActivity";
    private Button btn_pre;
    private Button btn_play;
    private Button btn_next;
    private ImageView btn_back;
    private ImageView album_pic;
    private SeekBar seekBar;
    private TextView song_info, tv_cur_time, tv_total_time;
    private MusicService.musicBinder musicControl;
    private static final int UPDATE_UI = 0;
    private List<Song> songList;
    private static final int REQUEST_CODE = 1024;

    MusicReceiver mReceiver;

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //hander更新ui
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    updateUI(); //循环更新播放进度
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();//检查文件读写权限并加载歌曲列表
        songList = ScanMusicUtils.getMusicData(this);
        Intent intent = new Intent(this, MusicService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", 1); //放第几首
        intent.putExtras(bundle);
        conn = new MusicConnection();
        startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);
        mReceiver = new MusicReceiver(new Handler());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.MAIN_UPDATE_UI);
        getApplicationContext().registerReceiver(mReceiver, intentFilter);
        bindView();
    }

    //绑定布局
    private void bindView() {
        btn_pre = findViewById(R.id.btn_pre);
        btn_play = findViewById(R.id.btn_play);
        btn_next = findViewById(R.id.btn_next);
        seekBar = findViewById(R.id.sb);
        song_info = findViewById(R.id.nowSongInfo);
        tv_cur_time = findViewById(R.id.tv_cur_time);
        tv_total_time = findViewById(R.id.tv_total_time);
        btn_pre.setOnClickListener(this);
        btn_play.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        //进度条拉进度监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicControl.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //连接Service
    private class MusicConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "::MyConnection::onServiceConnected");
            musicControl = (MusicService.musicBinder) service;
            updatePlayState();
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "::MyConnection::onServiceDisconnected");

        }
    }

    //接收MusicService的广播消息
    public class MusicReceiver extends BroadcastReceiver {
        private final Handler handler;

        public MusicReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            handler.post(new Runnable() {
                //获取后台歌曲的播放状态然后更新UI
                @Override
                public void run() {
                    int play_pause = intent.getIntExtra(MusicService.KEY_MAIN_ACTIVITY_UI_BTN, -1);
                    int songid = intent.getIntExtra(MusicService.KEY_MAIN_ACTIVITY_UI_TEXT, -1);
                    song_info.setText(songList.get(songid).getName() + "\n" + songList.get(songid).getSinger());
                    switch (play_pause) {
                        case MusicService.VAL_UPDATE_UI_PLAY:
                            btn_play.setText("暂停");
                            break;
                        case MusicService.VAL_UPDATE_UI_PAUSE:
                            btn_play.setText("播放");
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    //控制按钮监听
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                play(view);
                break;
            case R.id.btn_next:
                next(view);
                break;
            case R.id.btn_pre:
                pre(view);
                break;
        }
    }

    //更新进度条
    private void updateProgress() {
        int currenPostion = musicControl.getCurrenPostion();
        seekBar.setProgress(currenPostion);
    }


    //更新按钮状态
    public void updatePlayState() {
        if (MusicService.mlastPlayer != null && MusicService.mlastPlayer.isPlaying()) {
            btn_play.setText("暂停");
        } else {
            btn_play.setText("播放");
        }
    }

    //向后台MusicReceiver发送广播，如果在播放就暂停，如果在暂停就播放
    public void play(View view) {
        Intent intent = new Intent(MusicService.ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(MusicService.KEY_USR_ACTION, MusicService.ACTION_PLAY_PAUSE);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        updatePlayState();
    }

    //向后台MusicReceiver发送广播，播放下一首
    public void next(View view) {
        Intent intent = new Intent(MusicService.ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(MusicService.KEY_USR_ACTION, MusicService.ACTION_NEXT);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        updatePlayState();
    }

    //向后台MusicReceiver发送广播，播放上一首
    public void pre(View view) {
        Intent intent = new Intent(MusicService.ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(MusicService.KEY_USR_ACTION, MusicService.ACTION_PRE);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        updatePlayState();
    }

    public void updateUI() {

        //获取歌曲名
        String str = musicControl.getName() + "\n" + musicControl.getSinger();
        //获取歌曲时长
        int total_time = musicControl.getDuration();
        seekBar.setMax(total_time);
        //获取当前进度
        int cur_time = musicControl.getCurrenPostion();
        seekBar.setProgress(cur_time);

        song_info.setText(str);
        tv_cur_time.setText(timeToString(cur_time));
        tv_total_time.setText(timeToString(total_time));

        updateProgress();

        //主线程更新ui
        handler.sendEmptyMessageDelayed(UPDATE_UI, 500); //循环更新播放进度
    }

    //转化分钟秒数
    private String timeToString(int time) {
        time /= 1000;
        return String.format("%02d:%02d", time / 60, time % 60);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (musicControl != null) {
            handler.sendEmptyMessage(UPDATE_UI);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出程序，解绑服务
        getApplicationContext().unregisterReceiver(mReceiver);
        Log.d("退出播放页", "onDestroy: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("暂停播放页", "onStop: ");
        //Stop the progress of the update progress bar
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("进入播放页", "onResume: ");
    }

    public void loadSongList() {
        songList = ScanMusicUtils.getMusicData(this);//初始化获取歌曲信息
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
            } else {
            }
        }
    }


}