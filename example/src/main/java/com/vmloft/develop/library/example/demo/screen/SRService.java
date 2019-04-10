package com.vmloft.develop.library.example.demo.screen;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class SRService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 开始截图
     */
    public boolean startScreenShort() {
        return SRManager.getInstance().startScreenShort();
    }

    /**
     * 开始录屏
     */
    public boolean startScreenRecord() {
        return SRManager.getInstance().startScreenRecord();
    }


    /**
     * 结束录屏
     *
     * @return true
     */
    public boolean stopRecord() {
        return SRManager.getInstance().stop();
    }

    public class RecordBinder extends Binder {
        public SRService getRecordService() {
            return SRService.this;
        }
    }
}
