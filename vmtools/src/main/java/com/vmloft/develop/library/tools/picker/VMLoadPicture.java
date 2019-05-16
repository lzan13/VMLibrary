package com.vmloft.develop.library.tools.picker;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.bean.VMFolderBean;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/05/15
 * 加载图片工具类
 */
public class VMLoadPicture implements LoaderManager.LoaderCallbacks<Cursor> {
    // 加载所有图片
    public static final int LOADER_ALL = 0;
    // 分类加载图片
    public static final int LOADER_CATEGORY = 1;
    // 查询图片需要的数据列
    private final String[] IMAGE_PROJECTION = {
        // 图片的显示名称  aaa.jpg
        MediaStore.Images.Media.DISPLAY_NAME,
        // 图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
        MediaStore.Images.Media.DATA,
        // 图片的大小 long型  132492
        MediaStore.Images.Media.SIZE,
        // 图片的宽度 int型  1920
        MediaStore.Images.Media.WIDTH,
        // 图片的高度 int型  1080
        MediaStore.Images.Media.HEIGHT,
        // 图片的类型 image/jpeg
        MediaStore.Images.Media.MIME_TYPE,
        // 图片被添加的时间，long型  1450518608
        MediaStore.Images.Media.DATE_ADDED
    };

    private FragmentActivity activity;
    // 图片加载完成的回调接口
    private OnPictureListener mPictureListener;
    // 所有的图片文件夹
    private ArrayList<VMFolderBean> mFolderBeans = new ArrayList<>();

    /**
     * @param activity 用于初始化LoaderManager，需要兼容到2.3
     * @param path     指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     * @param listener 图片加载完成的回调接口
     */
    public VMLoadPicture(FragmentActivity activity, String path, OnPictureListener listener) {
        this.activity = activity;
        this.mPictureListener = listener;

        LoaderManager loaderManager = activity.getSupportLoaderManager();
        if (path == null) {
            loaderManager.initLoader(LOADER_ALL, null, this);//加载所有的图片
        } else {
            //加载指定目录的图片
            Bundle bundle = new Bundle();
            bundle.putString("path", path);
            loaderManager.initLoader(LOADER_CATEGORY, bundle, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        //扫描所有图片
        if (id == LOADER_ALL) {
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
        }
        //扫描某个图片文件夹
        if (id == LOADER_CATEGORY) {
            cursorLoader = new CursorLoader(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args
                .getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFolderBeans.clear();
        if (data != null) {
            ArrayList<VMPictureBean> allImages = new ArrayList<>();   //所有图片的集合,不分文件夹
            while (data.moveToNext()) {
                //查询数据
                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));

                File file = new File(imagePath);
                if (!file.exists() || file.length() <= 0) {
                    continue;
                }

                long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                //封装实体
                VMPictureBean VMPictureBean = new VMPictureBean();
                VMPictureBean.name = imageName;
                VMPictureBean.path = imagePath;
                VMPictureBean.size = imageSize;
                VMPictureBean.width = imageWidth;
                VMPictureBean.height = imageHeight;
                VMPictureBean.mimeType = imageMimeType;
                VMPictureBean.addTime = imageAddTime;
                allImages.add(VMPictureBean);
                //根据父路径分类存放图片
                File imageFile = new File(imagePath);
                File imageParentFile = imageFile.getParentFile();
                VMFolderBean bean = new VMFolderBean();
                bean.name = imageParentFile.getName();
                bean.path = imageParentFile.getAbsolutePath();

                if (!mFolderBeans.contains(bean)) {
                    ArrayList<VMPictureBean> images = new ArrayList<>();
                    images.add(VMPictureBean);
                    bean.cover = VMPictureBean;
                    bean.images = images;
                    mFolderBeans.add(bean);
                } else {
                    mFolderBeans.get(mFolderBeans.indexOf(bean)).images.add(VMPictureBean);
                }
            }
            //防止没有图片报异常
            if (data.getCount() > 0 && allImages.size() > 0) {
                //构造所有图片的集合
                VMFolderBean allImagesFolder = new VMFolderBean();
                allImagesFolder.name = activity.getResources().getString(R.string.ip_all_images);
                allImagesFolder.path = "/";
                allImagesFolder.cover = allImages.get(0);
                allImagesFolder.images = allImages;
                mFolderBeans.add(0, allImagesFolder);  //确保第一条是所有图片
            }
        }

        //回调接口，通知图片数据准备完成
        VMPicker.getInstance().setImageFolders(mFolderBeans);
        mPictureListener.onLoadComplete(mFolderBeans);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        System.out.println("--------");
    }

    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnPictureListener {
        void onLoadComplete(List<VMFolderBean> VMFolderBeans);
    }
}
