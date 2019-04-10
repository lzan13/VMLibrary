package com.vmloft.develop.library.example.base;

import com.vmloft.develop.library.tools.VMApp;
import com.vmloft.develop.library.tools.VMTools;

/**
 * Created by lzan13 on 2017/7/7.
 * 程序入口
 */
public class App extends VMApp {
    @Override
    public void onCreate() {
        super.onCreate();

        VMTools.init(context);
    }

}
