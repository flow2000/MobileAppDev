package com.ph.musicplayer;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//music服务，起到播放音乐的功能
//MusicService刚刚启动的时候就注册了一个广播，为的是让它在歌曲播完进行下一首播放
public class MusicService extends Service {
    public static MediaPlayer mlastPlayer;//当前歌曲Media
    public static int mPosition;//当前歌曲下标
    private int position;
    private String path = null;
    private MediaPlayer player;
    private Song song;
    private List<Song> songlist;
    private Context context;

    private String TAG = "MusicService";
    private RemoteViews remoteView;
    private Notification notification;//通知
    private String notificationChannelId = "playMusic";//通知渠道id
    private int notifyId = 1;

    public static String ACTION = "to_service";
    public static String MAIN_UPDATE_UI = "index_activity_ui";
    public static String KEY_USR_ACTION = "key_usr_action";
    public static final int ACTION_PRE = 0, ACTION_PLAY_PAUSE = 1, ACTION_NEXT = 2;
    public static String KEY_MAIN_ACTIVITY_UI_BTN = "index_activity_ui_btn_key";
    public static String KEY_MAIN_ACTIVITY_UI_TEXT = "index_activity_ui_text_key";
    public static final int VAL_UPDATE_UI_PLAY = 1, VAL_UPDATE_UI_PAUSE = 2;

    NotificationCompat.Builder mBuilder;//notification建造者

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new musicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        songlist = ScanMusicUtils.getMusicData(this);//获取音乐列表
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(receiver, intentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
        if (mlastPlayer == null || mPosition != position) {
            Log.d("播放页", "onStartCommand: " + "mPosition" + mPosition + "   position" + position);
            prepare();
        } else {
            player = mlastPlayer;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    void prepare() {
        song = songlist.get(position);
        path = song.getPath();
        Log.d(TAG, "song path:" + path);
        player = new MediaPlayer();
        if (mlastPlayer != null) {
            mlastPlayer.stop();
            mlastPlayer.release();
        }
        mlastPlayer = player;
        mPosition = position;
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(path); //Prepare resources
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        postState(getApplicationContext(), VAL_UPDATE_UI_PLAY, position);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                position += 1;
                position = (position + songlist.size()) % songlist.size();
                song = songlist.get(position);
                mPosition = position;
                prepare();
            }
        });

    }

    private void postState(Context context, int state, int songid) {
        Intent actionIntent = new Intent(MusicService.MAIN_UPDATE_UI);
        actionIntent.putExtra(MusicService.KEY_MAIN_ACTIVITY_UI_BTN, state);
        actionIntent.putExtra(MusicService.KEY_MAIN_ACTIVITY_UI_TEXT, songid);
        context.sendBroadcast(actionIntent);
    }

    //对音乐的操作
    public class musicBinder extends Binder {
        public boolean isPlaying() {
            return player.isPlaying();//判断当前歌曲是否正在播放
        }

        public void play() {
            if (player.isPlaying())
                player.pause();
            else {
                player.start();
            }
        }

        //播放下一首歌
        public void next(int type) {
            mPosition += type;
            mPosition = (mPosition + songlist.size()) % songlist.size();
            song = songlist.get(mPosition);
            prepare();
        }

        //Returns the length of the music in milliseconds
        public int getDuration() {
            return player.getDuration();
        }

        public int getPosition() {
            return mPosition;
        }

        //Return the name of the music
        public String getName() {
            return song.getName();
        }

        public String getPath() {
            return song.getPath();
        }

        public String getSinger() {
            return song.getSinger();
        }

        //Returns the current progress of the music in milliseconds
        public int getCurrenPostion() {
            return player.getCurrentPosition();
        }

        public long getAlbumId() {
            return song.getAlbumId();
        }

        //Set the progress of music playback in milliseconds
        public void seekTo(int mesc) {
            player.seekTo(mesc);
        }
    }

    //创建广播，发送和接受当前歌曲信息
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION.equals(action)) {
                int widget_action = intent.getIntExtra(KEY_USR_ACTION, -1);
                switch (widget_action) {
                    case ACTION_PRE:
                        next(-1);
                        break;
                    case ACTION_PLAY_PAUSE:
                        play();
                        break;
                    case ACTION_NEXT:
                        next(1);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    public void play() {
        if (player.isPlaying()) {
            player.pause();
            postState(getApplicationContext(), VAL_UPDATE_UI_PAUSE, position);
        } else {
            player.start();
            postState(getApplicationContext(), VAL_UPDATE_UI_PLAY, position);
        }
    }

    //Play the next music
    public void next(int type) {
        if (type == -1) {
            if (player.getCurrentPosition() > 2000) {
                player.seekTo(0);
                return;
            }
        }
        position += type;
        position = (position + songlist.size()) % songlist.size();
        song = songlist.get(position);
        prepare();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
