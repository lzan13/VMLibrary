package com.vmloft.develop.library.tools.picker.adapter;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.ui.VMPickBaseActivity;
import com.vmloft.develop.library.tools.picker.ui.VMPickGridActivity;

import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.widget.toast.VMToast;
import java.util.ArrayList;

/**
 * Create by lzan13 on 2019/05/16 21:51
 *
 * 加载相册图片的 RecyclerView 适配器，使用局部刷新解决选中照片出现闪动问题
 */
public class VMPictureAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int ITEM_TYPE_CAMERA = 0;  //第一个条目是相机
    private static final int ITEM_TYPE_NORMAL = 1;  //第一个条目不是相机
    private Activity mActivity;
    private ArrayList<VMPictureBean> mPictures;       //当前需要显示的所有的图片数据
    private ArrayList<VMPictureBean> mSelectedPictures; //全局保存的已经选中的图片数据
    private boolean isShowCamera;         //是否显示拍照按钮
    private int mImageSize;               //每个条目的大小
    private LayoutInflater mInflater;
    private OnImageItemClickListener listener;   //图片被点击的监听

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, VMPictureBean VMPictureBean, int position);
    }

    public void refreshData(ArrayList<VMPictureBean> pictures) {
        if (pictures == null || pictures.size() == 0) {
            this.mPictures = new ArrayList<>();
        } else {
            this.mPictures = pictures;
        }
        notifyDataSetChanged();
    }

    /**
     * 构造方法
     */
    public VMPictureAdapter(Activity activity, ArrayList<VMPictureBean> images) {
        this.mActivity = activity;
        if (images == null || images.size() == 0) {
            this.mPictures = new ArrayList<>();
        } else {
            this.mPictures = images;
        }

        mImageSize = (VMDimen.getScreenSize().x - VMDimen.dp2px(4) * 3) / 4;

        isShowCamera = VMPicker.getInstance().isShowCamera();
        mSelectedPictures = VMPicker.getInstance().getSelectedImages();
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA) {
            return new CameraViewHolder(mInflater.inflate(R.layout.vm_pick_picture_grid_camera_item, parent, false));
        }
        return new ImageViewHolder(mInflater.inflate(R.layout.vm_pick_picture_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder) {
            ((CameraViewHolder) holder).bindCamera();
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(position);
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return isShowCamera ? mPictures.size() + 1 : mPictures.size();
    }

    public VMPictureBean getItem(int position) {
        if (isShowCamera) {
            if (position == 0) {
                return null;
            }
            return mPictures.get(position - 1);
        } else {
            return mPictures.get(position);
        }
    }

    private class ImageViewHolder extends ViewHolder {

        View rootView;
        ImageView ivThumb;
        View mask;
        View checkView;
        CheckBox cbCheck;

        ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb = (ImageView) itemView.findViewById(R.id.vm_pick_grid_item_thumb_iv);
            mask = itemView.findViewById(R.id.vm_pick_grid_item_mask_view);
            checkView = itemView.findViewById(R.id.vm_pick_grid_item_check_layout);
            cbCheck = itemView.findViewById(R.id.vm_pick_grid_item_cb);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
        }

        void bind(final int position) {
            final VMPictureBean VMPictureBean = getItem(position);
            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onImageItemClick(rootView, VMPictureBean, position);
                    }
                }
            });
            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbCheck.setChecked(!cbCheck.isChecked());
                    int selectLimit = VMPicker.getInstance().getSelectLimit();
                    if (cbCheck.isChecked() && mSelectedPictures.size() >= selectLimit) {
                        String toastMsg = String.format(VMStr.strByResId(R.string.ip_select_limit), selectLimit);
                        VMToast.make(mActivity, toastMsg).error();
                        cbCheck.setChecked(false);
                        mask.setVisibility(View.GONE);
                    } else {
                        VMPicker.getInstance()
                            .addSelectedImageItem(position, VMPictureBean, cbCheck.isChecked());
                        mask.setVisibility(View.VISIBLE);
                    }
                }
            });
            //根据是否多选，显示或隐藏checkbox
            if (VMPicker.getInstance().isMultiMode()) {
                cbCheck.setVisibility(View.VISIBLE);
                boolean checked = mSelectedPictures.contains(VMPictureBean);
                if (checked) {
                    mask.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(true);
                } else {
                    mask.setVisibility(View.GONE);
                    cbCheck.setChecked(false);
                }
            } else {
                cbCheck.setVisibility(View.GONE);
            }
            VMPicker.getInstance()
                .getPictureLoader()
                .displayImage(mActivity, VMPictureBean.path, ivThumb, mImageSize, mImageSize); //显示图片
        }
    }

    private class CameraViewHolder extends ViewHolder {

        View mItemView;

        CameraViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        void bindCamera() {
            mItemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            mItemView.setTag(null);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((VMPickBaseActivity) mActivity).checkPermission(Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions(mActivity, new String[] {
                            Manifest.permission.CAMERA
                        }, VMPickGridActivity.REQUEST_PERMISSION_CAMERA);
                    } else {
                        VMPicker.getInstance().takePicture(mActivity, VMPicker.REQUEST_CODE_TAKE);
                    }
                }
            });
        }
    }
}
