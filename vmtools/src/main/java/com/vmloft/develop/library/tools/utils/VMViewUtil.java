package com.vmloft.develop.library.tools.utils;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lzan13 on 2018/3/29.
 */
public class VMViewUtil {

    public static void getAllChildViews(View view, int level) {
        if (level <= 0) {
            level = 1;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String viewName = String.format("% " + level * 4 + "d|- %s", 0, view.getAccessibilityClassName());
            Log.d("ViewTree", viewName);
            if (view instanceof ViewGroup) {
                level++;
                ViewGroup vp = (ViewGroup) view;
                for (int i = 0; i < vp.getChildCount(); i++) {
                    getAllChildViews(vp.getChildAt(i), level);
                }
            }
        }
    }
}
