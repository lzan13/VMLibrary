package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMColor;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Create by lzan13 on 2019/04/16
 *
 * 自定义编辑框控件
 */
public class VMEditView extends RelativeLayout {

    // 输入框
    private TextInputEditText mInputView;
    // 清空按钮
    private ImageView mClearIcon;
    // 显示隐藏按钮
    private ImageView mEyeIcon;

    // 控件属性
    // 文字
    private int mTextColor;
    private int mTextSize;
    private String mHint;
    // 输入模式
    private int mMode;

    // 是否启用清空图标也眼睛图标
    private boolean mEnableClear;
    private boolean mEnableEye;
    private int mClearRes;
    private int mEyeRes;

    public VMEditView(Context context) {
        this(context, null);
    }

    public VMEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VMEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化方法，获取属性等操作
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.vm_widget_edit, this);
        mInputView = findViewById(R.id.vm_edit_input_view);
        mClearIcon = findViewById(R.id.vm_edit_clear_icon);
        mEyeIcon = findViewById(R.id.vm_edit_eye_icon);

        // 定义默认值

        mTextColor = VMColor.colorByResId(R.color.vm_black_87);
        mTextSize = (int) VMDimen.sp2px(14);
        mHint = "";
        mMode = Mode.TEXT;
        mEnableClear = true;
        mEnableEye = false;
        mClearRes = R.drawable.vm_ic_close;
        mEyeRes = R.drawable.vm_ic_eye_off;

        // 获取控件的属性值
        handleAttrs(context, attrs);

        initEditView();
    }

    /**
     * 获取资源属性
     *
     * @param context
     * @param attrs
     */
    private void handleAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VMEditView);
        // 获取自定义属性值，如果没有设置就是默认值
        mTextColor = array.getColor(R.styleable.VMEditView_vm_edit_text_color, mTextColor);
        mTextSize = array.getDimensionPixelOffset(R.styleable.VMEditView_vm_edit_text_size, mTextSize);

        mHint = array.getString(R.styleable.VMEditView_vm_edit_hint);

        mMode = array.getInt(R.styleable.VMEditView_vm_edit_mode, mMode);

        mEnableClear = array.getBoolean(R.styleable.VMEditView_vm_enable_clear, mEnableClear);
        mEnableEye = array.getBoolean(R.styleable.VMEditView_vm_enable_eye, mEnableEye);
        mClearRes = array.getResourceId(R.styleable.VMEditView_vm_edit_clear_res, mClearRes);
        mEyeRes = array.getResourceId(R.styleable.VMEditView_vm_edit_eye_res, mEyeRes);

        // 回收资源
        array.recycle();
    }

    /**
     * 初始化 EditView 控件
     */
    private void initEditView() {
        mInputView.setTextColor(mTextColor);
        mInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mInputView.setHint(mHint);
        //mHintView.setText(mHint);
        // 设置输入模式
        int inputMode;
        switch (mMode) {
        case Mode.NUMBER:
            inputMode = InputType.TYPE_CLASS_NUMBER;
            break;
        case Mode.PHONE:
            inputMode = InputType.TYPE_CLASS_PHONE;
            break;
        case Mode.EMAIL:
            inputMode = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
            break;
        case Mode.TEXT:
            inputMode = InputType.TYPE_CLASS_TEXT;
            break;
        case Mode.PASSWORD:
            mEyeIcon.setVisibility(VISIBLE);
            mInputView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            inputMode = InputType.TYPE_TEXT_VARIATION_PASSWORD;
            break;
        default:
            inputMode = InputType.TYPE_CLASS_TEXT;
            break;
        }
        mInputView.setInputType(inputMode);
        // 设置图标
        mClearIcon.setImageResource(mClearRes);
        mEyeIcon.setImageResource(mEyeRes);

        mClearIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputView.setText("");
            }
        });
        mEyeIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEyeIcon.isSelected()) {
                    // 隐藏密码
                    mInputView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEyeIcon.setImageResource(R.drawable.vm_ic_eye_off);
                    mEyeIcon.setSelected(false);
                } else {
                    // 显示密码
                    mInputView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEyeIcon.setImageResource(R.drawable.vm_ic_eye_on);
                    mEyeIcon.setSelected(true);
                }
            }
        });

        mInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (VMStr.isEmpty(s)) {
                    mClearIcon.setVisibility(GONE);
                } else {
                    if (mEnableClear) {
                        mClearIcon.setVisibility(VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 输入模式
     */
    public interface Mode {
        int NUMBER = 0;
        int PHONE = 1;
        int EMAIL = 2;
        int TEXT = 3;
        int PASSWORD = 4;
    }
}
