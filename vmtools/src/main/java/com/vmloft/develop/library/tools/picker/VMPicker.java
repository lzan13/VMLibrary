package com.vmloft.develop.library.tools.picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.vmloft.develop.library.tools.picker.bean.VMFolderBean;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.util.ProviderUtil;
import com.vmloft.develop.library.tools.widget.VMCropView;

import com.vmloft.develop.library.tools.utils.VMFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/05/16
 *
 * 图片选择的入口类，采用单例和弱引用解决Intent传值限制导致的异常
 */
public class VMPicker {

    public static final int REQUEST_CODE_TAKE = 1001;
    public static final int REQUEST_CODE_CROP = 1002;
    public static final int REQUEST_CODE_PREVIEW = 1003;
    public static final int RESULT_CODE_ITEMS = 1004;
    public static final int RESULT_CODE_BACK = 1005;

    public static final String EXTRA_RESULT_ITEMS = "extra_result_items";
    public static final String EXTRA_SELECTED_IMAGE_POSITION = "selected_image_position";
    public static final String EXTRA_IMAGE_ITEMS = "extra_image_items";
    public static final String EXTRA_FROM_ITEMS = "extra_from_items";

    // 图片选择模式 是否多选
    private boolean multiMode = true;
    // 最大选择图片数量 默认 9 张
    private int selectLimit = 9;
    // 是否开启裁剪，单选模式有效
    private boolean crop = true;
    // 显示相机
    private boolean showCamera = true;
    // 裁剪后的图片是否是矩形，否者跟随裁剪框的形状
    private boolean isSaveRectangle = false;
    private int outPutX = 720;           //裁剪保存宽度
    private int outPutY = 720;           //裁剪保存高度
    private int focusWidth = 256;         //焦点框的宽度
    private int focusHeight = 256;        //焦点框的高度
    private VMCropView.Style style = VMCropView.Style.RECTANGLE; //裁剪框的形状
    // 图片加载器接口，需要外部实现，减少三方库依赖
    private IPictureLoader mPictureLoader;
    private File cropCacheFolder;
    private File takeImageFile;
    public Bitmap cropBitmap;
    // 选中的图片集合
    private ArrayList<VMPictureBean> mSelectedPictures = new ArrayList<>();
    // 所有的图片文件夹
    private List<VMFolderBean> mFolderBeans;
    // 当前选中的文件夹位置 0 表示所有图片
    private int mCurrentFolderPosition = 0;
    // 图片选中的监听回调
    private List<OnImageSelectedListener> mImageSelectedListeners;


    private VMPicker() {
    }

    /**
     * 内部类实现单利模式
     */
    private static class InnerHolder {
        public static VMPicker INSTANCE = new VMPicker();
    }

    /**
     * 获取单例类实例
     */
    public static VMPicker getInstance() {
        return InnerHolder.INSTANCE;
    }

