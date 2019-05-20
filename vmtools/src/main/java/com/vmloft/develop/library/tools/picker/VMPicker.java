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

import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.picker.bean.VMFolderBean;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.ui.VMPickGridActivity;
import com.vmloft.develop.library.tools.router.VMRouter;
import com.vmloft.develop.library.tools.utils.VMStr;
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

//    public static final int REQUEST_CODE_TAKE = 1001;
//    public static final int REQUEST_CODE_CROP = 1002;
//    public static final int REQUEST_CODE_PREVIEW = 1003;
//    public static final int RESULT_CODE_ITEMS = 1004;
//    public static final int RESULT_CODE_BACK = 1005;

    public static final String EXTRA_RESULT_ITEMS = "extra_result_items";
    public static final String EXTRA_SELECTED_IMAGE_POSITION = "selected_image_position";
    public static final String EXTRA_IMAGE_ITEMS = "extra_image_items";
    public static final String EXTRA_FROM_ITEMS = "extra_from_items";

    // 图片选择模式 是否多选 默认为 true
    private boolean mMultiMode = true;
    // 图片加载器接口，需要外部实现，减少三方库依赖
    private IPictureLoader mPictureLoader;
    // 是否开启裁剪，单选模式有效
    private boolean mCrop = true;
    // 裁剪焦点框的宽度
    private int mCropFocusWidth = 256;
    // 裁剪焦点框的高度
    private int mCropFocusHeight = 256;
    // 裁剪图片保存宽度
    private int mCropOutWidth = 720;
    // 裁剪图片保存高度
    private int mCropOutHeight = 720;
    // 裁剪框的形状
    private VMCropView.Style mCropStyle = VMCropView.Style.RECTANGLE;
    // 裁剪缓存文件夹路径
    private String mCropCacheFolder;
    // 裁剪后的图片是否保存为矩形，默认为 false，否则跟随裁剪框的形状
    private boolean mSaveRectangle = false;
    // 最大选择图片数量 默认 9 张
    private int mSelectLimit = 9;
    // 显示相机
    private boolean mShowCamera = true;
    // 选中的图片集合
    private ArrayList<VMPictureBean> mSelectedPictures = new ArrayList<>();
    // 定义调用拍照保存文件
    private File mTakePicture;
    // 所有的图片文件夹
    private List<VMFolderBean> mFolderBeans;
    // 当前选中的文件夹位置 0 表示所有图片
    private int mCurrentFolderPosition = 0;
    // 图片选中的监听回调
    private List<OnSelectedPictureListener> mSelectedPictureListeners;

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
                mTakePicture = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            } else {
                mTakePicture = Environment.getDataDirectory();
            }
            mTakePicture = VMFile.createFile(mTakePicture.getAbsolutePath(), "IMG_", ".jpg");
            if (mTakePicture != null) {
                // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                // 如果没有指定uri，则data就返回有数据！

                Uri uri;
                if (VERSION.SDK_INT <= VERSION_CODES.M) {
                    uri = Uri.fromFile(mTakePicture);
                } else {

                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为 FileProvider
                     * 并且这样可以解决 MIUI 系统上拍照返回 size 为 0 的情况
                     */
                    uri = FileProvider.getUriForFile(activity, VMPickerProvider.getAuthority(activity), mTakePicture);
                    //加入uri权限 要不三星手机不能拍照
                    List<ResolveInfo> resInfoList = activity.getPackageManager()
                            .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
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
     * 通知系统相册有变化
     */
    public static void notifyGalleryChange(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    /**
     * 添加选中图片，选中的图片时全局保存的，方便统一管理
     *
     * @param position 图片位置
     * @param bean     图片实体对象
     * @param isAdd    是否是添加
     */
    public void addSelectedPicture(int position, VMPictureBean bean, boolean isAdd) {
        if (isAdd) {
            mSelectedPictures.add(bean);
        } else {
            mSelectedPictures.remove(bean);
        }
        notifySelectedPictureChanged(position, bean, isAdd);
    }

    /**
     * 通知选中图片有更新
     *
     * @param position 图片位置
     * @param bean     图片实体对象
     * @param isAdd    是否是添加
     */
    private void notifySelectedPictureChanged(int position, VMPictureBean bean, boolean isAdd) {
        if (mSelectedPictureListeners == null) {
            return;
        }
        // 循环通知选中的图片有更新
        for (OnSelectedPictureListener listener : mSelectedPictureListeners) {
            listener.onPictureSelected(position, bean, isAdd);
        }
    }

    /**
     * --------------------------- 链式调用设置属性方法 ---------------------------
     */
    /**
     * 设置是否为多图抹式，默认为 true
     *
     * @param multiMode 是否多图模式
     */
    public VMPicker setMultiMode(boolean multiMode) {
        mMultiMode = multiMode;
        return this;
    }

    /**
     * 设置图片加载接口实现
     *
     * @param pictureLoader 外部实现的图片加载类
     */
    public VMPicker setPictureLoader(IPictureLoader pictureLoader) {
        this.mPictureLoader = pictureLoader;
        return this;
    }

    /**
     * 是否开启剪切 默认为 true，不过这个和单选模式相关联，多选无法使用
     *
     * @param crop 是否剪切
     */
    public VMPicker setCrop(boolean crop) {
        mCrop = crop;
        return this;
    }

    /**
     * 设置裁剪焦点框宽度
     *
     * @param width 裁剪框宽度
     */
    public VMPicker setCropFocusWidth(int width) {
        mCropFocusWidth = width;
        return this;
    }

    /**
     * 设置裁剪焦点框高度
     *
     * @param height 裁剪框高度
     */
    public VMPicker setCropFocusHeight(int height) {
        mCropFocusHeight = height;
        return this;
    }

    /**
     * 设置裁剪图片输出宽度
     *
     * @param width 裁剪输出宽度
     */
    public VMPicker setCropOutWidth(int width) {
        mCropOutWidth = width;
        return this;
    }

    /**
     * 设置裁剪图片输出宽高度
     *
     * @param height 裁剪输出高度
     */
    public VMPicker setCropOutHeight(int height) {
        mCropOutHeight = height;
        return this;
    }

    /**
     * 设置裁剪框样式 {@link VMCropView.Style}
     *
     * @param style 裁剪框样式
     */
    public VMPicker setCropStyle(VMCropView.Style style) {
        mCropStyle = style;
        return this;
    }

    /**
     * 设置裁剪图片缓存文件夹路径
     *
     * @param folder 文件夹地址
     */
    public void setCropCacheFolder(String folder) {
        mCropCacheFolder = folder;
    }

    /**
     * 设施是否保存为剪切区域的矩形数据，默认为 false
     *
     * @param saveRectangle 保存为矩形
     */
    public VMPicker setSaveRectangle(boolean saveRectangle) {
        mSaveRectangle = saveRectangle;
        return this;
    }

    /**
     * 设置选择限制 默认 9 个
     *
     * @param limit 最多选择多少个
     */
    public VMPicker setSelectLimit(int limit) {
        mSelectLimit = limit;
        return this;
    }

    /**
     * 设置是否显示相机选项 默认为 true
     *
     * @param showCamera 是否显示相机
     */
    public VMPicker setShowCamera(boolean showCamera) {
        mShowCamera = showCamera;
        return this;
    }

    /**
     * 设置选中的图像集合，这个主要是在已经选中了的情况下又打开选择器，外部传入
     *
     * @param pictures 已经选中的图片集合
     */
    public VMPicker setSelectedPictures(ArrayList<VMPictureBean> pictures) {
        if (pictures == null) {
            return this;
        }
        mSelectedPictures = pictures;
        return this;
    }

    /**
     * 启动选择器
     */
    public void startPicker(Activity activity) {
        Intent intent = new Intent(activity, VMPickGridActivity.class);
        intent.putExtra(VMConstant.VM_KEY_PICK_PICTURES, mSelectedPictures);
        VMRouter.goPicker(activity, intent, VMConstant.VM_PICK_REQUEST_CODE);
    }

    /**
     * 设置文件夹集合
     *
     * @param folders 文件夹集合
     */
    public void setFolderBeans(List<VMFolderBean> folders) {
        mFolderBeans = folders;
    }

    /**
     * 设置当前选中的文件夹在文件夹列表中的位置
     *
     * @param position 当前选中的在列表中的位置
     */
    public void setCurrentFolderPosition(int position) {
        mCurrentFolderPosition = position;
    }


    /**
     * --------------------------- 获取一些属性 ---------------------------
     */
    /**
     * 判断是不是多选模式
     */
    public boolean isMultiMode() {
        return mMultiMode;
    }

    /**
     * 获取图片加载实例
     */
    public IPictureLoader getPictureLoader() {
        return mPictureLoader;
    }

    /**
     * 判断是否开启剪切
     */
    public boolean isCrop() {
        return mCrop;
    }

    /**
     * 获取裁剪输出宽度
     */
    public int getCropOutWidth() {
        return mCropOutWidth;
    }

    /**
     * 获取裁剪输出高度
     */
    public int getCropOutHeight() {
        return mCropOutHeight;
    }

    /**
     * 获取裁剪框宽度
     */
    public int getCropFocusWidth() {
        return mCropFocusWidth;
    }

    /**
     * 获取裁剪框高度
     */
    public int getCropFocusHeight() {
        return mCropFocusHeight;
    }

    /**
     * 获取裁剪框样式
     */
    public VMCropView.Style getCropStyle() {
        return mCropStyle;
    }

    /**
     * 获取缓存文件夹
     */
    public String getCropCacheFolder() {
        if (VMStr.isEmpty(mCropCacheFolder)) {
            mCropCacheFolder = VMFile.getCacheFromSDCard();
        }
        return mCropCacheFolder;
    }

    /**
     * 判断是否保存为矩形
     */
    public boolean isSaveRectangle() {
        return mSaveRectangle;
    }

    /**
     * 获取选择限制
     *
     * @return 最多选择多少个
     */
    public int getSelectLimit() {
        return mSelectLimit;
    }

    /**
     * 判断是否显示相机
     */
    public boolean isShowCamera() {
        return mShowCamera;
    }

    /**
     * 获取已选择的图片集合
     */
    public ArrayList<VMPictureBean> getSelectedPictures() {
        return mSelectedPictures;
    }

    /**
     * 判断已选择图片是否包含指定对象
     *
     * @param bean 要判断是否包含的对象
     */
    public boolean isSelectPicture(VMPictureBean bean) {
        return mSelectedPictures.contains(bean);
    }

    /**
     * 获取要选择的数据对象个数
     */
    public int getSelectPictureCount() {
        if (mSelectedPictures == null) {
            return 0;
        }
        return mSelectedPictures.size();
    }

    /**
     * 获取拍照保存的文件
     */
    public File getTakeImageFile() {
        return mTakePicture;
    }

    /**
     * 获取文件夹列表集合
     */
    public List<VMFolderBean> getFolderBeans() {
        return mFolderBeans;
    }

    /**
     * 获取当前选中的文件夹位置
     */
    public int getCurrentFolderPosition() {
        return mCurrentFolderPosition;
    }

    /**
     * 获取当前文件夹下图片集合
     */
    public ArrayList<VMPictureBean> getCurrentFolderPictures() {
        return mFolderBeans.get(mCurrentFolderPosition).pictures;
    }

    /**
     * 清空当前选择的图片
     */
    public void clearSelectedPictures() {
        if (mSelectedPictures != null) {
            mSelectedPictures.clear();
        }
    }

    /**
     * 重置选择器
     */
    public void reset() {
        if (mSelectedPictureListeners != null) {
            mSelectedPictureListeners.clear();
            mSelectedPictureListeners = null;
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
        mMultiMode = savedInstanceState.getBoolean("multiMode");
        mSelectLimit = savedInstanceState.getInt("selectLimit");
        mCrop = savedInstanceState.getBoolean("crop");
        mShowCamera = savedInstanceState.getBoolean("showCamera");
        mSaveRectangle = savedInstanceState.getBoolean("isSaveRectangle");
        mCropOutWidth = savedInstanceState.getInt("outPutX");
        mCropOutHeight = savedInstanceState.getInt("outPutY");
        mCropFocusWidth = savedInstanceState.getInt("focusWidth");
        mCropFocusHeight = savedInstanceState.getInt("focusHeight");
        mCropStyle = (VMCropView.Style) savedInstanceState.getSerializable("style");
        mCropCacheFolder = savedInstanceState.getString("cropCacheFolder");
        mPictureLoader = (IPictureLoader) savedInstanceState.getSerializable("mPictureLoader");
        mTakePicture = (File) savedInstanceState.getSerializable("takeImageFile");
    }

    /**
     * 用于手机内存不足，进程被系统回收时的状态保存
     */
    public void saveInstanceState(Bundle outState) {
        outState.putBoolean("multiMode", mMultiMode);
        outState.putBoolean("crop", mCrop);
        outState.putInt("selectLimit", mSelectLimit);
        outState.putBoolean("showCamera", mShowCamera);
        outState.putBoolean("isSaveRectangle", mSaveRectangle);
        outState.putInt("outPutX", mCropOutWidth);
        outState.putInt("outPutY", mCropOutHeight);
        outState.putInt("focusWidth", mCropFocusWidth);
        outState.putInt("focusHeight", mCropFocusHeight);
        outState.putSerializable("style", mCropStyle);
        outState.putString("cropCacheFolder", mCropCacheFolder);
        outState.putSerializable("mPictureLoader", mPictureLoader);
        outState.putSerializable("takeImageFile", mTakePicture);
    }

    /**
     * ---------------------------------------------------------------
     *
     * 图片选中的监听
     */
    public interface OnSelectedPictureListener {
        void onPictureSelected(int position, VMPictureBean item, boolean isAdd);
    }

    /**
     * 添加图片选中监听回调
     */
    public void addOnSelectedPictureListener(OnSelectedPictureListener listener) {
        if (mSelectedPictureListeners == null) {
            mSelectedPictureListeners = new ArrayList<>();
        }
        mSelectedPictureListeners.add(listener);
    }

    /**
     * 移除图片选中监听回调
     */
    public void removeOnSelectedPictureListener(OnSelectedPictureListener listener) {
        if (mSelectedPictureListeners == null) {
            return;
        }
        mSelectedPictureListeners.remove(listener);
    }

}