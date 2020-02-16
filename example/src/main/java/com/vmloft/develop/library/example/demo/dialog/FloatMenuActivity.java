package com.vmloft.develop.library.example.demo.dialog;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.widget.VMFloatMenu;
import com.vmloft.develop.library.tools.widget.toast.VMToast;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by lzan13 on 2017/6/20.
 *
 * 测试悬浮菜单
 */
public class FloatMenuActivity extends AppActivity {

    @BindView(R.id.float_menu_left_top_btn) Button leftTopBtn;
    @BindView(R.id.float_menu_left_bottom_btn) Button leftBottomBtn;
    @BindView(R.id.float_menu_center_btn) Button centerBtn;
    @BindView(R.id.float_menu_right_top_btn) Button rightTopBtn;
    @BindView(R.id.float_menu_right_bottom_btn) Button rightBottomBtn;

    private int touchX;
    private int touchY;
    private VMFloatMenu mFloatMenu;

    @Override
    protected int layoutId() {
        return R.layout.activity_float_menu;
    }

    @Override
    protected void initUI() {
        super.initUI();
        mFloatMenu = new VMFloatMenu(mActivity);
        mFloatMenu.setItemClickListener(id -> {
            VMLog.d("点击了悬浮菜单 %d", id);
            VMToast.make(mActivity, "点击了悬浮菜单 " + id).done();
        });
    }

    @Override
    protected void initData() {

    }

    @OnTouch({
        R.id.float_menu_left_top_btn, R.id.float_menu_right_top_btn, R.id.float_menu_center_btn, R.id.float_menu_left_bottom_btn,
        R.id.float_menu_right_bottom_btn
    })
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = (int) event.getRawX();
            touchY = (int) event.getRawY();
        }
        return false;
    }

    @OnClick({
        R.id.float_menu_left_top_btn, R.id.float_menu_right_top_btn, R.id.float_menu_center_btn, R.id.float_menu_left_bottom_btn,
        R.id.float_menu_right_bottom_btn
    })
    void onClick(View view) {
        switch (view.getId()) {
        case R.id.float_menu_left_top_btn:
        case R.id.float_menu_left_bottom_btn:
        case R.id.float_menu_center_btn:
        case R.id.float_menu_right_top_btn:
        case R.id.float_menu_right_bottom_btn:
            showFloatMenu(view);
            break;
        }
    }

    /**
     * 测试弹出 PopupWindow 菜单
     */
    private void showFloatMenu(View view) {
        mFloatMenu.clearAllItem();
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(1, "悬浮菜单"));
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(2, "悬浮"));
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(3, "悬浮菜单", R.color.vm_red));
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(4, "悬浮菜单菜单"));
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(5, "悬浮菜单"));
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(6, "菜单"));
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(7, "悬浮菜单"));
        mFloatMenu.addItem(new VMFloatMenu.ItemBean(8, "浮菜单"));
        mFloatMenu.showAtLocation(view, touchX, touchY);
    }
}
