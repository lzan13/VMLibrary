package com.vmloft.develop.library.tools.picker.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.vmloft.develop.library.tools.R;

import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.adapter.VMPreviewPageAdapter;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.utils.VMNavBarUtil;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.widget.VMViewPager;

import com.vmloft.develop.library.tools.widget.toast.VMToast;
import java.util.ArrayList;

/**
 * Create by lzan13 on 2019/05/17
 *
 * 图片预览界面
 */
public class VMPickPreviewActivity extends VMPickBaseActivity implements CompoundButton.OnCheckedChangeListener {

    // 预览图片集合
    protected ArrayList<VMPictureBean> mPictures;
    // 所以已选择图片
    protected ArrayList<VMPictureBean> mSelectedPictures;
    // 当前位置
    protected int mCurrentPosition = 0;
    protected VMViewPager mViewPager;
    // 预览适配器
    protected VMPreviewPageAdapter mAdapter;
    // 是否预览整个文件夹
    protected boolean isPreviewFolder = false;
    // 是否选中原图
    private boolean isOrigin;
    // 是否选中当前图片
    private CheckBox mSelectCB;
    // 原图
    private CheckBox mOriginCB;
    // 确认图片的选择
    private View mBottomBar;
    private View mSpaceView;

    // 图片扫描回调接口
    private VMPicker.OnSelectedPictureListener mSelectedPictureListener;

    @Override
    protected int layoutId() {
        return R.layout.vm_activity_pick_preview;
    }

    /**
     * 初始化
     */
    @Override
    protected void initUI() {
        super.initUI();
        mBottomBar = findViewById(R.id.vm_preview_bottom_bar);
        mSelectCB = findViewById(R.id.vm_preview_select_cb);
        mOriginCB = findViewById(R.id.vm_preview_origin_cb);
        mSpaceView = findViewById(R.id.vm_preview_bottom_space);
    }

