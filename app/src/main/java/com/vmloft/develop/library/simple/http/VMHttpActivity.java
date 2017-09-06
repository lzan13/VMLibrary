package com.vmloft.develop.library.simple.http;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.VMCallback;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

/**
 * Created by lzan13 on 2017/6/13.
 * 测试网络请求界面
 */
public class VMHttpActivity extends VMActivity {

    @BindView(R.id.view_group) VMViewGroup viewGroup;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        ButterKnife.bind(this);

        init();
    }

    private void init() {

        String[] btnArray = {
                "Test Get", "Fetch Follow Subscription"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(new ContextThemeWrapper(activity, R.style.VMBtn_Red), null, 0);
            btn.setText(btnArray[i]);
            btn.setId(100 + i);
            btn.setOnClickListener(viewListener);
            viewGroup.addView(btn);
        }
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            switch (view.getId()) {
                case 100:
                    VMHttpManager.getInstance().testGet(new VMCallback() {
                        @Override public void onSuccess(String message) {
                            VMLog.i(message);
                        }

                        @Override public void onError(int code, String message) {
                            VMLog.i("onError %d %s", code, message);
                        }

                        @Override public void onPregress(int progress, String message) {

                        }
                    });
                    break;
                case 101:
                    new Thread(new Runnable() {
                        @Override public void run() {
                            EMSubscriptionManager.getInstance().fetchFollowSubscriptionAccountsFromServer(1, 20);
                        }
                    }).start();
                    break;
            }
        }
    };
}
