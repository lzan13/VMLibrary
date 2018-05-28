package com.vmloft.develop.library.tools.shell;

import com.vmloft.develop.library.tools.shell.exceptions.RootDeniedException;
import com.vmloft.develop.library.tools.shell.execution.Command;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * root 命令工具类
 */
public class VMRShell {
    private static final int EV_SYN = 0;
    private static final int EV_KEY = 1;
    private static final int EV_ABS = 3;

    private static final int DOWN = 1;
    private static final int UP = 0;

    private static final int BTN_TOUCH = 330;
    private static final int BTN_TOOL_FINGER = 325;

    private static final int KEY_VOLUMEDOWN = 114;
    private static final int KEY_VOLUMEUP = 115;
    private static final int KEY_POWER = 116;

    private static final int ABS_MT_TRACKING_ID = 57;
    private static final int ABS_MT_POSITION_X = 53;
    private static final int ABS_MT_POSITION_Y = 54;
    private static final int ABS_MT_TOUCH_MAJOR = 48;
    private static final int SYN_REPORT = 0;

    public static void execCommand(String cmd) {
        Command command = new Command(0, cmd);
        try {
            RootShell.getShell(true).add(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 模拟按键
     *
     * @param keyCode 按键值
     */
    public static void inputKeyEventk(int keyCode) {
        execCommand(String.format("input keyevent %d \n", keyCode));
    }

    /**
     * 模拟屏幕点击
     *
     * @param x 点击位置 x 坐标
     * @param y 点击位置 y 坐标
     */
    public static void inputTap(int x, int y) {
        execCommand(String.format("input tap %d %d \n", x, y));
    }

    /**
     * 模拟屏幕滑动
     *
     * @param startX 滑动开始 x 坐标
     * @param startY 滑动开始 y 坐标
     * @param endX 滑动结束 x 坐标
     * @param endY 滑动结束 y 坐标
     */
    public static void inputSwipe(int startX, int startY, int endX, int endY) {
        execCommand(String.format("input swipe %d %d %d %d\n", startX, startY, endX, endY));
    }

    /**
     * 模拟输入文本
     *
     * @param text 需要输入的文本内容
     */
    public static void inputText(String text) {
        execCommand(String.format("input text '%s' \n", text));
    }

    /**
     * 格式化 sendevent 类型的命令
     *
     * @param eventName 传感器对应事件名称
     * @param ev 事件类型
     * @param key 事件操作 key
     * @param value 事件值
     * @return
     */
    public static String formatSendEvent(String eventName, int ev, int key, int value) {
        return String.format("sendevent /dev/input/%s %d %d %d", eventName, ev, key, value);
    }

    public static void volumeUp() {
        execCommand(formatSendEvent("event1", EV_KEY, KEY_VOLUMEUP, DOWN));
        execCommand(formatSendEvent("event1", EV_SYN, SYN_REPORT, 0));
        execCommand(formatSendEvent("event1", EV_KEY, KEY_VOLUMEUP, UP));
        execCommand(formatSendEvent("event1", EV_SYN, SYN_REPORT, 0));
    }

    public static void volumeDown() {
        execCommand(formatSendEvent("event0", EV_KEY, KEY_VOLUMEDOWN, DOWN));
        execCommand(formatSendEvent("event0", EV_SYN, SYN_REPORT, 0));
        execCommand(formatSendEvent("event0", EV_KEY, KEY_VOLUMEDOWN, UP));
        execCommand(formatSendEvent("event0", EV_SYN, SYN_REPORT, 0));
    }

    public static void powerKey() {
        execCommand(formatSendEvent("event0", EV_KEY, KEY_POWER, DOWN));
        execCommand(formatSendEvent("event0", EV_SYN, SYN_REPORT, 0));
        execCommand(formatSendEvent("event0", EV_KEY, KEY_POWER, UP));
        execCommand(formatSendEvent("event0", EV_SYN, SYN_REPORT, 0));
    }

    public static void touchDown(int x, int y, int index) {
        execCommand(formatSendEvent("event2", EV_ABS, ABS_MT_TRACKING_ID, index));
        execCommand(formatSendEvent("event2", EV_KEY, BTN_TOUCH, DOWN));
        execCommand(formatSendEvent("event2", EV_KEY, BTN_TOOL_FINGER, DOWN));
        execCommand(formatSendEvent("event2", EV_ABS, ABS_MT_POSITION_X, x));
        execCommand(formatSendEvent("event2", EV_ABS, ABS_MT_POSITION_Y, y));
        execCommand(formatSendEvent("event2", EV_ABS, ABS_MT_TOUCH_MAJOR, 8));
        execCommand(formatSendEvent("event2", EV_SYN, SYN_REPORT, 0));
    }

    public static void touchUp() {
        execCommand(formatSendEvent("event2", EV_ABS, ABS_MT_TRACKING_ID, -1));
        execCommand(formatSendEvent("event2", EV_KEY, BTN_TOUCH, UP));
        execCommand(formatSendEvent("event2", EV_KEY, BTN_TOOL_FINGER, UP));
        execCommand(formatSendEvent("event2", EV_SYN, SYN_REPORT, 0));
    }
}
