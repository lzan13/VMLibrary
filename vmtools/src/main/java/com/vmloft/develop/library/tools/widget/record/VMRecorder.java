package com.vmloft.develop.library.tools.widget.record;

import android.media.MediaRecorder;

import com.vmloft.develop.library.tools.utils.VMDate;
import com.vmloft.develop.library.tools.utils.VMFile;
import com.vmloft.develop.library.tools.utils.logger.VMLog;
import com.vmloft.develop.library.tools.utils.VMStr;

import java.io.IOException;

/**
 * Created by lz on 2016/8/20.
 * 定义的录音功能单例类，主要处理录音的相关操作
 */
public class VMRecorder {
    public static final int ERROR_NONE = 0;     // 没有错误
    public static final int ERROR_SYSTEM = 1;   // 系统错误
    public static final int ERROR_FAILED = 2;   // 录制失败
    public static final int ERROR_RECORDING = 3;// 正在录制
    public static final int ERROR_CANCEL = 4;   // 录音取消
    public static final int ERROR_SHORT = 5;    // 录音时间过短

    // 媒体录影机，可以录制音频和视频
    private MediaRecorder mMediaRecorder;
    // 音频采样率 单位 Hz
    protected int mSamplingRate = 8000;
    // 音频编码比特率
    protected int mEncodingBitRate = 64;
    // 录音最大持续时间 10 分钟
    protected int mMaxDuration = 10 * 60 * 1000;
    // 计算分贝基准值
    protected int mDecibelBase = 200;

    // 是否录制中
    protected boolean isRecording = false;

    // 录制文件保存路径
    protected String mRecordFile;

    /**
     * 单例类的私有构造方法
     */
    private VMRecorder() {
    }

    /**
     * 内部类实现单例模式
     */
    public static class InnerHolder {
        public static VMRecorder INSTANCE = new VMRecorder();
    }

    /**
     * 获取单例类的实例
     */
    public static VMRecorder getInstance() {
        return InnerHolder.INSTANCE;
    }

    /**
     * 初始化录制音频
     */
    public void initVoiceRecorder() {
        // 实例化媒体录影机
        mMediaRecorder = new MediaRecorder();
        // 设置音频源为麦克风
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        /**
         * 设置音频文件编码格式，这里设置默认
         * https://developer.android.com/reference/android/media/MediaRecorder.AudioEncoder.html
         */
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        /**
         * 设置音频文件输出格式
         * https://developer.android.com/reference/android/media/MediaRecorder.OutputFormat.html
         */
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置音频采样率
        mMediaRecorder.setAudioSamplingRate(mSamplingRate);
        // 设置音频编码比特率
        mMediaRecorder.setAudioEncodingBitRate(mEncodingBitRate);
        // 设置录音最大持续时间
        mMediaRecorder.setMaxDuration(mMaxDuration);
    }

    /**
     * 开始录制声音文件
     *
     * @param path 录音文件保存地址，可以为空，默认保存到项目包名 files 下
     */
    public int startRecord(String path) {
        // 判断录制系统是否空闲
        if (isRecording) {
            return ERROR_RECORDING;
        }

        // 判断媒体录影机是否释放，没有则释放
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        // 设置录制状态
        isRecording = true;

        if (VMStr.isEmpty(path)) {
            // 这里默认保存在 /sdcard/android/data/packagename/files/下
            //File file = VMFile.createFile(VMFile.getFilesFromSDCard(), "VMVoice_", ".amr");
            mRecordFile = VMFile.getFilesFromSDCard() + "VMVoice_" + VMDate.filenameDateTime() + ".amr";
        } else {
            //File file = VMFile.createFile(path, "VMVoice_", ".amr");
            mRecordFile = path;
        }

        // 释放之后重新初始化
        initVoiceRecorder();

        // 设置录制输出文件
        mMediaRecorder.setOutputFile(mRecordFile);
        try {
            // 准备录制
            mMediaRecorder.prepare();
            // 开始录制
            mMediaRecorder.start();
        } catch (IOException e) {
            reset();
            VMLog.e("录音系统出现错误 " + e.getMessage());
            return ERROR_SYSTEM;
        }
        return ERROR_NONE;
    }

    /**
     * 停止录制
     */
    public int stopRecord() {
        // 停止录音，将录音状态设置为false
        isRecording = false;
        // 释放媒体录影机
        if (mMediaRecorder != null) {
            // 防止录音机 start 后马上调用 stop 出现异常
            mMediaRecorder.setOnErrorListener(null);
            try {
                // 停止录制
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                VMLog.e("录音系统出现错误 " + e.getMessage());
                reset();
                return ERROR_SYSTEM;
            } catch (RuntimeException e) {
                VMLog.e("录音系统出现错误 " + e.getMessage());
                reset();
                return ERROR_SYSTEM;
            }
        }
        // 根据录制结果判断录音是否成功
        if (!VMFile.isFileExists(mRecordFile)) {
            VMLog.e("录音失败没有生成文件");
            return ERROR_FAILED;
        }
        return ERROR_NONE;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {
        // 停止录音，将录音状态设置为false
        isRecording = false;
        // 释放媒体录影机
        if (mMediaRecorder != null) {
            try {
                // 停止录制
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                VMLog.e("录音系统出现错误 " + e.getMessage());
                reset();
            } catch (RuntimeException e) {
                VMLog.e("录音系统出现错误 " + e.getMessage());
                reset();
            }
        }
        // 取消录音，删除文件
        if (VMFile.isFileExists(mRecordFile)) {
            VMFile.deleteFile(mRecordFile);
        }
    }

    /**
     * 获取录制的语音文件
     */
    public String getRecordFile() {
        return mRecordFile;
    }

    /**
     * 获取声音分贝信息
     */
    public int getDecibel() {
        int decibel = 1;
        if (mMediaRecorder != null) {
            int ratio = 0;
            try {
                ratio = mMediaRecorder.getMaxAmplitude() / mDecibelBase;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            if (ratio > 0) {
                // 根据麦克风采集到的声音振幅计算声音分贝大小
                decibel = (int) (20 * Math.log10(ratio)) / 10;
            }
        }
        return decibel;
    }

    /**
     * 判断录音机是否正在录制中
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 重置录音机
     */
    public void reset() {
        isRecording = false;
        mRecordFile = null;
        if (mMediaRecorder != null) {
            // 重置媒体录影机
            mMediaRecorder.reset();
            // 释放媒体录影机
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }
}
