package com.vmloft.develop.library.simple.widget.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.widget.VMDetailsView;
import java.util.List;

/**
 * Created by lzan13 on 2017/3/29.
 *
 * 历史通话适配器
 */
public class VMDetailsAdapter extends RecyclerView.Adapter<VMDetailsAdapter.ConversationViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<VMDetailsEntity> list;
    private ItemListener itemListener;

    public VMDetailsAdapter(Context context, List<VMDetailsEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_details, parent, false);
        ConversationViewHolder viewHolder = new ConversationViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(final ConversationViewHolder holder, final int position) {
        final VMDetailsEntity detailsEntity = list.get(position);

        holder.detailsView.setFold(detailsEntity.isFold());
        holder.detailsView.setContentText(detailsEntity.getContent());

        // 设置 item 点击监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                VMLog.d("The item view position: %d", position);
                if (itemListener != null) {
                    itemListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }

    @Override public int getItemCount() {
        return list.size();
    }

    /**
     * 设置 RecyclerView 事件监听接口
     */
    public interface ItemListener {
        /**
         * RecyclerView item 点击回调
         *
         * @param view 当前点击的 view
         * @param position 当前点击位置
         */
        void onItemClick(View view, int position);
    }

    /**
     * 设置 RecyclerView item 事件监听回调
     */
    public void setItemListener(ItemListener listener) {
        itemListener = listener;
    }

    /**
     * 自定义会话列表项的 ViewHolder 用来显示会话列表项的内容
     */
    static class ConversationViewHolder extends RecyclerView.ViewHolder {

        VMDetailsView detailsView;

        /**
         * 构造方法，初始化列表项的控件
         *
         * @param itemView item项的父控件
         */
        ConversationViewHolder(View itemView) {
            super(itemView);

            detailsView = (VMDetailsView) itemView.findViewById(R.id.view_details);
        }
    }
}