    /**
     * 拍照的方法
     */
    public void takePicture(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (VMFile.hasSdcard()) {
                takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            } else {
                takeImageFile = Environment.getDataDirectory();
            }
            takeImageFile = VMFile.createFile(takeImageFile.getAbsolutePath(), "IMG_", ".jpg");
            if (takeImageFile != null) {
                // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                // 如果没有指定uri，则data就返回有数据！

                Uri uri;
                if (VERSION.SDK_INT <= VERSION_CODES.M) {
                    uri = Uri.fromFile(takeImageFile);
                } else {

                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为 FileProvider
                     * 并且这样可以解决 MIUI 系统上拍照返回 size 为 0 的情况
                     */
                    uri = FileProvider.getUriForFile(activity, VMPickerProvider.getAuthority(activity), takeImageFile);
                    //加入uri权限 要不三星手机不能拍照
                    List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        activity.startActivityForResult(takePictureIntent, requestCode);
    }

    /**
     * 扫描图片
     */
    public static void galleryAddPic(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 图片选中的监听
     */
    public interface OnImageSelectedListener {
        void onImageSelected(int position, VMPictureBean item, boolean isAdd);
    }

    public void addOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) {
            mImageSelectedListeners = new ArrayList<>();
        }
        mImageSelectedListeners.add(l);
    }

    public void removeOnImageSelectedListener(OnImageSelectedListener l) {
        if (mImageSelectedListeners == null) {
            return;
        }
        mImageSelectedListeners.remove(l);
    }

    public void addSelectedImageItem(int position, VMPictureBean item, boolean isAdd) {
        if (isAdd) {
            mSelectedPictures.add(item);
        } else {
            mSelectedPictures.remove(item);
        }
        notifyImageSelectedChanged(position, item, isAdd);
    }

    public void setSelectedImages(ArrayList<VMPictureBean> selectedImages) {
        if (selectedImages == null) {
            return;
        }
        this.mSelectedPictures = selectedImages;
    }

    private void notifyImageSelectedChanged(int position, VMPictureBean item, boolean isAdd) {
        if (mImageSelectedListeners == null) {
            return;
        }
        for (OnImageSelectedListener l : mImageSelectedListeners) {
            l.onImageSelected(position, item, isAdd);
        }
    }

    public boolean isMultiMode() {
        return multiMode;
    }

    public void setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isSaveRectangle() {
        return isSaveRectangle;
    }

    public void setSaveRectangle(boolean isSaveRectangle) {
        this.isSaveRectangle = isSaveRectangle;
    }

    public int getOutPutX() {
        return outPutX;
    }

    public void setOutPutX(int outPutX) {
        this.outPutX = outPutX;
    }

    public int getOutPutY() {
        return outPutY;
    }

    public void setOutPutY(int outPutY) {
        this.outPutY = outPutY;
    }

    public int getFocusWidth() {
        return focusWidth;
    }

    public void setFocusWidth(int focusWidth) {
        this.focusWidth = focusWidth;
    }

    public int getFocusHeight() {
        return focusHeight;
    }

    public void setFocusHeight(int focusHeight) {
        this.focusHeight = focusHeight;
    }

    public File getTakeImageFile() {
        return takeImageFile;
    }

    public File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
            cropCacheFolder = new File(context.getCacheDir() + "/VMPicker/cropTemp/");
        }
        return cropCacheFolder;
    }

    public void setCropCacheFolder(File cropCacheFolder) {
        this.cropCacheFolder = cropCacheFolder;
    }

    public IPictureLoader getPictureLoader() {
        return mPictureLoader;
    }

    public void setPictureLoader(IPictureLoader pictureLoader) {
        this.mPictureLoader = pictureLoader;
    }

    public VMCropView.Style getStyle() {
        return style;
    }

    public void setStyle(VMCropView.Style style) {
        this.style = style;
    }

    public List<VMFolderBean> getImageFolders() {
        return mFolderBeans;
    }

    public void setImageFolders(List<VMFolderBean> VMFolderBeans) {
        mFolderBeans = VMFolderBeans;
    }

    public int getCurrentImageFolderPosition() {
        return mCurrentFolderPosition;
    }

    public void setCurrentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        mCurrentFolderPosition = mCurrentSelectedImageSetPosition;
    }

    public ArrayList<VMPictureBean> getCurrentImageFolderItems() {
        return mFolderBeans.get(mCurrentFolderPosition).images;
    }

    public boolean isSelect(VMPictureBean item) {
        return mSelectedPictures.contains(item);
    }

    public int getSelectImageCount() {
        if (mSelectedPictures == null) {
            return 0;
        }
        return mSelectedPictures.size();
    }

    public ArrayList<VMPictureBean> getSelectedImages() {
        return mSelectedPictures;
    }

    public void clearSelectedImages() {
        if (mSelectedPictures != null) {
            mSelectedPictures.clear();
        }
    }

    public void reset() {
        if (mImageSelectedListeners != null) {
            mImageSelectedListeners.clear();
            mImageSelectedListeners = null;
        }
        if (mFolderBeans != null) {
            mFolderBeans.clear();
            mFolderBeans = null;
        }
        if (mSelectedPictures != null) {
            mSelectedPictures.clear();
        }
        mCurrentFolderPosition = 0;
    }

    /**
     * 用于手机内存不足，进程被系统回收，重启时的状态恢复
     */
    public void restoreInstanceState(Bundle savedInstanceState) {
        cropCacheFolder = (File) savedInstanceState.getSerializable("cropCacheFolder");
        takeImageFile = (File) savedInstanceState.getSerializable("takeImageFile");
        mPictureLoader = (IPictureLoader) savedInstanceState.getSerializable("IPictureLoader");
        style = (VMCropView.Style) savedInstanceState.getSerializable("style");
        multiMode = savedInstanceState.getBoolean("multiMode");
        crop = savedInstanceState.getBoolean("crop");
        showCamera = savedInstanceState.getBoolean("showCamera");
        isSaveRectangle = savedInstanceState.getBoolean("isSaveRectangle");
        selectLimit = savedInstanceState.getInt("selectLimit");
        outPutX = savedInstanceState.getInt("outPutX");
        outPutY = savedInstanceState.getInt("outPutY");
        focusWidth = savedInstanceState.getInt("focusWidth");
        focusHeight = savedInstanceState.getInt("focusHeight");
    }

    /**
     * 用于手机内存不足，进程被系统回收时的状态保存
     */
    public void saveInstanceState(Bundle outState) {
        outState.putSerializable("cropCacheFolder", cropCacheFolder);
        outState.putSerializable("takeImageFile", takeImageFile);
        outState.putSerializable("IPictureLoader", mPictureLoader);
        outState.putSerializable("style", style);
        outState.putBoolean("multiMode", multiMode);
        outState.putBoolean("crop", crop);
        outState.putBoolean("showCamera", showCamera);
        outState.putBoolean("isSaveRectangle", isSaveRectangle);
        outState.putInt("selectLimit", selectLimit);
        outState.putInt("outPutX", outPutX);
        outState.putInt("outPutY", outPutY);
        outState.putInt("focusWidth", focusWidth);
        outState.putInt("focusHeight", focusHeight);
    }
}