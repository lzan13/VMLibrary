package com.vmloft.develop.library.tools.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Create by lzan13 on 2019/04/09
 *
 * 简单通用 Fragment + ViewPager 适配器
 */
public class VMFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public VMFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
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
