package com.vmloft.develop.library.tools.picker.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.permission.VMPermission;
import com.vmloft.develop.library.tools.picker.VMPickScanPicture;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.adapter.VMFolderAdapter;
import com.vmloft.develop.library.tools.adapter.VMSpaceGridDecoration;
import com.vmloft.develop.library.tools.picker.adapter.VMPictureAdapter;
import com.vmloft.develop.library.tools.picker.bean.VMFolderBean;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.widget.FolderPopupWindow;

import com.vmloft.develop.library.tools.utils.VMDimen;

import com.vmloft.develop.library.tools.utils.VMNavBarUtil;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.widget.toast.VMToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Crete by lzan13 on 2019/05/19 11:32
 *
 * 图片选择界面
 */
public class VMPickGridActivity extends VMPickBaseActivity {

    // 图片文件夹适配器
    private VMFolderAdapter mFolderAdapter;
    // 展示图片文件夹列表的 PopupWindow
    private FolderPopupWindow mFolderPopupWindow;
    // 图片文件夹数据集合
    private List<VMFolderBean> mFolderBeans;

    // 是否显示相机
    private boolean isShowCamera = false;
    // 默认不是直接调取相机
    private boolean isDirectCamera = false;
    // 是否选中原图
    private boolean isOrigin = false;

    // 底部栏
    private View mBottomBar;
    private View mBottomSpaceView;
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
    private VMPicker.OnSelectedPictureListener mSelectedPictureListener;

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

        mBottomBar = findViewById(R.id.vm_pick_grid_bottom_bar_rl);
        mBottomSpaceView = findViewById(R.id.vm_pick_grid_bottom_space);
        mChangeDirView = findViewById(R.id.vm_pick_grid_choose_folder_rl);
        mCurrDirView = findViewById(R.id.vm_pick_grid_choose_folder_tv);
        mPreviewBtn = findViewById(R.id.vm_pick_grid_preview_btn);

