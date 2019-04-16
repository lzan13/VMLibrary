package com.vmloft.develop.library.tools.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.vmloft.develop.library.tools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 2018/3/21
 *
 * 可以伸缩布局控件
 */
public class VMExpandableLayout extends LinearLayout {

    public static final String KEY_SUPER_STATE = "super_state";
    public static final String KEY_EXPANDED = "expanded";

    private static final int DEFAULT_DURATION = 300;

    private int wms;
    private int hms;

    private List<View> expandableViews;

    private int duration = DEFAULT_DURATION;
    private boolean expanded = false;

    private Interpolator interpolator = new FastOutSlowInInterpolator();
    private AnimatorSet animatorSet;

    private OnExpansionUpdateListener listener;

    public VMExpandableLayout(Context context) {
        this(context, null);
    }

    public VMExpandableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleAttrs(context, attrs);
        expandableViews = new ArrayList<>();

        // We only support vertical layouts for now
        setOrientation(VERTICAL);
    }

    /**
     * 获取资源属性
     *
     * @param context
     * @param attrs
     */
    private void handleAttrs(Context context, AttributeSet attrs) {
        // 获取控件的属性值
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VMExpandableLayout);
        duration = a.getInt(R.styleable.VMExpandableLayout_vm_el_duration, DEFAULT_DURATION);
        expanded = a.getBoolean(R.styleable.VMExpandableLayout_vm_el_expanded, false);
        a.recycle();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();

        bundle.putBoolean(KEY_EXPANDED, expanded);
        bundle.putParcelable(KEY_SUPER_STATE, superState);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        expanded = bundle.getBoolean(KEY_EXPANDED);
        Parcelable superState = bundle.getParcelable(KEY_SUPER_STATE);

        for (View expandableView : expandableViews) {
            expandableView.setVisibility(expanded ? VISIBLE : GONE);
        }

        super.onRestoreInstanceState(superState);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        LayoutParams lp = (LayoutParams) params;
        if (lp.expandable) {
            expandableViews.add(child);
            child.setVisibility(expanded ? VISIBLE : GONE);
        }

        super.addView(child, index, params);
    }

    @Override
    public void removeView(View child) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp.expandable) {
            expandableViews.remove(child);
        }

        super.removeView(child);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        wms = widthMeasureSpec;
        hms = heightMeasureSpec;
    }

    @Override
    public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        super.onConfigurationChanged(newConfig);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        if (expanded) {
            collapse(animate);
        } else {
            expand(animate);
        }
    }

    public void expand() {
        expand(true);
    }

    @SuppressLint("WrongCall")
    public void expand(boolean animate) {
        if (expanded) {
            return;
        }

        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet = null;
        }

        expanded = true;

        for (View expandableView : expandableViews) {
            LayoutParams lp = (LayoutParams) expandableView.getLayoutParams();

            // Calculate view's original height
            expandableView.setVisibility(View.VISIBLE);
            lp.weight = lp.originalWeight;
            lp.height = lp.originalHeight;
            super.onMeasure(wms, hms);
        }

        for (View expandableView : expandableViews) {
            if (animate) {
                animateHeight(expandableView, expandableView.getMeasuredHeight());
            } else {
                setHeight(expandableView, expandableView.getMeasuredHeight());
            }
        }

        if (animatorSet != null && animate) {
            animatorSet.start();
        }
    }

    public void collapse() {
        collapse(true);
    }

    public void collapse(boolean animate) {
        if (!expanded) {
            return;
        }

        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet = null;
        }

        expanded = false;

        for (View expandableView : expandableViews) {
            if (animate) {
                animateHeight(expandableView, 0);
            } else {
                setHeight(expandableView, 0);
            }
        }

        if (animatorSet != null && animate) {
            animatorSet.start();
        }
    }

    public void setOnExpansionUpdateListener(OnExpansionUpdateListener listener) {
        this.listener = listener;
    }

    private void animateHeight(final View view, final int targetHeight) {
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(interpolator);
            animatorSet.setDuration(duration);
        }

        final LayoutParams lp = (LayoutParams) view.getLayoutParams();
        lp.weight = 0;
        int height = view.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(height, targetHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.getLayoutParams().height = (Integer) valueAnimator.getAnimatedValue();
                view.requestLayout();

                if (listener != null) {
                    float fraction = targetHeight == 0 ? 1 - valueAnimator.getAnimatedFraction() : valueAnimator.getAnimatedFraction();
                    listener.onExpansionUpdate(fraction);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (targetHeight == 0) {
                    view.setVisibility(GONE);
                } else {
                    lp.height = lp.originalHeight;
                    lp.weight = lp.originalWeight;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animatorSet.playTogether(animator);
    }

    private void setHeight(View view, int targetHeight) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();

        if (targetHeight == 0) {
            view.setVisibility(GONE);
        } else {
            lp.height = lp.originalHeight;
            lp.weight = lp.originalWeight;

            view.requestLayout();
        }

        if (listener != null) {
            listener.onExpansionUpdate(targetHeight == 0 ? 0f : 1f);
        }
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {

        private final boolean expandable;
        private final int originalHeight;
        private final float originalWeight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.VMExpandableLayout);
            expandable = a.getBoolean(R.styleable.VMExpandableLayout_vm_el_expandable, false);
            originalHeight = this.height;
            originalWeight = this.weight;
            a.recycle();
        }
    }

    public interface OnExpansionUpdateListener {

        void onExpansionUpdate(float expansionFraction);
    }
}