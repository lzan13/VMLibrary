package com.vmloft.develop.library.tools.picker.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.permission.VMPermission;
import com.vmloft.develop.library.tools.permission.VMPermissionCallback;
import com.vmloft.develop.library.tools.picker.DataHolder;
import com.vmloft.develop.library.tools.picker.VMPickScanPicture;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.adapter.VMFolderAdapter;
import com.vmloft.develop.library.tools.adapter.VMSpaceGridDecoration;
import com.vmloft.develop.library.tools.picker.adapter.VMPictureAdapter;
import com.vmloft.develop.library.tools.picker.bean.VMFolderBean;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.widget.FolderPopupWindow;

import com.vmloft.develop.library.tools.utils.VMDimen;

import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.widget.toast.VMToast;
import java.util.ArrayList;
import java.util.List;

/**
 * Crete by lzan13 on 2019/05/19 11:32
 *
 * 图片选择界面
 */
public class VMPickGridActivity extends VMPickBaseActivity implements VMPicker.OnSelectedPictureListener {

    public static final String EXTRAS_TAKE_PICKERS = "TAKE";
    public static final String EXTRAS_IMAGES = "IMAGES";

    // 图片文件夹适配器
    private VMFolderAdapter mFolderAdapter;
    // 展示图片文件夹列表的 PopupWindow
    private FolderPopupWindow mFolderPopupWindow;
    // 图片文件夹数据集合
    private List<VMFolderBean> mFolderBeans;

    // 默认不是直接调取相机
    private boolean isDirectCamera = false;
    // 是否选中原图
    private boolean isOrigin = false;

    // 底部栏
    private View mFooterBar;
    // 文件夹切换按钮
    private View mChangeDirView;
    // 显示当前文件夹
    private TextView mCurrDirView;
    // 预览按钮
    private TextView mPreviewBtn;

    // 使用 RecyclerView 展示图片
    private RecyclerView mRecyclerView;
    private VMPictureAdapter mPictureAdapter;
    // 图片扫描回调接口
    private VMPickScanPicture.OnScanPictureListener mScanPictureListener;

    @Override
    protected int layoutId() {
        return R.layout.vm_activity_pick_grid;
    }

    /**
     * 初始化
     */
    @Override
    protected void initUI() {
        super.initUI();
        mRecyclerView = findViewById(R.id.vm_pick_grid_recycler_view);
        mFooterBar = findViewById(R.id.vm_pick_grid_bottom_bar_rl);
        mChangeDirView = findViewById(R.id.vm_pick_grid_choose_folder_rl);
        mCurrDirView = findViewById(R.id.vm_pick_grid_choose_folder_tv);
        mPreviewBtn = findViewById(R.id.vm_pick_grid_preview_btn);
    }

