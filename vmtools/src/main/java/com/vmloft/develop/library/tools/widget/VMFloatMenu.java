package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMTheme;

import java.util.List;

/**
 * Create by lzan13 on 2019/05/28 19:33
 *
 * 自定义实现悬浮菜单，可以跟随按下位置他拿出，同时适配屏幕大小
 */
public class VMFloatMenu extends PopupWindow {

    private final static int SHOW_ON_LEFT = 0X11;
    private final static int SHOW_ON_RIGHT = 0X12;
    private final static int SHOW_ON_UP = 0X13;
    private final static int SHOW_ON_DOWN = 0X14;

    private Context mContext;

    private View mContentView;
    private LinearLayout mItemContainer;

    private int showAtVertical, showAtOrientation;
    // 菜单数量
    private int mItemCount = 0;
    private int mItemPadding = 0;

    private IItemClickListener listener;

    public VMFloatMenu(Context context) {
        super(context);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.vm_widget_float_menu, null);
        mItemContainer = mContentView.findViewById(R.id.vm_float_menu_container);
        setContentView(mContentView);

        mItemPadding = VMDimen.dp2px(16);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setInputMethodMode(INPUT_METHOD_NOT_NEEDED);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.color.vm_transparent);
        setBackgroundDrawable(drawable);
        VMTheme.changeShadow(mItemContainer);
    }

    /**
     * 显示悬浮菜单在指定位置，显示之前需要根据菜单的高度进行重新计算菜单位置
     */
    public void showAtLocation(View view, int positionX, int positionY) {
        if (mItemCount == 0) {
            return;
        }
        int screenW = VMDimen.getScreenSize(mContext).x;
        int screenH = VMDimen.getScreenSize(mContext).y;
        // 计算悬浮菜单显示区域
        mContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int windowsHeight = mContentView.getMeasuredHeight();
        int windowsWidth = mContentView.getMeasuredWidth();
        // 菜单弹出显示的最终坐标
        int x = positionX, y = positionY;

        if (screenH - positionY < windowsHeight) {
            // 向上弹出
            y = positionY - windowsHeight;
            showAtVertical = SHOW_ON_UP;
        } else {
            //向下弹出
            showAtVertical = SHOW_ON_DOWN;
        }

        if (screenW - positionX < windowsWidth || positionX > screenW / 5 * 3) {
            // 左弹出
            x = positionX - windowsWidth;
            showAtOrientation = SHOW_ON_LEFT;
        } else {
            // 右弹出
            showAtOrientation = SHOW_ON_RIGHT;
        }
        setMenuAnim();
        showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }

    /**
     * 设置悬浮菜单在触摸位置弹出动画
     */
    public void setMenuAnim() {
        if (showAtOrientation == SHOW_ON_RIGHT && showAtVertical == SHOW_ON_UP) {
            setAnimationStyle(R.style.VMFloatMenuLB);
        }

        if (showAtOrientation == SHOW_ON_RIGHT && showAtVertical == SHOW_ON_DOWN) {
            setAnimationStyle(R.style.VMFloatMenuLT);
        }

        if (showAtOrientation == SHOW_ON_LEFT && showAtVertical == SHOW_ON_UP) {
            setAnimationStyle(R.style.VMFloatMenuRB);
        }

        if (showAtOrientation == SHOW_ON_LEFT && showAtVertical == SHOW_ON_DOWN) {
            setAnimationStyle(R.style.VMFloatMenuRT);
        }
    }

    /**
     * 清空之前添加的 Item
     */
    public void clearAllItem() {
        mItemContainer.removeAllViews();
        mItemCount = 0;
    }

    /**
     * 直接添加一个集合
     */
    public void addItemList(List<ItemBean> items) {
        for (ItemBean bean : items) {
            addItem(bean);
        }
    }

    /**
     * 添加菜单项
     */
    public void addItem(ItemBean bean) {
        TextView itemView = new TextView(mContext);
        itemView.setId(bean.itemId);
        itemView.setText(bean.itemTitle);
        itemView.setPadding(mItemPadding, mItemPadding, mItemPadding * 2, mItemPadding);
        itemView.setTextColor(ContextCompat.getColor(mContext, bean.itemColor));
        itemView.setBackgroundResource(R.drawable.vm_click_full_transparent);

        mItemContainer.addView(itemView);
        mItemCount++;

        setItemClick(itemView);
    }

    /**
     * 设置菜单项点击事件
     */
    private void setItemClick(View view) {
        view.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onItemClick(v.getId());
            }
        });
    }

    /**
     * 菜单项的数据 Bean 类
     */
    public static class ItemBean {
        public int itemId;
        public String itemTitle;
        public int itemColor;

        public ItemBean(int id, String title) {
            this.itemId = id;
            this.itemTitle = title;
            this.itemColor = R.color.vm_black_87;
        }

        public ItemBean(int id, String title, int color) {
            this.itemId = id;
            this.itemTitle = title;
            this.itemColor = color;
        }
    }

    /**
     * 定义悬浮菜单点击监听接口
     */
    public interface IItemClickListener {
        void onItemClick(int id);
    }

    public void setItemClickListener(IItemClickListener listener) {
        this.listener = listener;
    }
}
