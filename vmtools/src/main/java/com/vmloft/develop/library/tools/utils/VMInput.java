package com.vmloft.develop.library.tools.utils;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lzan13 on 2018/5/3.
 */
public class VMInput {

    private static float default_size = 0.15f;
    private static float default_pressure = 0.15f;

    private static VMInput instance;

    private VMInput() {}

    public static VMInput getInstance() {
        if (instance == null) {
            instance = new VMInput();
        }
        return instance;
    }

    /**
     * Convert the characters of string text into key event's and send to
     * device.
     *
     * @param text is a string of characters you want to input to the device.
     */
    public void sendText(String text) {
        int inputSource = InputDevice.SOURCE_KEYBOARD;
        StringBuffer buff = new StringBuffer(text);
        boolean escapeFlag = false;
        for (int i = 0; i < buff.length(); i++) {
            if (escapeFlag) {
                escapeFlag = false;
                if (buff.charAt(i) == 's') {
                    buff.setCharAt(i, ' ');
                    buff.deleteCharAt(--i);
                }
            }
            if (buff.charAt(i) == '%') {
                escapeFlag = true;
            }
        }

        char[] chars = buff.toString()
                           .toCharArray();

        KeyCharacterMap kcm = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
        KeyEvent[] events = kcm.getEvents(chars);
        for (int i = 0; i < events.length; i++) {
            KeyEvent e = events[i];
            if (inputSource != e.getSource()) {
                e.setSource(inputSource);
            }
            injectKeyEvent(e);
        }
    }

    public void sendKeyEvent(int keyCode, boolean longpress) {
        int inputSource = InputDevice.SOURCE_KEYBOARD;
        long now = SystemClock.uptimeMillis();
        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, inputSource));
        if (longpress) {
            injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 1, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0, KeyEvent.FLAG_LONG_PRESS, inputSource));
        }
        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, 0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, inputSource));
    }

    public void sendTap(float x, float y) {
        int inputSource = InputDevice.SOURCE_TOUCHSCREEN;
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x, y, default_pressure);
        injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x, y, 0.0f);
    }

    public void sendSwipe(float x1, float y1, float x2, float y2, int duration, boolean partial) {
        int inputSource = InputDevice.SOURCE_TOUCHSCREEN;
        if (duration < 0) {
            duration = 300;
        }
        long now = SystemClock.uptimeMillis();
        if (!partial) {
            injectMotionEvent(inputSource, MotionEvent.ACTION_DOWN, now, x1, y1, default_pressure);
        }

        long startTime = now;
        long endTime = startTime + duration;
        while (now < endTime) {
            long elapsedTime = now - startTime;
            float alpha = (float) elapsedTime / duration;
            injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, lerp(x1, x2, alpha), lerp(y1, y2, alpha), default_pressure);
            now = SystemClock.uptimeMillis();
        }
        if (!partial) {
            injectMotionEvent(inputSource, MotionEvent.ACTION_UP, now, x2, y2, 0.0f);
        }
    }

    /**
     * Sends a simple zero-pressure move event.
     *
     * @param inputSource the InputDevice.SOURCE_* sending the input event
     * @param dx change in x coordinate due to move
     * @param dy change in y coordinate due to move
     */
    public void sendMove(float dx, float dy) {
        int inputSource = InputDevice.SOURCE_TRACKBALL;
        long now = SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, MotionEvent.ACTION_MOVE, now, dx, dy, 0.0f);
    }

    private void injectKeyEvent(KeyEvent event) {
        injectEvent(event);
    }

    private void injectEvent(InputEvent event) {
        try {
            VMLog.d("injectKeyEvent: " + event);
            Field inject_input_event_mode_wait_for_finish = InputManager.class.getField("INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH");
            Method inst = InputManager.class.getDeclaredMethod("getInstance");
            InputManager im = (InputManager) (inst.invoke(null));

            Method injectInputEvent = InputManager.class.getDeclaredMethod("injectInputEvent", InputEvent.class, Integer.TYPE);
            injectInputEvent.invoke(im, event, inject_input_event_mode_wait_for_finish.getInt(null));

        } catch (Throwable e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Builds a MotionEvent and injects it into the event stream.
     *
     * @param inputSource the InputDevice.SOURCE_* sending the input event
     * @param action the MotionEvent.ACTION_* for the event
     * @param when the value of SystemClock.uptimeMillis() at which the event happened
     * @param x x coordinate of event
     * @param y y coordinate of event
     * @param pressure pressure of event
     */
    private void injectMotionEvent(int inputSource, int action, long when, float x, float y,
            float pressure) {
        final float DEFAULT_SIZE = default_size;
        final int DEFAULT_META_STATE = 0;
        final float DEFAULT_PRECISION_X = 1.0f;
        final float DEFAULT_PRECISION_Y = 1.0f;
        final int DEFAULT_DEVICE_ID = 0;
        final int DEFAULT_EDGE_FLAGS = 0;
        MotionEvent event = MotionEvent.obtain(when, when, action, x, y, pressure, DEFAULT_SIZE, DEFAULT_META_STATE, DEFAULT_PRECISION_X, DEFAULT_PRECISION_Y, DEFAULT_DEVICE_ID, DEFAULT_EDGE_FLAGS);
        event.setSource(inputSource);
        VMLog.d("injectMotionEvent: " + event);
        injectEvent(event);
    }

    private static final float lerp(float a, float b, float alpha) {
        return (b - a) * alpha + a;
    }

    private static final int getSource(int inputSource, int defaultSource) {
        return inputSource == InputDevice.SOURCE_UNKNOWN ? defaultSource : inputSource;
    }

}
