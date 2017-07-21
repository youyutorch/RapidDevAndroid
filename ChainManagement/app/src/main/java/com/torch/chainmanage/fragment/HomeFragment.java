package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.torch.chainmanage.R;
import com.torch.chainmanage.activity.LoginActivity;
import com.torch.chainmanage.constant.RequestUrl;
import com.torch.chainmanage.http.OkHttpHelper;
import com.torch.chainmanage.model.NoticeImage;
import com.torch.chainmanage.util.GsonUtil;
import com.torch.chainmanage.util.ImageLoader;
import com.torch.chainmanage.util.WebPageHelper;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 首页--默认展示
 */

public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    public static final int MSG_UPDATE_VEEWPAGE = 0;
    private View view;
    private List<ImageView> mNoticeImgs = new ArrayList<>(); //公告图列表
    private List<NoticeImage> mImageUrls; //公告图地址列表
    private List<String> mLinkUrls; //广告链接地址，一般由服务器返回，这里由客户端模拟
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private boolean mIsAutoSlide = true; //是否自主轮播
    private int mCurrPosition = 1;
    private Timer mTimer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VEEWPAGE) {
                mViewPager.setCurrentItem(mCurrPosition);
                updataIndicator(mCurrPosition);
            }
        }
    };

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mNoticeImgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            Log.d(TAG, "ImageViewPager - setPrimaryItem " + position);
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "ImageViewPager - instantiateItem " + position);
            ImageView view = mNoticeImgs.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public Parcelable saveState() {
//            Log.d(TAG, "ImageViewPager - saveState");
            return super.saveState();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
//            Log.d(TAG, "ImageViewPager - restoreState");
            super.restoreState(state, loader);
        }

        @Override
        public void startUpdate(ViewGroup container) {
//            Log.d(TAG, "ImageViewPager - startUpdate");
            super.startUpdate(container);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d(TAG, "ImageViewPager - destroyItem " + position);
            container.removeView(mNoticeImgs.get(position));
        }
    };

    public static HomeFragment newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, name);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView - " + mFragmentName);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            initView(view);
            getNoticeImgs();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart - " + mFragmentName);
        //增加自动播放
        if (mTimer == null) {
            addAutoSlip();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop - " + mFragmentName);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initData() {
        Log.d(TAG, "initData ...");
        mLinkUrls = new ArrayList<>();
        mLinkUrls.add("http://blog.csdn.net/youyu_torch/article/details/74512946");
        mLinkUrls.add("http://blog.csdn.net/youyu_torch/article/details/74781768");
        mLinkUrls.add("http://blog.csdn.net/youyu_torch/article/details/75042823");
        mLinkUrls.add("http://blog.csdn.net/youyu_torch/article/details/75331971");
    }

    private void initView(View view) {
        Log.d(TAG, "initView ...");
        TextView content = (TextView) view.findViewById(R.id.tv);
        content.setText(mFragmentName);
        mIndicatorLayout = (LinearLayout)view.findViewById(R.id.fragment_point_subscript);

        mViewPager = (ViewPager)view.findViewById(R.id.fragment_img_viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.w(TAG, "receive event:" + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIsAutoSlide = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mIsAutoSlide = false;
                        break;

                    case MotionEvent.ACTION_UP:
                        mIsAutoSlide = true;
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获取广告轮播图
     */
    private void getNoticeImgs() {
        //已发起过网络请求加载图片信息，则不需要重新加载
        if(mImageUrls != null && mImageUrls.size() > 0) {
            showNoticeImgs(mImageUrls);
            return;
        }

        //从网络中获取图片地址
        OkHttpHelper.getInstance().doGet(RequestUrl.Announcement, new OkHttpHelper.RequestCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess 获取广告轮播图成功 - " + result);
                List<NoticeImage> images = GsonUtil.parseNoticeJson(result);
                if (images == null || images.isEmpty()) {
                    //从数据库中获取图片
                    images = getImgsFromLocal();
                } else {
                    //保存到数据库中
                    DataSupport.deleteAll(NoticeImage.class);
                    DataSupport.saveAll(images);
                }
                showNoticeImgs(images);
            }

            @Override
            public void onFailure(IOException e) {
                Log.d(TAG, "onSuccess - 获取广告轮播图失败");
                List<NoticeImage> images = getImgsFromLocal();
                showNoticeImgs(images);
            }
        });
    }

    /**
     * 展示轮播图
     * @param imageUrls 图片地址列表
     */
    private void showNoticeImgs(List<NoticeImage> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            Log.d(TAG, "showNoticeImgs - imageUrls为空");
            return;
        }
        mImageUrls = imageUrls; //缓存url地址列表，做tag切换时不再重新请求
        Log.d(TAG, "showNoticeImgs - " + imageUrls.toString());
        mNoticeImgs.clear();
        //根据imageUrls动态创建views
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < imageUrls.size(); i++) {
            NoticeImage image = imageUrls.get(i);
            final int index = i % mLinkUrls.size();
            if (!TextUtils.isEmpty(image.getImgUrl())) {
                String absoluteUrl = RequestUrl.BaseUrl + image.getImgUrl();
                urls.add(absoluteUrl);
                mNoticeImgs.add(buildImage(mLinkUrls.get(index)));
            }
        }
        //增加循环滑动效果
        addCycleSlip(urls);

        //增加自动播放
        if (mTimer == null) {
            addAutoSlip();
        }
        ImageLoader.loadFromUrls(mActivity, urls, mNoticeImgs);
        //更新界面显示
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mCurrPosition);
        //更新指示点
        updataIndicator(mCurrPosition);
    }

    private void addAutoSlip() {
        if (mNoticeImgs.size() < 2) {
            return;
        }
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer("AutoSlipTimer");
        mTimer.schedule(new AutoSlipTask(), 3000, 3000);
    }

    private ImageView buildImage(final String linkUrl) {
        ImageView view = new ImageView(mActivity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //点击跳转到广告链接地址
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebPageHelper.loadUrl(mActivity, linkUrl);
            }
        });
        return view;
    }

    /**
     * 增加循环滑动效果
     * @param requestUrls 请求的图片地址列表
     */
    private void addCycleSlip(List<String> requestUrls) {
        if (requestUrls.size() < 2) {
            return;
        }
        int len = requestUrls.size();
        String firstUrl = requestUrls.get(len - 1);
        String lastUrl = requestUrls.get(0);
        //在第一页前加最后一张图
        mNoticeImgs.add(0, buildImage(lastUrl));
        requestUrls.add(0, lastUrl);
        //在最后一页加第一张图
        mNoticeImgs.add(buildImage(firstUrl));
        requestUrls.add(firstUrl);
    }

    private void updataIndicator(int position) {
        //清除所有指示下标
        mIndicatorLayout.removeAllViews();
        //需要去掉重复的第一页和最后一页
        for (int i = 1; i < mNoticeImgs.size() - 1; i++) {
            ImageView img = new ImageView(mActivity);
            //添加下标圆点参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            img.setLayoutParams(params);
            img.setImageResource(R.drawable.sns_v2_page_point);
            if (i == position) {
                img.setSelected(true);
            }
            mIndicatorLayout.addView(img);
        }
    }

    /**
     * 从本地数据库获取图片地址列表
     * @return
     */
    private List<NoticeImage> getImgsFromLocal() {
        Log.d(TAG, "getImgsFromLocal - 从本地数据库获取图片地址列表");
        return DataSupport.findAll(NoticeImage.class);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.i(TAG, "onPageScrolled - " + position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected - " + position);
        int size = mNoticeImgs.size();
        //右滑到最后一页
        if (position == size - 1) {
            mViewPager.setCurrentItem(1, false);
            return;
        }
        //左滑到第一页
        if (position == 0) {
            mViewPager.setCurrentItem(size - 2);
            return;
        }

        mCurrPosition = position;
        for (int i = 0; i < mIndicatorLayout.getChildCount(); i++) {
            ImageView image = (ImageView) mIndicatorLayout.getChildAt(i);
            if (i == position - 1) {
                image.setSelected(true);
            } else {
                image.setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i(TAG, "onPageScrollStateChanged - state=" + state);
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            mIsAutoSlide = false;
        } else {
            mIsAutoSlide = true;
        }
    }

    class AutoSlipTask extends TimerTask {

        @Override
        public void run() {
            Log.i(TAG, "AutoSlipTask start, curr page=" + mCurrPosition);
            if (mIsAutoSlide) {
                int temp = mNoticeImgs.size() - 2;
                if (mCurrPosition < 1) {
                    mCurrPosition = 1;
                }
                if (mCurrPosition > temp) {
                    mCurrPosition = temp;
                }
                mCurrPosition = (mCurrPosition + 1) % temp;
                Log.i(TAG, "slip page to " + mCurrPosition);
                mHandler.sendEmptyMessage(MSG_UPDATE_VEEWPAGE);
            }
        }
    }
}
