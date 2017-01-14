package com.vmloft.develop.library.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import com.vmloft.develop.library.tools.VMBaseActivity;

/**
 * Created by lzan13 on 2017/1/12.
 */
public class VMTransitionOneActivity extends VMBaseActivity {

    private Activity mActivity;

    private Toolbar mToolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitions_one);

        mActivity = this;

        setupWindowTransitions();

        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mToolbar.setTitle("VMTransitionOneActivity");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(mActivity, VMTransitionTwoActivity.class);
                View sharedElement = findViewById(R.id.img_shared_element);
                onStartActivity(mActivity, intent, sharedElement);
            }
        });
    }

    /**
     * 设置界面过度效果
     */
    protected void setupWindowTransitions() {
        Transition enterTransition = TransitionInflater.from(mActivity)
                .inflateTransition(R.transition.transition_slide_enter);
        Transition exitTransition = TransitionInflater.from(mActivity)
                .inflateTransition(R.transition.transition_slide_exit);
        Transition explode = TransitionInflater.from(mActivity)
                .inflateTransition(R.transition.transition_explode);

        getWindow().setEnterTransition(explode);
        getWindow().setExitTransition(explode);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override public void onBackPressed() {
        //super.onBackPressed();
        onFinish();
    }
}