    @Override
    protected void initData() {

        mCurrentPosition = getIntent().getIntExtra(VMConstant.KEY_PICK_CURRENT_SELECTED_POSITION, 0);
        isPreviewFolder = getIntent().getBooleanExtra(VMConstant.KEY_PICK_PREVIEW_ALL, false);
        isOrigin = getIntent().getBooleanExtra(VMConstant.VM_KEY_PICK_IS_ORIGIN, false);

        if (isPreviewFolder) {
            mPictures = VMPicker.getInstance().getCurrentFolderPictures();
        } else {
            mPictures = VMPicker.getInstance().getSelectedPictures();
        }

        mSelectedPictures = VMPicker.getInstance().getSelectedPictures();
        getTopBar().setTitle(VMStr.byResArgs(R.string.vm_pick_preview_picture_count, mCurrentPosition + 1, mPictures.size()));
        getTopBar().setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(VMConstant.VM_KEY_PICK_IS_ORIGIN, isOrigin);
                setResult(VMConstant.VM_PICK_RESULT_CODE_BACK, intent);
                onFinish();
            }
        });
        getTopBar().setEndBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VMPicker.getInstance().getSelectedPictures().size() == 0) {
                    mSelectCB.setChecked(true);
                    VMPictureBean bean = mPictures.get(mCurrentPosition);
                    VMPicker.getInstance().addSelectedPicture(mCurrentPosition, bean, mSelectCB.isChecked());
                }
                Intent intent = new Intent();
                intent.putExtra(VMConstant.KEY_PICK_RESULT_PICTURES, VMPicker.getInstance().getSelectedPictures());
                setResult(VMConstant.VM_PICK_RESULT_CODE_PICTURES, intent);
                onFinish();
            }
        });

        mOriginCB.setText(getString(R.string.vm_pick_origin));
        mOriginCB.setOnCheckedChangeListener(this);
        mOriginCB.setChecked(isOrigin);

        // 初始化当前页面的状态
        VMPictureBean item = mPictures.get(mCurrentPosition);
        mSelectCB.setChecked(VMPicker.getInstance().isSelectPicture(item));

        initViewPager();

        initSelectedPictureListener();

        initNavBarListener();

        //当点击当前选中按钮的时候，需要根据当前的选中状态添加和移除图片
        mSelectCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VMPictureBean VMPictureBean = mPictures.get(mCurrentPosition);
                int selectLimit = VMPicker.getInstance().getSelectLimit();
                if (mSelectCB.isChecked() && mSelectedPictures.size() >= selectLimit) {
                    VMToast.make(mActivity, VMStr.byResArgs(R.string.vm_pick_select_limit, selectLimit)).error();
                    mSelectCB.setChecked(false);
                } else {
                    VMPicker.getInstance().addSelectedPicture(mCurrentPosition, VMPictureBean, mSelectCB.isChecked());
                }
            }
        });
    }

    /**
     * 初始化底部导航栏变化监听
     */
    private void initNavBarListener() {
        VMNavBarUtil.with(this).setListener(new VMNavBarUtil.OnNavBarChangeListener() {
            @Override
            public void onShow(int orientation, int height) {
                mSpaceView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = mSpaceView.getLayoutParams();
                if (layoutParams.height == 0) {
                    layoutParams.height = VMDimen.getNavigationBarHeight();
                    mSpaceView.requestLayout();
                }
            }

            @Override
            public void onHide(int orientation) {
                mSpaceView.setVisibility(View.GONE);
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
     * 初始化预览适配器
     */
    private void initViewPager() {
        mViewPager = findViewById(R.id.vm_pick_preview_viewpager);
        mAdapter = new VMPreviewPageAdapter(mActivity, mPictures);
        mAdapter.setPreviewClickListener(new VMPreviewPageAdapter.OnPreviewClickListener() {
            @Override
            public void onPreviewClick(View view, float v, float v1) {
                onPictureClick();
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);

        // ViewPager 滑动的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                VMPictureBean item = mPictures.get(mCurrentPosition);
                boolean isSelected = VMPicker.getInstance().isSelectPicture(item);
                mSelectCB.setChecked(isSelected);
                getTopBar().setTitle(VMStr.byResArgs(R.string.vm_pick_preview_picture_count, mCurrentPosition + 1, mPictures.size()));
            }
        });
    }

    /**
     * 初始化图片选择回调
     * 图片添加成功后，修改当前图片的选中数量
     */
    private void initSelectedPictureListener() {
        mSelectedPictureListener = new VMPicker.OnSelectedPictureListener() {
            @Override
            public void onPictureSelected(int position, VMPictureBean item, boolean isAdd) {
                if (VMPicker.getInstance().getSelectPictureCount() > 0) {
                    int selectCount = VMPicker.getInstance().getSelectPictureCount();
                    int selectLimit = VMPicker.getInstance().getSelectLimit();
                    String complete = VMStr.byResArgs(R.string.vm_pick_complete_select, selectCount, selectLimit);
                    getTopBar().setEndBtn(complete);
                } else {
                    getTopBar().setEndBtn(VMStr.byRes(R.string.vm_pick_complete));
                }

                if (mOriginCB.isChecked()) {
                    long size = 0;
                    for (VMPictureBean VMPictureBean : mSelectedPictures) {
                        size += VMPictureBean.size;
                    }
                    String fileSize = Formatter.formatFileSize(mActivity, size);
                    mOriginCB.setText(getString(R.string.vm_pick_origin_size, fileSize));
                }
            }
        };
        VMPicker.getInstance().addOnSelectedPictureListener(mSelectedPictureListener);
        mSelectedPictureListener.onPictureSelected(0, null, false);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(VMConstant.VM_KEY_PICK_IS_ORIGIN, isOrigin);
        setResult(VMConstant.VM_PICK_RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.vm_preview_origin_cb) {
            if (isChecked) {
                long size = 0;
                for (VMPictureBean item : mSelectedPictures) {
                    size += item.size;
                }
                String fileSize = Formatter.formatFileSize(this, size);
                isOrigin = true;
                mOriginCB.setText(getString(R.string.vm_pick_origin_size, fileSize));
            } else {
                isOrigin = false;
                mOriginCB.setText(getString(R.string.vm_pick_origin));
            }
        }
    }

    /**
     * 单击时，隐藏头和尾
     */
    public void onPictureClick() {
        if (mTopBar.getVisibility() == View.VISIBLE) {
            mTopBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_fade_out));
            mBottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_fade_out));
            mTopBar.setVisibility(View.GONE);
            mBottomBar.setVisibility(View.GONE);
        } else {
            mTopBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_fade_in));
            mBottomBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.vm_fade_in));
            mTopBar.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        VMPicker.getInstance().removeOnSelectedPictureListener(mSelectedPictureListener);
        super.onDestroy();
    }
}
