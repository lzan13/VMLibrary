package com.vmloft.develop.library.tools.picker.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.vmloft.develop.library.tools.R;

/**
 * Create by lzan13 on 2019/05/19 09:30
 *
 * 自定义 PopupWindow，实现弹出文件夹列表
 */
public class FolderPopupWindow extends PopupWindow implements View.OnClickListener {

    private ListView listView;
    private OnItemClickListener onItemClickListener;
    private final View masker;
    private final View marginView;
    private int marginPx;

    public FolderPopupWindow(Context context, BaseAdapter adapter) {
        super(context);

        final View view = View.inflate(context, R.layout.vm_pick_folder_list_window, null);
        masker = view.findViewById(R.id.vm_pick_folder_mask_ll);
        masker.setOnClickListener(this);
        marginView = view.findViewById(R.id.margin);
        marginView.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.vm_pick_folder_lv);
        listView.setAdapter(adapter);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);  //如果不设置，就是 AnchorView 的宽度
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(0);
        view.getViewTreeObserver()
            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int maxHeight = view.getHeight() * 5 / 8;
                    int realHeight = listView.getHeight();
                    ViewGroup.LayoutParams listParams = listView.getLayoutParams();
                    listParams.height = realHeight > maxHeight ? maxHeight : realHeight;
                    listView.setLayoutParams(listParams);
                    LinearLayout.LayoutParams marginParams = (LinearLayout.LayoutParams) marginView.getLayoutParams();
                    marginParams.height = marginPx;
                    marginView.setLayoutParams(marginParams);
                    enterAnimator();
                }
            });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, position, l);
                }
            }
        });
    }

    private void enterAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", listView.getHeight(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    @Override
    public void dismiss() {
        exitAnimator();
    }

    private void exitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(listView, "translationY", 0, listView.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                FolderPopupWindow.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setSelection(int selection) {
        listView.setSelection(selection);
    }

    public void setMargin(int marginPx) {
        this.marginPx = marginPx;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> adapterView, View view, int position, long l);
    }
}
