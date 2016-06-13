package com.ciist.xunxun.app.game;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ciist.xunxun.R;
import com.ciist.xunxun.app.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by 郑浩楠 on 2016/5/23.
 */
public class GameMusic {
    private static MediaPlayer music;
    private static SoundPool soundPool;

    private static boolean musicSt = true; //音乐开关
    private static boolean soundSt = true; //音效开关
    private static Context context;

    //private static final int[] musicId = {R.raw.bg1};
    private static Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表

    /**
     * 初始化方法
     * @param c
     */
    public static void init(Context c)
    {
        context = c;

        initMusic();

        initSound();
    }

    //初始化音效播放器
    private static void initSound()
    {
        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,100);

        soundMap = new HashMap<Integer,Integer>();
//        soundMap.put(R.raw.bg1, soundPool.load(context, R.raw.bg1, 1));
//        soundMap.put(R.raw.bg2, soundPool.load(context, R.raw.bg2, 1));
    }

    //初始化音乐播放器
    private static void initMusic()
    {
//        int r = new Random().nextInt(musicId.length);
//        music = MediaPlayer.create(context,musicId[r]);
        music.setLooping(true);
    }

    /**
     * 播放音效
     */
    public static void playSound(int resId)
    {
        if(soundSt == false)
            return;

        Integer soundId = soundMap.get(resId);
        if(soundId != null)
            soundPool.play(soundId, 1, 1, 1, 0, 1);
    }

    /**
     * 暂停音乐
     */
    public static void pauseMusic()
    {
        if(music.isPlaying())
            music.pause();
    }

    /**
     * 播放音乐
     */
    public static void startMusic()
    {
        if(musicSt)
            music.start();
    }

    /**
     * 切换一首音乐并播放
     */
    public static void changeAndPlayMusic()
    {
        if(music != null)
            music.release();
        initMusic();
        startMusic();
    }

    /**
     * 获得音乐开关状态
     * @return
     */
    public static boolean isMusicSt() {
        return musicSt;
    }

    /**
     * 设置音乐开关
     */
    public static void setMusicSt(boolean musicSt) {
        GameMusic.musicSt = musicSt;
        if(musicSt)
            music.start();
        else
            music.stop();
    }

    /**
     * 获得音效开关状态
     * @return
     */
    public static boolean isSoundSt() {
        return soundSt;
    }

    /**
     * 设置音效开关
     */
    public static void setSoundSt(boolean soundSt) {
        GameMusic.soundSt = soundSt;
    }

    /**
     * 发出‘邦’的声音
     */
//    public static void paopao()
//    {
//        playSound(R.raw.paopao);
//    }
}







