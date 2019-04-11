package com.vmloft.develop.library.example.demo.indicator;

import android.os.Bundle;
import android.widget.TextView;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppLazyFragment;

import butterknife.BindView;

/**
 * Create by lzan13 on 2019/04/09
 *
 * 通用界面 Fragment 实现容器
 */
public class IndicatorFragment extends AppLazyFragment {

    private static final String ARG_CONTENT_ID = "arg_content_id";

    @BindView(R.id.default_title)
    TextView titleView;

    private String mContent;

    /**
     * Fragment 的工厂方法，方便创建并设置参数
     */
    public static IndicatorFragment newInstance(String content) {
        IndicatorFragment fragment = new IndicatorFragment();

        Bundle args = new Bundle();
        args.putString(ARG_CONTENT_ID, content);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_default;
    }

    @Override
    protected void initData() {
        mContent = getArguments().getString(ARG_CONTENT_ID);
        titleView.setText(mContent);
    }

}
