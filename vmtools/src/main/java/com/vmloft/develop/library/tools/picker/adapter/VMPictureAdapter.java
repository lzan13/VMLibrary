package com.vmloft.develop.library.tools.picker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.adapter.VMHolder;
import com.vmloft.develop.library.tools.picker.IPictureLoader;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;

import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.widget.toast.VMToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/05/16 21:51
 *
 * 加载相册图片的 RecyclerView 适配器，使用局部刷新解决选中照片出现闪动问题
 */
public class VMPictureAdapter extends VMAdapter<VMPictureBean, VMHolder> {

    // 相机 Item 类型
    private static final int ITEM_TYPE_CAMERA = 0;
    // 正常 Item 类型
    private static final int ITEM_TYPE_NORMAL = 1;

    // 是否显示拍照按钮
    private boolean isShowCamera;

    /**
     * 构造方法
     */
    public VMPictureAdapter(Context context, ArrayList<VMPictureBean> pictures) {
        super(context, pictures);

        int space = VMDimen.dp2px(2);

        isShowCamera = VMPicker.getInstance().isShowCamera();
        if (isShowCamera) {
            mDataList.add(0, new VMPictureBean());
        }
    }

    @Override
    public VMHolder createHolder(@NonNull ViewGroup root, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA) {
            return new CameraViewHolder(mInflater.inflate(R.layout.vm_pick_picture_grid_camera_item, root, false));
        }
        return new PictureViewHolder(mInflater.inflate(R.layout.vm_pick_picture_grid_item, root, false));
    }

    @Override
    public void bindHolder(@NonNull VMHolder holder, int position) {
        switch (getItemViewType(position)) {
        case ITEM_TYPE_CAMERA:
            ((CameraViewHolder) holder).bindCamera();
            break;
        case ITEM_TYPE_NORMAL:
            ((PictureViewHolder) holder).bind(position);
            break;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VMHolder holder, int position, @NonNull List<Object> payloads) {
        //        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        if (VMPicker.getInstance().isMultiMode() && position > 0) {
            VMPictureBean bean = getItemData(position);
            ((PictureViewHolder) holder).refreshCheckBox(bean);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowCamera) {
            return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
        }
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public void refresh(List<VMPictureBean> list) {
        if (list == null || list.size() == 0) {
            mDataList.clear();
        } else {
            mDataList.clear();
            mDataList.addAll(list);
        }
        if (isShowCamera) {
            mDataList.add(0, new VMPictureBean());
        }
        notifyDataSetChanged();
    }

    /**
     * 展示图片缩略图 Holder
     */
    private class PictureViewHolder extends VMHolder {

        // 图片
        public ImageView mThumbView;
        // 选择框热区
        public View mHotRegionCB;
        // 选择框
        public CheckBox mItemCB;

        public PictureViewHolder(View itemView) {
            super(itemView);
            mThumbView = itemView.findViewById(R.id.vm_pick_grid_item_thumb_iv);
            mHotRegionCB = itemView.findViewById(R.id.vm_pick_grid_item_check_layout);
            mItemCB = itemView.findViewById(R.id.vm_pick_grid_item_cb);
        }

        /**
         * 绑定数据
         *
         * @param position 数据所在位置
         */
        public void bind(final int position) {
            final VMPictureBean bean = getItemData(position);
            // 根据是否多选，显示或隐藏checkbox
            if (VMPicker.getInstance().isMultiMode()) {
                refreshCheckBox(bean);
            } else {
                mHotRegionCB.setVisibility(View.GONE);
                mItemCB.setVisibility(View.GONE);
            }

            mHotRegionCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemCB.setChecked(!mItemCB.isChecked());
                    List<VMPictureBean> selectedList = VMPicker.getInstance().getSelectedPictures();
                    int selectLimit = VMPicker.getInstance().getSelectLimit();
                    if (mItemCB.isChecked() && selectedList.size() >= selectLimit) {
                        String toastMsg = VMStr.byResArgs(R.string.vm_pick_select_limit, selectLimit);
                        VMToast.make((Activity) mContext, toastMsg).error();
                        mItemCB.setChecked(false);
                    } else {
                        VMPicker.getInstance().addSelectedPicture(position, bean, mItemCB.isChecked());
                    }
                }
            });
            IPictureLoader.Options options = new IPictureLoader.Options(bean.path);
            options.isRadius = true;
            options.radiusSize = VMDimen.dp2px(4);
            VMPicker.getInstance().getPictureLoader().load(mContext, options, mThumbView); //显示图片
        }

        /**
         * 刷新选中状态
         */
        public void refreshCheckBox(VMPictureBean bean) {
            mHotRegionCB.setVisibility(View.VISIBLE);
            mItemCB.setVisibility(View.VISIBLE);
            List<VMPictureBean> selectedList = VMPicker.getInstance().getSelectedPictures();
            boolean checked = selectedList.contains(bean);
            if (checked) {
                mItemCB.setChecked(true);
            } else {
                mItemCB.setChecked(false);
            }
        }
    }

    /**
     * 打开相机 Holder
     */
    private class CameraViewHolder extends VMHolder {

        private View mItemView;

        public CameraViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public void bindCamera() {
            mItemView.setTag(null);
        }
    }
}
