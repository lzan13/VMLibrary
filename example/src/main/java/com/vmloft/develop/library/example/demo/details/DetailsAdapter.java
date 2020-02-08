package com.vmloft.develop.library.example.demo.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.adapter.VMHolder;

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
    public DataHolder createHolder(@NonNull ViewGroup root, int viewType) {
        View view = mInflater.inflate(R.layout.details_list_item, root, false);
        return new DataHolder(view);
    }

    @Override
    public void bindHolder(@NonNull DataHolder holder, int position) {
        final DetailsEntity detailsEntity = getItemData(position);

        //holder.mDetailsView.setFold(detailsEntity.isFold());
        //holder.mDetailsView.setContentText(detailsEntity.getContent());
        holder.mDetailsView.setText(detailsEntity.getContent());
    }

    /**
     * 自定义会话列表项的 ViewHolder 用来显示会话列表项的内容
     */
    static class DataHolder extends VMHolder {

        TextView mDetailsView;

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
