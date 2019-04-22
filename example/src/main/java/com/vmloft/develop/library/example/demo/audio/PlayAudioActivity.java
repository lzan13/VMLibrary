package com.vmloft.develop.library.example.demo.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.utils.VMLog;

import butterknife.OnClick;

/**
 * Created by lzan13 on 2017/6/14.
 * 测试系统播放音频相关界面
 */
public class PlayAudioActivity extends AppActivity {

    private PlayAudioActivity activity;

    // 音频管理器
    private AudioManager audioManager;
    // 音频池
    private SoundPool soundPool;
    // 声音资源 id
    private int streamID;
    private int loadId;
    private boolean isLoaded = false;

    @Override
    protected int layoutId() {
        return R.layout.activity_audio_play;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {

        // 初始化音频池
        initSoundPool();
        // 音频管理器
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @OnClick({R.id.btn_play, R.id.btn_stop_play, R.id.btn_open_speaker, R.id.btn_close_speaker})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                attemptPlayCallSound();
                break;
            case R.id.btn_stop_play:
                stopCallSound();
                break;
            case R.id.btn_open_speaker:
                openSpeaker();
                break;
            case R.id.btn_close_speaker:
                closeSpeaker();
                break;
        }
    }

    /**
     * 打开扬声器
     * 主要是通过扬声器的开关以及设置音频播放模式来实现
     * 1、MODE_NORMAL：是正常模式，一般用于外放音频
     * 2、MODE_IN_CALL：
     * 3、MODE_IN_COMMUNICATION：这个和 CALL 都表示通讯模式，不过 CALL 在华为上不好使，故使用 COMMUNICATION
     * 4、MODE_RINGTONE：铃声模式
     */
    public void openSpeaker() {
        // 检查是否已经开启扬声器
        if (!audioManager.isSpeakerphoneOn()) {
            // 打开扬声器
            audioManager.setSpeakerphoneOn(true);
        }
        // 开启了扬声器之后，因为是进行通话，声音的模式也要设置成通讯模式
        audioManager.setMode(AudioManager.MODE_NORMAL);
        VMLog.d("open speaker! audio mode %d", audioManager.getMode());
    }

    /**
     * 关闭扬声器，即开启听筒播放模式
     * 更多内容看{@link #openSpeaker()}
     */
    public void closeSpeaker() {
        // 检查是否已经开启扬声器
        if (audioManager.isSpeakerphoneOn()) {
            // 关闭扬声器
            audioManager.setSpeakerphoneOn(false);
        }
        // 设置声音模式为通讯模式
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        VMLog.d("close speaker! audio mode %d", audioManager.getMode());
    }

    /**
     * ----------------------------- Sound start -----------------------------
     * 初始化 SoundPool
     */
    private void initSoundPool() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    // 设置音频要用在什么地方，这里选择电话通知铃音
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            // 当系统的 SDK 版本高于21时，使用 build 的方式实例化 SoundPool
            soundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(1).build();
        } else {
            // 老版本使用构造函数方式实例化 SoundPool，MODE 设置为铃音 MODE_RINGTONE
            soundPool = new SoundPool(1, AudioManager.MODE_RINGTONE, 0);
        }
    }

    /**
     * 加载音效资源
     */
    private void loadSound() {
        loadId = soundPool.load(activity, R.raw.sound_call_incoming, 1);
    }

    /**
     * 尝试播放呼叫通话提示音
     */
    public void attemptPlayCallSound() {
        // 检查音频资源是否已经加载完毕
        if (isLoaded) {
            playCallSound();
        } else {
            // 播放之前先去加载音效
            loadSound();
            // 设置资源加载监听，也因为加载资源在单独的进程，需要时间，所以等监听到加载完成才能播放
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    VMLog.d("SoundPool load complete! loadId: %d", loadId);
                    isLoaded = true;
                    // 首次监听到加载完毕，开始播放音频
                    playCallSound();
                }
            });
        }
    }

    /**
     * 播放音频
     */
    private void playCallSound() {
        VMLog.d("play sound loadId: %d", loadId);
        // 打开扬声器
        openSpeaker();
        // 设置音频管理器音频模式为铃音模式
        audioManager.setMode(AudioManager.MODE_RINGTONE);
        // 播放提示音，返回一个播放的音频id，等下停止播放需要用到
        if (soundPool != null) {
            streamID = soundPool.play(loadId, // 播放资源id；就是加载到SoundPool里的音频资源顺序
                    0.5f,   // 左声道音量
                    0.5f,   // 右声道音量
                    1,      // 优先级，数值越高，优先级越大
                    -1,     // 是否循环；0 不循环，-1 循环，N 表示循环次数
                    1);     // 播放速率；从0.5-2，一般设置为1，表示正常播放
        }
    }

    /**
     * 关闭音效的播放，并释放资源
     */
    protected void stopCallSound() {
        if (soundPool != null) {
            // 停止播放音效
            soundPool.stop(streamID);
            // 卸载音效
            //soundPool.unload(loadId);
            // 释放资源
            //soundPool.release();
        }
    }
}
