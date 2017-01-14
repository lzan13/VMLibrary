package com.vmloft.develop.library.manager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import com.vmloft.develop.library.tools.VMBaseActivity;

/**
 * Created by lzan13 on 2017/1/12.
 */

public class VMTransitionTwoActivity extends VMBaseActivity {

    private Activity mActivity;

    private Toolbar mToolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitions_two);

        mActivity = this;

        setupWindowTransitions();

        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mToolbar.setTitle("VMTransitionTwoActivity");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
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

    @Override public void onBackPressed() {
        //super.onBackPressed();
        onFinish();
    }
}