    @Override
    protected void initData() {
        // 每次打开都重置选择器
        VMPicker.getInstance().reset();

        VMPicker.getInstance().addOnSelectedPictureListener(this);

        Intent data = getIntent();
        // 新增可直接拍照
        if (data != null && data.getExtras() != null) {
            isDirectCamera = data.getBooleanExtra(EXTRAS_TAKE_PICKERS, false); // 默认不是直接打开相机
            if (isDirectCamera) {
                openCamera();
            }
            ArrayList<VMPictureBean> images = (ArrayList<VMPictureBean>) data.getSerializableExtra(EXTRAS_IMAGES);
            VMPicker.getInstance().setSelectedPictures(images);
        }

        getTopBar().setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });

        mPreviewBtn.setOnClickListener(viewListener);
        mChangeDirView.setOnClickListener(viewListener);

        if (VMPicker.getInstance().isMultiMode()) {
            getTopBar().setEndBtnListener("完成", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, VMPicker.getInstance()
                        .getSelectedPictures());
                    setResult(VMPicker.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
                    onFinish();
                }
            });
            mPreviewBtn.setVisibility(View.VISIBLE);
        } else {
            getTopBar().setEndBtn("");
            mPreviewBtn.setVisibility(View.GONE);
        }

        mFolderAdapter = new VMFolderAdapter(mActivity, null);
        mPictureAdapter = new VMPictureAdapter(mActivity, null);

        onPictureSelected(0, null, false);
        // 初始化完成 扫描图片
        scanPicture();
    }

    /**
     * 扫描图片
     */
    private void scanPicture() {
        mScanPictureListener = new VMPickScanPicture.OnScanPictureListener() {
            @Override
            public void onLoadComplete(List<VMFolderBean> folderBeans) {
                loadPictureComplete(folderBeans);
            }
        };
        // 检查权限
        if (!VMPermission.getInstance(mActivity).checkStorage()) {
            VMPermission.getInstance(mActivity).requestStorage(new VMPermissionCallback() {
                @Override
                public void onReject() {
                    VMToast.make(mActivity, "读写手机存储 权限被拒绝，无法使用此功能").error();
                }

                @Override
                public void onComplete() {
                    new VMPickScanPicture(mActivity, null, mScanPictureListener);
                }
            });
        } else {
            new VMPickScanPicture(mActivity, null, mScanPictureListener);
        }
    }

    /**
     * 开启相机
     */
    private void openCamera() {
        // 检查是否有相机权限
        if (!VMPermission.getInstance(mActivity).checkCamera()) {
            VMPermission.getInstance(mActivity).requestCamera(new VMPermissionCallback() {
                @Override
                public void onReject() {
                    VMToast.make(mActivity, "访问相机 权限被拒绝，无法使用此功能").error();
                }

                @Override
                public void onComplete() {
                    VMPicker.getInstance().takePicture(mActivity, VMPicker.REQUEST_CODE_TAKE);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        VMPicker.getInstance().removeOnSelectedPictureListener(this);
        super.onDestroy();
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vm_pick_grid_choose_folder_rl) {
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
            } else if (v.getId() == R.id.vm_pick_grid_preview_btn) {
                Intent intent = new Intent(VMPickGridActivity.this, VMPickPreviewActivity.class);
                intent.putExtra(VMPicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
                intent.putExtra(VMPicker.EXTRA_IMAGE_ITEMS, VMPicker.getInstance()
                    .getSelectedPictures());
                intent.putExtra(VMPickPreviewActivity.ISORIGIN, isOrigin);
                intent.putExtra(VMPicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intent, VMPicker.REQUEST_CODE_PREVIEW);
            }
        }
    };

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopupWindow(mActivity, mFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener(new FolderPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mFolderAdapter.setSelectIndex(position);
                VMPicker.getInstance().setCurrentFolderPosition(position);
                mFolderPopupWindow.dismiss();
                VMFolderBean VMFolderBean = (VMFolderBean) adapterView.getAdapter()
                    .getItem(position);
                if (null != VMFolderBean) {
                    //                    mImageGridAdapter.refreshData(VMFolderBean.pictures);
                    mPictureAdapter.refresh(VMFolderBean.pictures);
                    mCurrDirView.setText(VMFolderBean.name);
                }
            }
        });
        mFolderPopupWindow.setMargin(mFooterBar.getHeight());
    }

    /**
     * 扫描图片完成
     *
     * @param folderBeans 扫描结果文件夹列表
     */
    private void loadPictureComplete(List<VMFolderBean> folderBeans) {
        mFolderBeans = folderBeans;
        VMPicker.getInstance().setFolderBeans(mFolderBeans);
        if (mFolderBeans.size() == 0) {
            mPictureAdapter.refresh(null);
        } else {
            mPictureAdapter.refresh(mFolderBeans.get(0).pictures);
        }
        mPictureAdapter.setClickListener(new VMAdapter.IClickListener() {
            @Override
            public void onItemAction(int position, Object object) {
                // 判断第一个是不是相机，如果是，特殊处理
                if (VMPicker.getInstance().isShowCamera() && position == 0) {
                    openCamera();
                } else {
                    if (VMPicker.getInstance().isMultiMode()) {
                        Intent intent = new Intent(VMPickGridActivity.this, VMPickPreviewActivity.class);
                        intent.putExtra(VMPicker.EXTRA_SELECTED_IMAGE_POSITION, position);

                        /**
                         * 依然采用弱引用进行解决，采用单例加锁方式处理
                         */
                        // 据说这样会导致大量图片的时候崩溃
                        //            intent.putExtra(VMPicker.EXTRA_IMAGE_ITEMS, VMPicker.getCurrentFolderPictures());

                        // 但采用弱引用会导致预览弱引用直接返回空指针
                        DataHolder.getInstance()
                            .save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, VMPicker.getInstance()
                                .getCurrentFolderPictures());
                        intent.putExtra(VMPickPreviewActivity.ISORIGIN, isOrigin);
                        startActivityForResult(intent, VMPicker.REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面
                    } else {
                        VMPicker.getInstance().clearSelectedPictures();
                        VMPicker.getInstance().addSelectedPicture(position, VMPicker.getInstance()
                            .getCurrentFolderPictures()
                            .get(position), true);
                        if (VMPicker.getInstance().isCrop()) {
                            Intent intent = new Intent(VMPickGridActivity.this, VMPickCropActivity.class);
                            startActivityForResult(intent, VMPicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, VMPicker.getInstance()
                                .getSelectedPictures());
                            setResult(VMPicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                            finish();
                        }
                    }
                }
            }

            @Override
            public boolean onItemLongAction(int action, Object object) {
                return false;
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
        mRecyclerView.addItemDecoration(new VMSpaceGridDecoration(4, VMDimen.dp2px(2)));
        //        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, VMDimen.dp2px(4), false));
        mRecyclerView.setAdapter(mPictureAdapter);
        mFolderAdapter.refreshData(mFolderBeans);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onPictureSelected(int position, VMPictureBean item, boolean isAdd) {
        int selectCount = VMPicker.getInstance().getSelectPictureCount();
        int selectLimit = VMPicker.getInstance().getSelectLimit();
        if (VMPicker.getInstance().isMultiMode()) {
            if (selectCount > 0) {
                getTopBar().setEndBtn(VMStr.byResArgs(R.string.vm_pick_complete_select, selectCount, selectLimit));
                getTopBar().setEndBtnEnable(true);
                mPreviewBtn.setEnabled(true);
                mPreviewBtn.setText(getResources().getString(R.string.vm_pick_preview_count, VMPicker
                    .getInstance()
                    .getSelectPictureCount()));
            } else {
                getTopBar().setEndBtn(VMStr.byRes(R.string.vm_pick_complete));
                getTopBar().setEndBtnEnable(false);
                mPreviewBtn.setEnabled(false);
                mPreviewBtn.setText(getResources().getString(R.string.vm_pick_preview));
            }
        }

        for (int i = VMPicker.getInstance()
            .isShowCamera() ? 1 : 0; i < mPictureAdapter.getItemCount(); i++) {
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
                VMPicker.galleryAddPic(mActivity, VMPicker.getInstance().getTakeImageFile());

                /**
                 * 2017-03-21 对机型做旋转处理
                 */
                String path = VMPicker.getInstance().getTakeImageFile().getAbsolutePath();

                VMPictureBean VMPictureBean = new VMPictureBean();
                VMPictureBean.path = path;
                VMPicker.getInstance().clearSelectedPictures();
                VMPicker.getInstance().addSelectedPicture(0, VMPictureBean, true);
                if (VMPicker.getInstance().isCrop()) {
                    Intent intent = new Intent(mActivity, VMPickCropActivity.class);
                    startActivityForResult(intent, VMPicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, VMPicker.getInstance()
                        .getSelectedPictures());
                    setResult(VMPicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                    finish();
                }
            } else if (isDirectCamera) {
                finish();
            }
        }
    }

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
}