package com.vmloft.develop.library.tools.picker.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.picker.DataHolder;
import com.vmloft.develop.library.tools.picker.VMLoadPicture;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.adapter.VMFolderAdapter;
import com.vmloft.develop.library.tools.adapter.VMSpaceGridDecoration;
import com.vmloft.develop.library.tools.picker.adapter.VMPictureAdapter;
import com.vmloft.develop.library.tools.picker.bean.VMFolderBean;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.widget.FolderPopUpWindow;

import com.vmloft.develop.library.tools.utils.VMDimen;

import java.util.ArrayList;
import java.util.List;

/**
 * Crete by lzan13 on 2019/05/14
 *
 * 图片选择器
 */
public class VMPickGridActivity extends VMPickBaseActivity implements VMLoadPicture.OnPictureListener, VMPicker.OnImageSelectedListener, View.OnClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;
    public static final String EXTRAS_TAKE_PICKERS = "TAKE";
    public static final String EXTRAS_IMAGES = "IMAGES";

    private VMFolderAdapter mFolderAdapter;    //图片文件夹的适配器
    private FolderPopUpWindow mFolderPopupWindow;  //ImageSet的PopupWindow
    private List<VMFolderBean> mFolderBeans;   //所有的图片文件夹

    private boolean isDirectCamera = false; // 默认不是直接调取相机
    private boolean isOrigin = false;  //是否选中原图

    private Button mOKBtn;       //确定按钮
    private View mFooterBar;     //底部栏
    private View mChangeDirLayout; //文件夹切换按钮
    private TextView mCurrDirView; //显示当前文件夹
    private TextView mPreviewBtn;      //预览按钮

    // 使用 RecyclerView 展示图片
    private RecyclerView mRecyclerView;
    private VMPictureAdapter mPictureAdapter;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isDirectCamera = savedInstanceState.getBoolean(EXTRAS_TAKE_PICKERS, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRAS_TAKE_PICKERS, isDirectCamera);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vm_activity_pick_grid);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        // 每次打开都重置选择器
        VMPicker.getInstance().reset();

        mOKBtn = findViewById(R.id.vm_common_ok_btn);
        mFooterBar = findViewById(R.id.vm_pick_grid_bottom_bar_rl);
        mChangeDirLayout = findViewById(R.id.vm_pick_grid_choose_folder_rl);
        mCurrDirView = findViewById(R.id.vm_pick_grid_choose_folder_tv);
        mPreviewBtn = findViewById(R.id.vm_pick_grid_preview_btn);

        mRecyclerView = findViewById(R.id.vm_pick_grid_recycler_view);

        VMPicker.getInstance().addOnImageSelectedListener(this);

        Intent data = getIntent();
        // 新增可直接拍照
        if (data != null && data.getExtras() != null) {
            isDirectCamera = data.getBooleanExtra(EXTRAS_TAKE_PICKERS, false); // 默认不是直接打开相机
            if (isDirectCamera) {
                if (!(checkPermission(Manifest.permission.CAMERA))) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA
                    }, VMPickGridActivity.REQUEST_PERMISSION_CAMERA);
                } else {
                    VMPicker.getInstance().takePicture(activity, VMPicker.REQUEST_CODE_TAKE);
                }
            }
            ArrayList<VMPictureBean> images = (ArrayList<VMPictureBean>) data.getSerializableExtra(EXTRAS_IMAGES);
            VMPicker.getInstance().setSelectedImages(images);
        }


        findViewById(R.id.vm_common_back_btn).setOnClickListener(this);

        mOKBtn.setOnClickListener(this);
        mPreviewBtn.setOnClickListener(this);
        mChangeDirLayout.setOnClickListener(this);
        if (VMPicker.getInstance().isMultiMode()) {
            mOKBtn.setVisibility(View.VISIBLE);
            mPreviewBtn.setVisibility(View.VISIBLE);
        } else {
            mOKBtn.setVisibility(View.GONE);
            mPreviewBtn.setVisibility(View.GONE);
        }

        mFolderAdapter = new VMFolderAdapter(activity, null);
        mPictureAdapter = new VMPictureAdapter(activity, null);

        onImageSelected(0, null, false);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new VMLoadPicture(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_PERMISSION_STORAGE);
            }
        } else {
            new VMLoadPicture(activity, null, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new VMLoadPicture(this, null, this);
            } else {
                showToast("权限被禁止，无法选择本地图片");
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                VMPicker.getInstance().takePicture(this, VMPicker.REQUEST_CODE_TAKE);
            } else {
                showToast("权限被禁止，无法打开相机");
            }
        }
    }

    @Override
    protected void onDestroy() {
        VMPicker.getInstance().removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.vm_common_ok_btn) {
            Intent intent = new Intent();
            intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, VMPicker.getInstance().getSelectedImages());
            setResult(VMPicker.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
            finish();
        } else if (id == R.id.vm_pick_grid_choose_folder_rl) {
            if (mFolderBeans == null) {
                Log.i("VMPickGridActivity", "您的手机没有图片");
                return;
            }
            //点击文件夹按钮
            createPopupFolderList();
            mFolderAdapter.refreshData(mFolderBeans);  //刷新数据
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.showAtLocation(mFooterBar, Gravity.NO_GRAVITY, 0, 0);
                //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                int index = mFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mFolderPopupWindow.setSelection(index);
            }
        } else if (id == R.id.vm_pick_grid_preview_btn) {
            Intent intent = new Intent(VMPickGridActivity.this, VMPickPreviewActivity.class);
            intent.putExtra(VMPicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
            intent.putExtra(VMPicker.EXTRA_IMAGE_ITEMS, VMPicker.getInstance().getSelectedImages());
            intent.putExtra(VMPickPreviewActivity.ISORIGIN, isOrigin);
            intent.putExtra(VMPicker.EXTRA_FROM_ITEMS, true);
            startActivityForResult(intent, VMPicker.REQUEST_CODE_PREVIEW);
        } else if (id == R.id.vm_common_back_btn) {
            //点击返回按钮
            finish();
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(activity, mFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mFolderAdapter.setSelectIndex(position);
                VMPicker.getInstance().setCurrentImageFolderPosition(position);
                mFolderPopupWindow.dismiss();
                VMFolderBean VMFolderBean = (VMFolderBean) adapterView.getAdapter()
                        .getItem(position);
                if (null != VMFolderBean) {
                    //                    mImageGridAdapter.refreshData(VMFolderBean.images);
                    mPictureAdapter.refresh(VMFolderBean.images);
                    mCurrDirView.setText(VMFolderBean.name);
                }
            }
        });
        mFolderPopupWindow.setMargin(mFooterBar.getHeight());
    }

    @Override
    public void onLoadComplete(List<VMFolderBean> VMFolderBeans) {
        this.mFolderBeans = VMFolderBeans;
        VMPicker.getInstance().setImageFolders(VMFolderBeans);
        if (VMFolderBeans.size() == 0) {
            mPictureAdapter.refresh(null);
        } else {
            mPictureAdapter.refresh(VMFolderBeans.get(0).images);
        }
        mPictureAdapter.setClickListener(new VMAdapter.IClickListener() {
            @Override
            public void onItemAction(int position, Object object) {
                // 根据是否有相机按钮确定位置
                position = VMPicker.getInstance().isShowCamera() ? position - 1 : position;
                if (VMPicker.getInstance().isMultiMode()) {
                    Intent intent = new Intent(VMPickGridActivity.this, VMPickPreviewActivity.class);
                    intent.putExtra(VMPicker.EXTRA_SELECTED_IMAGE_POSITION, position);

                    /**
                     * 2017-03-20
                     *
                     * 依然采用弱引用进行解决，采用单例加锁方式处理
                     */

                    // 据说这样会导致大量图片的时候崩溃
                    //            intent.putExtra(VMPicker.EXTRA_IMAGE_ITEMS, VMPicker.getCurrentImageFolderItems());

                    // 但采用弱引用会导致预览弱引用直接返回空指针
                    DataHolder.getInstance().save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, VMPicker.getInstance().getCurrentImageFolderItems());
                    intent.putExtra(VMPickPreviewActivity.ISORIGIN, isOrigin);
                    startActivityForResult(intent, VMPicker.REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面
                } else {
                    VMPicker.getInstance().clearSelectedImages();
                    VMPicker.getInstance().addSelectedImageItem(position, VMPicker.getInstance().getCurrentImageFolderItems().get(position), true);
                    if (VMPicker.getInstance().isCrop()) {
                        Intent intent = new Intent(VMPickGridActivity.this, VMPickCropActivity.class);
                        startActivityForResult(intent, VMPicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, VMPicker.getInstance()
                                .getSelectedImages());
                        setResult(VMPicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                        finish();
                    }
                }
            }

            @Override
            public boolean onItemLongAction(int action, Object object) {
                return false;
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
        mRecyclerView.addItemDecoration(new VMSpaceGridDecoration(4, VMDimen.dp2px(2)));
//        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, VMDimen.dp2px(4), false));
        mRecyclerView.setAdapter(mPictureAdapter);
        mFolderAdapter.refreshData(VMFolderBeans);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onImageSelected(int position, VMPictureBean item, boolean isAdd) {
        if (VMPicker.getInstance().getSelectImageCount() > 0) {
            mOKBtn.setText(getString(R.string.ip_select_complete, VMPicker.getInstance()
                    .getSelectImageCount(), VMPicker.getInstance().getSelectLimit()));
            mOKBtn.setEnabled(true);
            mPreviewBtn.setEnabled(true);
            mPreviewBtn.setText(getResources().getString(R.string.ip_preview_count, VMPicker.getInstance()
                    .getSelectImageCount()));
            //mPreviewBtn.setTextColor(ContextCompat.getColor(this, R.color.ip_text_primary_inverted));
            //mOKBtn.setTextColor(ContextCompat.getColor(this, R.color.ip_text_primary_inverted));
        } else {
            mOKBtn.setText(getString(R.string.ip_complete));
            mOKBtn.setEnabled(false);
            mPreviewBtn.setEnabled(false);
            mPreviewBtn.setText(getResources().getString(R.string.ip_preview));
            //mPreviewBtn.setTextColor(ContextCompat.getColor(this, R.color.ip_text_secondary_inverted));
            //mOKBtn.setTextColor(ContextCompat.getColor(this, R.color.ip_text_secondary_inverted));
        }
        //        mImageGridAdapter.notifyDataSetChanged();
        //        mPictureAdapter.notifyItemChanged(position); // 17/4/21 fix the position while click img to preview
        //        mPictureAdapter.notifyItemChanged(position + (VMPicker.isShowCamera() ? 1 : 0));// 17/4/24  fix the position while click right bottom preview button
        for (int i = VMPicker.getInstance().isShowCamera() ? 1 : 0; i < mPictureAdapter.getItemCount(); i++) {
            if (mPictureAdapter.getItemData(i).path != null && mPictureAdapter.getItemData(i).path.equals(item.path)) {
                mPictureAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (resultCode == VMPicker.RESULT_CODE_BACK) {
                isOrigin = data.getBooleanExtra(VMPickPreviewActivity.ISORIGIN, false);
            } else {
                //从拍照界面返回
                //点击 X , 没有选择照片
                if (data.getSerializableExtra(VMPicker.EXTRA_RESULT_ITEMS) == null) {
                    //什么都不做，直接返回
                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    setResult(VMPicker.RESULT_CODE_ITEMS, data);
                }
                finish();
            }
        } else {
            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (resultCode == RESULT_OK && requestCode == VMPicker.REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                VMPicker.galleryAddPic(activity, VMPicker.getInstance().getTakeImageFile());

                /**
                 * 2017-03-21 对机型做旋转处理
                 */
                String path = VMPicker.getInstance().getTakeImageFile().getAbsolutePath();

                VMPictureBean VMPictureBean = new VMPictureBean();
                VMPictureBean.path = path;
                VMPicker.getInstance().clearSelectedImages();
                VMPicker.getInstance().addSelectedImageItem(0, VMPictureBean, true);
                if (VMPicker.getInstance().isCrop()) {
                    Intent intent = new Intent(activity, VMPickCropActivity.class);
                    startActivityForResult(intent, VMPicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, VMPicker.getInstance()
                            .getSelectedImages());
                    setResult(VMPicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                    finish();
                }
            } else if (isDirectCamera) {
                finish();
            }
        }
    }
}