        mPreviewBtn.setOnClickListener(viewListener);
        mChangeDirView.setOnClickListener(viewListener);
        getTopBar().setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinish();
            }
        });

        if (VMPicker.getInstance().isMultiMode()) {
            getTopBar().setEndBtnListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    List<VMPictureBean> result = VMPicker.getInstance().getSelectedPictures();
                    intent.putParcelableArrayListExtra(VMConstant.KEY_PICK_RESULT_PICTURES, (ArrayList<? extends Parcelable>) result);
                    setResult(VMConstant.VM_PICK_RESULT_CODE_PICTURES, intent);
                    onFinish();
                }
            });
            mPreviewBtn.setVisibility(View.VISIBLE);
        } else {
            getTopBar().setEndBtn("");
            mPreviewBtn.setVisibility(View.GONE);
        }

        initPictureRecyclerView();

        initNavBarListener();
    }

    @Override
    protected void initData() {
        // 每次打开都重置选择器
        //VMPicker.getInstance().reset();

        isShowCamera = VMPicker.getInstance().isShowCamera();
        List<VMPictureBean> pictures = (List<VMPictureBean>) getIntent().getSerializableExtra(VMConstant.VM_KEY_PICK_PICTURES);
        VMPicker.getInstance().setSelectedPictures(pictures);

        mFolderAdapter = new VMFolderAdapter(mActivity, null);

        refreshBtnStatus();
        // 初始化扫描图片， 要先扫描图片
        initScanPicture();
        // 初始化选择图片监听
        initSelectPictureListener();
    }

    /**
     * 初始化图片列表
     */
    private void initPictureRecyclerView() {
        mPictureAdapter = new VMPictureAdapter(mActivity, null);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
        mRecyclerView.addItemDecoration(new VMSpaceGridDecoration(4, VMDimen.dp2px(2)));
        mRecyclerView.setAdapter(mPictureAdapter);
        mPictureAdapter.setClickListener((int position, Object object) -> {
            onPictureClick(position);
        });
    }

    /**
     * 初始化底部导航栏变化监听
     */
    private void initNavBarListener() {
        VMNavBarUtil.with(this).setListener(new VMNavBarUtil.OnNavBarChangeListener() {
            @Override
            public void onShow(int orientation, int height) {
                mBottomSpaceView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = mBottomSpaceView.getLayoutParams();
                if (layoutParams.height == 0) {
                    layoutParams.height = VMDimen.getNavigationBarHeight();
                    mBottomSpaceView.requestLayout();
                }
            }

            @Override
            public void onHide(int orientation) {
                mBottomSpaceView.setVisibility(View.GONE);
            }
        });
        VMNavBarUtil.with(this, VMNavBarUtil.ORIENTATION_HORIZONTAL).setListener(new VMNavBarUtil.OnNavBarChangeListener() {
            @Override
            public void onShow(int orientation, int height) {
                mTopBar.setPadding(0, 0, height, 0);
                mBottomBar.setPadding(0, 0, height, 0);
            }

            @Override
            public void onHide(int orientation) {
                mTopBar.setPadding(0, 0, 0, 0);
                mBottomBar.setPadding(0, 0, 0, 0);
            }
        });
    }

    /**
     * 扫描图片
     */
    private void initScanPicture() {
        mScanPictureListener = new VMPickScanPicture.OnScanPictureListener() {
            @Override
            public void onLoadComplete(List<VMFolderBean> folderBeans) {
                mFolderBeans = folderBeans;
                if (mFolderBeans.size() == 0) {
                    mPictureAdapter.refresh(null);
                } else {
                    mPictureAdapter.refresh(mFolderBeans.get(0).pictures);
                }
                mFolderAdapter.refreshData(mFolderBeans);
            }
        };
        // 检查权限
        if (!VMPermission.getInstance(mActivity).checkStorage()) {
            VMPermission.getInstance(mActivity).requestStorage(new VMPermission.PCallback() {
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
     * 初始化选择图片监听
     */
    private void initSelectPictureListener() {
        mSelectedPictureListener = new VMPicker.OnSelectedPictureListener() {
            @Override
            public void onPictureSelected(int position, VMPictureBean bean, boolean isAdd) {
                refreshBtnStatus();
                mPictureAdapter.notifyItemChanged(position, 1);
            }
        };
        VMPicker.getInstance().addOnSelectedPictureListener(mSelectedPictureListener);
    }

    /**
     * 刷新确认以及预览按钮
     */
    private void refreshBtnStatus() {
        int selectCount = VMPicker.getInstance().getSelectPictureCount();
        int selectLimit = VMPicker.getInstance().getSelectLimit();
        if (VMPicker.getInstance().isMultiMode()) {
            if (selectCount > 0) {
                getTopBar().setEndBtn(VMStr.byResArgs(R.string.vm_pick_complete_select, selectCount, selectLimit));
                getTopBar().setEndBtnEnable(true);
                mPreviewBtn.setEnabled(true);
                mPreviewBtn.setText(VMStr.byResArgs(R.string.vm_pick_preview_count, selectCount));
            } else {
                getTopBar().setEndBtn(VMStr.byRes(R.string.vm_pick_complete));
                getTopBar().setEndBtnEnable(false);
                mPreviewBtn.setEnabled(false);
                mPreviewBtn.setText(getResources().getString(R.string.vm_pick_preview));
            }
        }
    }

    /**
     * 点击图片列表
     */
    private void onPictureClick(int position) {
        // 判断第一个是不是相机，如果是，特殊处理
        if (VMPicker.getInstance().isShowCamera() && position == 0) {
            openCamera();
        } else {
            position = isShowCamera ? position - 1 : position;
            if (VMPicker.getInstance().isMultiMode()) {
                Intent intent = new Intent(VMPickGridActivity.this, VMPickPreviewActivity.class);
                intent.putExtra(VMConstant.KEY_PICK_CURRENT_SELECTED_POSITION, position);
                intent.putExtra(VMConstant.KEY_PICK_PREVIEW_ALL, true);

                intent.putExtra(VMConstant.VM_KEY_PICK_IS_ORIGIN, isOrigin);
                startActivityForResult(intent, VMConstant.VM_PICK_REQUEST_CODE_PREVIEW);  //如果是多选，点击图片进入预览界面
            } else {
                VMPicker.getInstance().clearSelectedPictures();
                VMPicker.getInstance().addSelectedPicture(position, VMPicker.getInstance().getCurrentFolderPictures().get(position), true);
                if (VMPicker.getInstance().isCrop()) {
                    Intent intent = new Intent(mActivity, VMPickCropActivity.class);
                    startActivityForResult(intent, VMConstant.VM_PICK_REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    Intent intent = new Intent();
                    List<VMPictureBean> result = VMPicker.getInstance().getSelectedPictures();
                    intent.putParcelableArrayListExtra(VMConstant.KEY_PICK_RESULT_PICTURES, (ArrayList<? extends Parcelable>) result);
                    setResult(VMConstant.VM_PICK_RESULT_CODE_PICTURES, intent);   //单选不需要裁剪，返回数据
                    finish();
                }
            }
        }
    }

    /**
     * 开启相机，先检查是否有相机权限
     */
    private void openCamera() {
        if (!VMPermission.getInstance(mActivity).checkCamera()) {
            VMPermission.getInstance(mActivity).requestCamera(new VMPermission.PCallback() {
                @Override
                public void onReject() {
                    VMToast.make(mActivity, "访问相机 权限被拒绝，无法使用此功能").error();
                }

                @Override
                public void onComplete() {
                    VMPicker.getInstance().takePicture(mActivity, VMConstant.VM_PICK_REQUEST_CODE_TAKE);
                }
            });
        } else {
            VMPicker.getInstance().takePicture(mActivity, VMConstant.VM_PICK_REQUEST_CODE_TAKE);
        }
    }

    /**
     * 弹出文件夹选择列表
     */
    private void showFolderList() {
        if (mFolderBeans == null) {
            VMToast.make(mActivity, "没有更多图片可供选择").error();
            return;
        }
        mFolderPopupWindow = new FolderPopupWindow(mActivity, mFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener((AdapterView<?> adapterView, View view, int position, long l) -> {
            mFolderAdapter.setSelectIndex(position);
            VMPicker.getInstance().setCurrentFolderPosition(position);
            mFolderPopupWindow.dismiss();
            VMFolderBean folderBean = (VMFolderBean) adapterView.getAdapter().getItem(position);
            if (null != folderBean) {
                mPictureAdapter.refresh(folderBean.pictures);
                mCurrDirView.setText(folderBean.name);
            }
        });
        mFolderPopupWindow.setMargin(mBottomBar.getHeight());

        //点击文件夹按钮
        mFolderAdapter.refreshData(mFolderBeans);  //刷新数据
        if (mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.dismiss();
        } else {
            mFolderPopupWindow.showAtLocation(mBottomBar, Gravity.NO_GRAVITY, 0, 0);
            //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
            int index = mFolderAdapter.getSelectIndex();
            index = index == 0 ? index : index - 1;
            mFolderPopupWindow.setSelection(index);
        }
    }

    /**
     * 界面控件点击事件
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.vm_pick_grid_choose_folder_rl) {
                showFolderList();
            } else if (v.getId() == R.id.vm_pick_grid_preview_btn) {
                Intent intent = new Intent(mActivity, VMPickPreviewActivity.class);
                intent.putExtra(VMConstant.KEY_PICK_CURRENT_SELECTED_POSITION, 0);
                intent.putExtra(VMConstant.VM_KEY_PICK_IS_ORIGIN, isOrigin);
                intent.putExtra(VMConstant.KEY_PICK_PREVIEW_ALL, false);
                startActivityForResult(intent, VMConstant.VM_PICK_REQUEST_CODE_PREVIEW);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (resultCode == VMConstant.VM_PICK_RESULT_CODE_BACK) {
                isOrigin = data.getBooleanExtra(VMConstant.VM_KEY_PICK_IS_ORIGIN, false);
            } else {
                //从拍照界面返回
                //点击 X , 没有选择照片
                if (data.getSerializableExtra(VMConstant.KEY_PICK_RESULT_PICTURES) == null) {
                    //什么都不做，直接返回
                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    setResult(VMConstant.VM_PICK_RESULT_CODE_PICTURES, data);
                }
                onFinish();
            }
        } else {
            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (resultCode == RESULT_OK && requestCode == VMConstant.VM_PICK_REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                VMPicker.notifyGalleryChange(mActivity, VMPicker.getInstance().getTakeImageFile());

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
                    startActivityForResult(intent, VMConstant.VM_PICK_REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    Intent intent = new Intent();
                    List<VMPictureBean> result = VMPicker.getInstance().getSelectedPictures();
                    intent.putParcelableArrayListExtra(VMConstant.KEY_PICK_RESULT_PICTURES, (ArrayList<? extends Parcelable>) result);
                    setResult(VMConstant.VM_PICK_RESULT_CODE_PICTURES, intent);   //单选不需要裁剪，返回数据
                    onFinish();
                }
            } else if (isDirectCamera) {
                onFinish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mSelectedPictureListener != null) {
            VMPicker.getInstance().removeOnSelectedPictureListener(mSelectedPictureListener);
            mSelectedPictureListener = null;
        }
        super.onDestroy();
    }
}