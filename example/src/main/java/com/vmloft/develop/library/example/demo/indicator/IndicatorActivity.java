package com.vmloft.develop.library.example.demo.indicator;

import android.support.v4.view.ViewPager;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.example.common.AppLazyFragment;
import com.vmloft.develop.library.example.common.SimplePagerAdapter;
import com.vmloft.develop.library.tools.widget.indicator.VMIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Create by lzan13 on 2019/04/11
 *
 * 自定义指示器验证示例
 */
public class IndicatorActivity extends AppActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.indicator_view)
    VMIndicatorView mIndicatorView;

    private List<AppLazyFragment> fragmentList;
    private SimplePagerAdapter mAdapter;

    @Override
    protected int loadView() {
        return R.layout.activity_indicator;
    }

    @Override
    protected void init() {

        fragmentList = new ArrayList<>();
        fragmentList.add(IndicatorFragment.newInstance("第 1 页"));
        fragmentList.add(IndicatorFragment.newInstance("第 2 页"));
        fragmentList.add(IndicatorFragment.newInstance("第 3 页"));
        fragmentList.add(IndicatorFragment.newInstance("第 4 页"));
        fragmentList.add(IndicatorFragment.newInstance("第 5 页"));
        mAdapter = new SimplePagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mIndicatorView.setViewPager(mViewPager);
    }
}