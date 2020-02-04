package com.vmloft.develop.library.example.tools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;
import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.utils.VMCrypto;
import com.vmloft.develop.library.tools.utils.VMLog;

import com.vmloft.develop.library.tools.utils.VMStr;
import com.vmloft.develop.library.tools.utils.VMUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lzan13 on 2017/12/22.
 * 签名工具
 */
public class ToolsActivity extends AppActivity {
    @BindView(R.id.edit_str) EditText inputStrEdit;
    @BindView(R.id.tools_result_tv) TextView mResultTV;
    private String inputStr;

    @Override
    protected int layoutId() {
        return R.layout.activity_tools;
    }

    @Override
    protected void init() {
        setTopIcon(R.drawable.ic_back);
        setTopTitle("工具类");
        getTopBar().setCenter(true);
        getTopBar().setEndBtn("测试");
    }

    @OnClick({ R.id.btn_key_hashes, R.id.btn_md5_signature, R.id.btn_base64, R.id.tools_random_btn })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_key_hashes:
            getKeyHashes();
            break;
        case R.id.btn_md5_signature:
            md5Encryption();
            break;
        case R.id.btn_base64:
            base64Encryption();
            break;
        case R.id.tools_random_btn:
            random();
            break;
        }
    }

    private void getInputStr() {
        inputStr = inputStrEdit.getText().toString().trim();
    }

    /**
     * 获取随机数
     */
    private void random() {
        int random = VMUtils.random(1, 6);
        mResultTV.setText(VMStr.byArgs("获取的随机数: %d", random));
    }

    /**
     * 获取应用的 Key Hashes
     */
    public void getKeyHashes() {
        getInputStr();
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(inputStr, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyResult = new String(Base64.encode(md.digest(), 0));
                // String something = new String(Base64.encodeBytes(md.digest()));
                VMLog.d("Get Key Hashes %s", KeyResult);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void md5Encryption() {
        getInputStr();
        String result = VMCrypto.cryptoStr2MD5(inputStr);
        VMLog.d("Encryption result: %s", result);
    }

    public void base64Encryption() {
        getInputStr();
        String result = new String(Base64.encodeToString(inputStr.getBytes(), Base64.DEFAULT));
        VMLog.d("Encryption result: %s", result);
    }
}
