package com.vmloft.develop.library.simple.popup;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;

/**
 * Created by lzan13 on 2017/6/20.
 * 测试 PopupWindow 界面
 */
public class VMPopupWindowActivity extends VMBaseActivity {

    private VMPopupWindowActivity activity;

    @BindView(R.id.btn_popup_window_top_left) Button topLeftBtn;
    @BindView(R.id.btn_popup_window_top_right) Button topRightBtn;
    @BindView(R.id.btn_popup_window_center) Button centerBtn;
    @BindView(R.id.btn_popup_window_bottom_left) Button bottomLeftBtn;
    @BindView(R.id.btn_popup_window_bottom_right) Button bottomRightBtn;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        activity = this;
        ButterKnife.bind(activity);

        initView();
    }

    private void initView() {

    }

    @OnClick({
            R.id.btn_popup_window_top_left, R.id.btn_popup_window_top_right, R.id.btn_popup_window_center,
            R.id.btn_popup_window_bottom_left, R.id.btn_popup_window_bottom_right
    }) void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_popup_window_top_left:
                loadTopLeftPopupWindow(topLeftBtn);
                break;
            case R.id.btn_popup_window_center:
                loadTopLeftPopupWindow(centerBtn);
                break;
            case R.id.btn_popup_window_bottom_left:
                loadBottomLeftPopupWindow(bottomLeftBtn);
                break;
            case R.id.btn_popup_window_bottom_right:
                loadBottomRightPopupWindow(bottomRightBtn);
                break;
        }
    }

    /**
     * 测试弹出 PopupWindow 菜单
     */
    private void loadTopLeftPopupWindow(View view) {
        PopupWindow popupWindow = new PopupWindow();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 3; i++) {
            Button btn = new Button(activity);
            btn.setText("Popup window button " + i);
            linearLayout.addView(btn);
        }
        popupWindow.setContentView(linearLayout);
        popupWindow.setWidth(view.getWidth());
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
        popupWindow.setOutsideTouchable(true);
        //popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.showAsDropDown(view, 0, popupWindow.getHeight());
    }

    /**
     * 测试弹出 PopupWindow 菜单
     */
    private void loadBottomLeftPopupWindow(View view) {
        PopupWindow popupWindow = new PopupWindow();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 3; i++) {
            Button btn = new Button(activity);
            btn.setText("Popup window button " + i);
            linearLayout.addView(btn);
        }
        popupWindow.setContentView(linearLayout);
        popupWindow.setWidth(view.getWidth());
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x232323));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.LEFT, view.getLeft(), view.getHeight());
        //popupWindow.showAsDropDown(view, 0, popupWindow.getHeight());
    }

    /**
     * 测试弹出 PopupWindow 菜单
     */
    private void loadBottomRightPopupWindow(View view) {
        PopupWindow popupWindow = new PopupWindow();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 3; i++) {
            Button btn = new Button(activity);
            btn.setText("Popup window button " + i);
            linearLayout.addView(btn);
        }
        popupWindow.setContentView(linearLayout);
        popupWindow.setWidth(view.getWidth());
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.LEFT, view.getLeft(), view.getHeight());
        //popupWindow.showAsDropDown(view, 0, popupWindow.getHeight());
    }
}
