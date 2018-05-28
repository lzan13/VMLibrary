package com.vmloft.develop.library.example.jni;

import android.os.Bundle;
import butterknife.ButterKnife;
import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.ntools.VMTestJni;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Created by lzan13 on 2018/5/7.
 */

public class JniActivity extends VMActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        ButterKnife.bind(activity);

        init();
    }

    private void init() {
        StringBuilder builder = new StringBuilder();
        int[] array = { 9, 111, 230, 1, 32, 49, 22 };
        VMTestJni.giveArray(array);
        for (int i : array) {
            builder.append(i + ",");
        }
        VMLog.d(builder.toString());

        builder.delete(0, builder.length());
        int[] array2 = VMTestJni.getArray();
        for (int i : array2) {
            builder.append(i + ",");
        }
        VMLog.d(builder.toString());

        VMLog.d(VMTestJni.sayHelloJni());

        VMTestJni testJni = new VMTestJni();
        VMLog.i("修改属性前: %d, %s, %s", testJni.age, testJni.name, testJni.say);
        testJni.say();
        VMLog.i("修改属性后: %d, %s, %s", testJni.age, testJni.name, testJni.say);

        testJni.createGlobalRef();
        VMLog.i("Jni 层全局变量:%s", testJni.getGlobalRef());
        testJni.deleteGlobalRef();

        /**
         * 传递 java 数组到 jni，并获取 string 返回值
         */
        String[] strArr = { "Hello ", "world!", "This ", "is ", "java!" };
        VMLog.i(testJni.getString(strArr));

        /**
         * 从 jni 层获取字符串数组返回值
         */
        VMLog.i(VMStr.arrayToStr(testJni.getStringArray(), " "));
    }

    static {
        System.loadLibrary("vmntools");
    }
}
