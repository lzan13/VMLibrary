package com.vmloft.develop.library.simple.details;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.widget.VMDetailsView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzan13 on 2017/5/12.
 * 展示自定义可变控件界面
 */
public class VMDetailsActivity extends VMActivity {

    private LinearLayoutManager layoutManager;
    private VMDetailsAdapter adapter;
    private RecyclerView recyclerView;
    private List<VMDetailsEntity> detailsList = new ArrayList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        VMDetailsView detailsView = (VMDetailsView) findViewById(R.id.view_details);
        detailsView.setContentText("下边是一个列表显示多个可以折叠的 TextView，测试性能！这简单测试普通设置");

        detailsView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(VMDetailsActivity.this, "测试外部设置点击事件", Toast.LENGTH_LONG).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        for (int i = 0; i < 150; i++) {
            VMDetailsEntity entity = new VMDetailsEntity();
            if (i % 3 == 0) {
                entity.setContent(i + " - 这里是列表内的可折叠控件内容显示，这里测试多个 view 列表滚动时的情况，水电费是方");
            } else if (i % 2 == 0) {
                entity.setContent(i + " - 这里是单行内容，测试多个 view 列表滚动时的情况");
            } else {
                entity.setContent(i
                        + " - 这里是列表内的可折叠控件内容显示，这里测试多个 view 列表滚动时的情况，水电费是方法及违法少妇了说的分开件违反收代理费就死定了附件为佛山及地方了收到了附件是丹佛微积分，这里测试多个 view 列表滚动时的情况，水电费是方法及违法少妇了说的分开件违反收代理费就死定了附件为佛山及地方了收到了附件是丹佛微积分");
            }
            entity.setFold(true);
            detailsList.add(entity);
        }

        adapter = new VMDetailsAdapter(this, detailsList);
        recyclerView.setAdapter(adapter);

        initItemListener();
    }

    /**
     * 设置 Item 项监听
     */
    private void initItemListener() {
        adapter.setItemListener(new VMDetailsAdapter.ItemListener() {
            @Override public void onItemClick(View view, int position) {
                VMDetailsEntity detailsEntity = detailsList.get(position);
                if (detailsEntity.isFold()) {
                    detailsEntity.setFold(false);
                } else {
                    detailsEntity.setFold(true);
                }
            }
        });
    }
}
