package com.ph.musicplayer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanMusicUtils {

    private static final int[] musicIds = {R.raw.music1, R.raw.music2, R.raw.music3, R.raw.music4};
    private static final Map<Integer, String> musicMaps = new HashMap<Integer, String>() {{
        put(R.raw.music1, "康加舞.mp3");
        put(R.raw.music2, "手拍鼓.mp3");
        put(R.raw.music3, "打击乐器.mp3");
        put(R.raw.music4, "蓝调小号.mp3");
    }};
    private static final String musicFileName = "/musicplayer";

    public static List<Song> getMusicData(Context context) {
        List<Song> list = new ArrayList<>();//音乐列表
        String path = Environment.getExternalStorageDirectory() + "/musicplayer";
        File dirFile = new File(path);
        boolean f=true;
        if (!dirFile.exists()) {
            f = dirFile.mkdir();
        }
        for (int i = 0; i < musicIds.length; i++) {
            InputStream is = context.getResources().openRawResource(musicIds[i]);
            String FileName = path + "/" + musicMaps.get(musicIds[i]);
            try {
                File file = new File(FileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int length;
                while ((length = is.read(b)) > 0) {
                    fos.write(b, 0, length);
                }
                is.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(FileName);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Song song = new Song();
            song.setAlbumId(musicIds[i]);
            song.setAlbum(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            song.setId(musicIds[i]);
            song.setName(musicMaps.get(musicIds[i]));
            song.setPath(path + "/" + musicMaps.get(musicIds[i]));
            song.setSinger(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            song.setDuration(Integer.parseInt(duration));
            list.add(song);
        }
        return list;
    }

//     定义一个方法用来格式化获取到的时间
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return (time / 1000 / 60) + ":0" + time / 1000 % 60;
        } else {
            return (time / 1000 / 60) + ":" + time / 1000 % 60;
        }
    }

}
