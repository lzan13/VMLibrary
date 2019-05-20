package com.vmloft.develop.library.tools.picker.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.adapter.VMHolder;
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

    // 当前需要显示的所有的图片数据
    private ArrayList<VMPictureBean> mPictures;
    // 全局保存的已经选中的图片数据
    private ArrayList<VMPictureBean> mSelectedPictures;
    // 是否显示拍照按钮
    private boolean isShowCamera;
    // Item 的大小，这个需要根据屏幕大小计算
    private int mItemSize;

    /**
     * 构造方法
     */
    public VMPictureAdapter(Context context, ArrayList<VMPictureBean> pictures) {
        super(context, pictures);

        int space = VMDimen.dp2px(2);
        mItemSize = (VMDimen.getScreenSize().x - space * 3) / 4;

        mSelectedPictures = VMPicker.getInstance().getSelectedPictures();
        isShowCamera = VMPicker.getInstance().isShowCamera();
        if (isShowCamera) {
            mDataList.add(0, new VMPictureBean());
        }
    }

    @Override
    public VMHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA) {
            return new CameraViewHolder(mInflater.inflate(R.layout.vm_pick_picture_grid_camera_item, parent, false));
        }
        return new PictureViewHolder(mInflater.inflate(R.layout.vm_pick_picture_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VMHolder holder, int position) {
        super.onBindViewHolder(holder, position);
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
        // 遮罩
        public View mMaskView;
        // 选择框热区
        public View mHotRegionCB;
        // 选择框
        public CheckBox mPickCB;

        public PictureViewHolder(View itemView) {
            super(itemView);
            mThumbView = itemView.findViewById(R.id.vm_pick_grid_item_thumb_iv);
            mMaskView = itemView.findViewById(R.id.vm_pick_grid_item_mask_view);
            mHotRegionCB = itemView.findViewById(R.id.vm_pick_grid_item_check_layout);
            mPickCB = itemView.findViewById(R.id.vm_pick_grid_item_cb);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemSize)); //让图片是个正方形
        }

        public void bind(final int position) {
            final VMPictureBean VMPictureBean = getItemData(position);
            // 根据是否多选，显示或隐藏checkbox
            if (VMPicker.getInstance().isMultiMode()) {
                mHotRegionCB.setVisibility(View.VISIBLE);
                mPickCB.setVisibility(View.VISIBLE);
                boolean checked = mSelectedPictures.contains(VMPictureBean);
                if (checked) {
                    mMaskView.setVisibility(View.VISIBLE);
                    mPickCB.setChecked(true);
                } else {
                    mMaskView.setVisibility(View.GONE);
                    mPickCB.setChecked(false);
                }
            } else {
                mHotRegionCB.setVisibility(View.GONE);
                mPickCB.setVisibility(View.GONE);
            }

            mHotRegionCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPickCB.setChecked(!mPickCB.isChecked());
                    int selectLimit = VMPicker.getInstance().getSelectLimit();
                    if (mPickCB.isChecked() && mSelectedPictures.size() >= selectLimit) {
                        String toastMsg = VMStr.byResArgs(R.string.vm_pick_select_limit, selectLimit);
                        VMToast.make((Activity) mContext, toastMsg).error();
                        mPickCB.setChecked(false);
                        mMaskView.setVisibility(View.GONE);
                    } else {
                        VMPicker.getInstance()
                            .addSelectedPicture(position, VMPictureBean, mPickCB.isChecked());
                        mMaskView.setVisibility(View.VISIBLE);
                    }
                }
            });
            VMPicker.getInstance()
                .getPictureLoader()
                .displayImage(mContext, VMPictureBean.path, mThumbView, mItemSize, mItemSize); //显示图片
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
            mItemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemSize)); //让图片是个正方形
            mItemView.setTag(null);
            //            mItemView.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    if (!((VMPickBaseActivity) mContext).checkPermission(Manifest.permission.CAMERA)) {
            //                        ActivityCompat.requestPermissions(mActivity, new String[]{
            //                                Manifest.permission.CAMERA
            //                        }, VMPickGridActivity.REQUEST_PERMISSION_CAMERA);
            //                    } else {
            //                        VMPicker.getInstance().takePicture(mActivity, VMPicker.REQUEST_CODE_TAKE);
            //                    }
            //                }
            //            });
        }
    }
}
