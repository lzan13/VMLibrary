package com.vmloft.develop.library.example.demo.details;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.adapter.VMAdapter;

import com.vmloft.develop.library.tools.utils.VMSystem;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lzan13 on 2017/5/12.
 * 展示自定义可变控件界面
 */
public class DetailsActivity extends AppActivity {
    //@BindView(R.id.view_details) VMDetailsView mDetailsView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    //private VMWrapper<DetailsEntity> mWrapper;
    private DetailsAdapter mAdapter;
    private List<DetailsEntity> mDetailsList = new ArrayList<>();

    @Override
    protected int layoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void initUI() {
        super.initUI();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadData();
        });

        mAdapter = new DetailsAdapter(mActivity, mDetailsList);

        // 添加 Header View
        View headerView = LayoutInflater.from(mActivity).inflate(R.layout.details_header_view, null);
        mAdapter.addHeaderView(headerView);

        // 添加空数据部分
        View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.details_empty_view, null);
        mAdapter.setEmptyView(emptyView);

        // 添加 Footer View
        View footerView = LayoutInflater.from(mActivity).inflate(R.layout.details_footer_view, null);
        mAdapter.addFooterView(footerView);

        // 添加 More View
        View moreView = LayoutInflater.from(mActivity).inflate(R.layout.details_more_view, null);
        mAdapter.setMoreView(moreView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);

        initItemListener();
        //loadData();
    }

    @Override
    protected void initData() {

    }

    /**
     * 设置 Item 项监听
     */
    private void initItemListener() {
        mAdapter.setClickListener((VMAdapter.IClickListener<DetailsEntity>) (action, detailsEntity) -> {
            Toast.makeText(mActivity, "点击", Toast.LENGTH_SHORT).show();

            if (detailsEntity.isFold()) {
                detailsEntity.setFold(false);
            } else {
                detailsEntity.setFold(true);
            }
        });
        mAdapter.setMoreListener(() -> loadData());
    }

    private void loadData() {
        for (int i = 0; i < 30; i++) {
            DetailsEntity entity = new DetailsEntity();
            if (i % 3 == 0) {
                entity.setContent(i + " - 这里是列表内的可折叠控件内容显示，这里测试多个 view 列表滚动时的情况，水电费是方");
            } else if (i % 2 == 0) {
                entity.setContent(i + " - 这里是单行内容，测试多个 view 列表滚动时的情况");
            } else {
                entity.setContent(i + " - 这里是列表内的可折叠控件内容显示，这里测试多个 view 列表滚动时的情况，水电费是方法及违法少妇了说的分开件违反收代理费就死定了附件为佛山及地方了收到了附件是丹佛微积分，这里测试多个 " + "view 列表滚动时的情况，水电费是方法及违法少妇了说的分开件违反收代理费就死定了附件为佛山及地方了收到了附件是丹佛微积分");
            }
            entity.setFold(true);
            mDetailsList.add(entity);
        }
        mSwipeRefreshLayout.setRefreshing(false);
        refresh();
    }

    private void refresh() {
        VMSystem.runInUIThread(() -> {mAdapter.refresh(mDetailsList);});
    }
}
