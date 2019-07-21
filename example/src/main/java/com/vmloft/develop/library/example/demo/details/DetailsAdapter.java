package com.vmloft.develop.library.example.demo.details;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.adapter.VMHolder;
import com.vmloft.develop.library.tools.widget.VMDetailsView;

import java.util.List;

/**
 * Created by lzan13 on 2017/3/29.
 *
 * 历史通话适配器
 */
public class DetailsAdapter extends VMAdapter<DetailsEntity, DetailsAdapter.DataHolder> {

    public DetailsAdapter(Context context, List<DetailsEntity> list) {
        super(context, list);
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.details_list_item, parent, false);
        DataHolder viewHolder = new DataHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final DetailsEntity detailsEntity = getItemData(position);

        holder.mDetailsView.setFold(detailsEntity.isFold());
        holder.mDetailsView.setContentText(detailsEntity.getContent());
    }

    /**
     * 自定义会话列表项的 ViewHolder 用来显示会话列表项的内容
     */
    static class DataHolder extends VMHolder {

        VMDetailsView mDetailsView;

        /**
         * 构造方法，初始化列表项的控件
         *
         * @param itemView item项的父控件
         */
        DataHolder(View itemView) {
            super(itemView);

            mDetailsView = itemView.findViewById(R.id.view_details);
        }
    }
}
