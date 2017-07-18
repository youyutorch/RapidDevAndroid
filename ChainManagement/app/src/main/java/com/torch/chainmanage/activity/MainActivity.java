package com.torch.chainmanage.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.torch.chainmanage.R;
import com.torch.chainmanage.fragment.BaseFragment;
import com.torch.chainmanage.fragment.HomeFragment;
import com.torch.chainmanage.fragment.MeFragment;
import com.torch.chainmanage.fragment.ShopFragment;
import com.torch.chainmanage.fragment.TrainFragment;
import com.torch.chainmanage.fragment.VisitFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity {
    private static final String TAG = "MainActivity";
    private final String[] mTitles = new String[]{"首页", "巡店", "拜访", "培训", "个人中心"}; //标题列表
    /** 底部图标资源-未选中状态 **/
    private final int[] mIconNormalRes = new int[]{R.drawable.menu_home_normal, R.drawable.menu_shop_normal,
        R.drawable.menu_visit_normal, R.drawable.menu_train_normal, R.drawable.menu_me_normal};
    /** 底部图标资源-选中状态 **/
    private final int[] mIconPressedRes = new int[]{R.drawable.menu_home_press, R.drawable.menu_shop_pressed,
            R.drawable.menu_visit_pressed, R.drawable.menu_train_pressed, R.drawable.menu_me_pressed};

    private List<BaseFragment> mFragments = new ArrayList<>(); //页面fragment列表
    private List<BottomNavigationItem> mNavigationItems = new ArrayList<>(); //底部item列表
    private BottomNavigationBar mBottomNavigationBar;
    private ViewPager mViewPager;
    private ImageView mTitleRefreshImage; //刷新图标
    private ImageView mTitleAddImage; //添加图标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initListener();
    }

    /**
     * 初始化fragments及bottombar相关数据
     */
    private void initData() {
        BaseFragment home = HomeFragment.newInstance("首页界面");
        mFragments.add(home);
        BaseFragment shop = ShopFragment.newInstance("巡店界面");
        mFragments.add(shop);
        BaseFragment visit = VisitFragment.newInstance("拜访界面");
        mFragments.add(visit);
        BaseFragment train = TrainFragment.newInstance("培训界面");
        mFragments.add(train);
        BaseFragment me = MeFragment.newInstance("个人中心");
        mFragments.add(me);

        for (int i = 0; i < mTitles.length; i++) {
            BottomNavigationItem item = new BottomNavigationItem(mIconPressedRes[i], mTitles[i]);
            Drawable drawable = getResources().getDrawable(mIconNormalRes[i]);
            item.setInactiveIcon(drawable); //定制item未选中图片
            mNavigationItems.add(item);
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        TextView title_bar_back = (TextView) findViewById(R.id.title_bar_back);
        title_bar_back.setVisibility(View.GONE);
        mTitleAddImage = (ImageView)findViewById(R.id.title_bar_more);
        mTitleRefreshImage = (ImageView)findViewById(R.id.title_bar_change);

        mViewPager = (ViewPager)findViewById(R.id.view_pager_content);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setCurrentItem(0);

        mBottomNavigationBar = (BottomNavigationBar)findViewById(R.id.footer_bottom_bar);
        for (BottomNavigationItem item : mNavigationItems) {
            mBottomNavigationBar.addItem(item);
        }
        mBottomNavigationBar.initialise();
        mBottomNavigationBar.setFirstSelectedPosition(0);
        setTitleName(mTitles[0]);
    }

    /**
     * 初始化页面滑动监听器及底部tag切换监听器
     */
    private void initListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected - " + position);
                mBottomNavigationBar.selectTab(position);
                refreshHeaderView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                //未选中->选中
                mViewPager.setCurrentItem(position);
                refreshHeaderView(position);
            }
        });
    }

    /**
     * 更新当前页面的header view
     * @param position
     */
    private void refreshHeaderView(int position) {
        setTitleName(mTitles[position]);
        switch (position) {
            case 1:
                mTitleAddImage.setVisibility(View.VISIBLE);
                mTitleRefreshImage.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTitleAddImage.setVisibility(View.VISIBLE);
                mTitleRefreshImage.setVisibility(View.INVISIBLE);
                break;
            default:
                mTitleAddImage.setVisibility(View.INVISIBLE);
                mTitleRefreshImage.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    };

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    };
}
