package com.vmloft.develop.library.example.tools;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMCryptoUtil;
import com.vmloft.develop.library.tools.utils.VMLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lzan13 on 2017/12/22.
 * 签名工具
 */
public class SignatureActivity extends VMActivity {
    @BindView(R.id.edit_str) EditText inputStrEdit;
    private String inputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(activity);
    }

    @OnClick({R.id.btn_key_hashes, R.id.btn_md5_signature, R.id.btn_base64})
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
        }
    }

    private void getInputStr() {
        inputStr = inputStrEdit.getText().toString().trim();

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
        String result = VMCryptoUtil.cryptoStr2MD5(inputStr);
        VMLog.d("Encryption result: %s", result);
    }

    public void base64Encryption() {
        getInputStr();
        String result = new String(Base64.encodeToString(inputStr.getBytes(), Base64.DEFAULT));
        VMLog.d("Encryption result: %s", result);
    }
}
