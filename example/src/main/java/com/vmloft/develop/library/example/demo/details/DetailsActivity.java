package com.vmloft.develop.library.example.demo.details;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.adapter.VMAdapter;

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
    protected void init() {

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);

        initItemListener();
        //loadData();
    }

    /**
     * 设置 Item 项监听
     */
    private void initItemListener() {
        mAdapter.setClickListener(new VMAdapter.IClickListener<DetailsEntity>() {
            @Override
            public void onItemClick(int action, DetailsEntity detailsEntity) {
                Toast.makeText(mActivity, "点击", Toast.LENGTH_SHORT).show();

                if (detailsEntity.isFold()) {
                    detailsEntity.setFold(false);
                } else {
                    detailsEntity.setFold(true);
                }
            }
        });
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
        mAdapter.refresh(mDetailsList);
    }
}
