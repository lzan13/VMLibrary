package com.vmloft.develop.library.example.shell;

import com.vmloft.develop.library.tools.utils.VMLog;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lzan13 on 2018/5/2.
 */
public class RootShellCmd {
    private static DataOutputStream os;

    /**
     * 初始化获取 root 权限
     */
    public static void initRootPermission() {
        try {
            if (os == null) {
                Process process = Runtime.getRuntime().exec("su");
                os = new DataOutputStream(process.getOutputStream());
            }
        } catch (IOException e) {
            //                e.printStackTrace();
            VMLog.e("获取 Root 权限失败！ %s", e.getMessage());
        }
    }

    /**
     * 执行 shell 命令
     *
     * @param cmd shell 命令
     */
    public static boolean exec(String cmd) {
        try {
            initRootPermission();
            if (os != null) {
                os.writeBytes(cmd);
                os.flush();
                VMLog.i("Shell 命令执行成功！%s", cmd);
                return true;
            } else {
                VMLog.e("Shell 命令执行失败！%s", cmd);
                return false;
            }
        } catch (IOException e) {
            //            e.printStackTrace();
            VMLog.e("Shell 命令执行失败！%s", e.getMessage());
        }
        return false;
    }

    /**
     * 模拟按键
     *
     * @param keyCode 按键值
     */
    public static boolean simulateKeyEvent(int keyCode) {
        return exec(String.format("input keyevent %d \n", keyCode));
    }

    /**
     * 模拟屏幕点击
     *
     * @param x 点击位置 x 坐标
     * @param y 点击位置 y 坐标
     */
    public static boolean simulateTap(int x, int y) {
        return exec(String.format("input tap %d %d \n", x, y));
    }

    /**
     * 使用滑动方法加上持续时间模拟长按 拖动
     *
     * @param startX 长按开始 x 坐标
     * @param startY 长按开始 y 坐标
     * @param endX 滑动结束 x 坐标
     * @param endY 滑动结束 y 坐标
     * @param duration 长按持续时间
     */
    public static boolean simulateLong(int startX, int startY, int endX, int endY, int duration) {
        return exec(
            String.format("input swipe %d %d %d %d %d", startX, startY, startX, startY, duration));
        //simulateSwipe(startX, startY, endX, endY);
        //return true;
    }

    /**
     * 模拟屏幕滑动
     *
     * @param startX 滑动开始 x 坐标
     * @param startY 滑动开始 y 坐标
     * @param endX 滑动结束 x 坐标
     * @param endY 滑动结束 y 坐标
     */
    public static boolean simulateSwipe(int startX, int startY, int endX, int endY) {
        return exec(String.format("input swipe %d %d %d %d\n", startX, startY, endX, endY));
    }

    /**
     * 模拟输入文本
     *
     * @param text 需要输入的文本内容
     */
    public static boolean simulateInputText(String text) {
        return exec(String.format("input text '%s' \n", text));
    }

    /**
     * 鼠标按下
     */
    public static boolean mouseClick() {
        exec("sendevent /dev/input/event6 2 0 100");    // 移动 x 坐标
        exec("sendevent /dev/input/event6 2 1 200");    // 移动 y 坐标
        exec("sendevent /dev/input/event6 0 0 0");      // 同步（必须）
        exec("sendevent /dev/input/event6 1 272 1");    // 鼠标按下
        exec("sendevent /dev/input/event6 0 0 0");      // 同步（必须）
        exec("sendevent /dev/input/event6 1 272 0");    // 鼠标抬起
        exec("sendevent /dev/input/event6 0 0 0");      // 同步（必须）
        return true;
    }
}
