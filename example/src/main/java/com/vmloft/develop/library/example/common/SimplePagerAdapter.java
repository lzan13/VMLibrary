package com.vmloft.develop.library.example.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Create by lzan13 on 2019/04/09
 *
 * 简单通用 Fragment + ViewPager 适配器
 */
public class SimplePagerAdapter extends FragmentPagerAdapter {

    private List<AppLazyFragment> fragments;

    public SimplePagerAdapter(FragmentManager fm, List<AppLazyFragment> list) {
        super(fm);
        fragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
