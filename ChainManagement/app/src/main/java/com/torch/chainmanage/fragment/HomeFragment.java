package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.torch.chainmanage.R;
import com.torch.chainmanage.adapter.InfoListBaseAdapter;
import com.torch.chainmanage.adapter.TaskListBaseAdapter;
import com.torch.chainmanage.constant.RequestUrl;
import com.torch.chainmanage.http.OkHttpHelper;
import com.torch.chainmanage.model.Info;
import com.torch.chainmanage.model.NoticeImage;
import com.torch.chainmanage.model.Task;
import com.torch.chainmanage.util.GsonUtil;
import com.torch.chainmanage.util.ImageLoader;
import com.torch.chainmanage.util.SharePreUtil;
import com.torch.chainmanage.util.WebPageHelper;
import com.torch.chainmanage.view.AutoLinearLayoutManager;
import com.torch.chainmanage.view.PartScrollView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 首页--默认展示
 */

public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final int MSG_UPDATE_VEEWPAGE = 0;
    private View mFragmentView; //当前fragment
    private RelativeLayout mLoadingProgress; //加载框
    private LinearLayout mFailedTip; //网络访问失败提示
    private PartScrollView mPartScrollView;
    
    /** 公告图相关 **/
    private List<ImageView> mNoticeImgs = new ArrayList<>(); //公告图列表
    private List<NoticeImage> mImageUrls; //公告图地址列表
    private List<String> mLinkUrls; //广告链接地址，一般由服务器返回，这里由客户端模拟
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private boolean mIsAutoSlide = true; //是否自主轮播
    private int mCurrPosition = 1;
    private Timer mTimer;

    /** 巡店、拜访、培训 **/
    private TextView mNewShopView;
    private TextView mNewVisitView;
    private TextView mTrainView;

    /** 任务类 **/
    private RelativeLayout mTaskLayout;
    private RecyclerView mTaskRecyclerView;
    private TextView mTaskDetailView; //更多
    private List<Task> mTaskList; //任务列表
    private TaskListBaseAdapter mTaskAdapter;

    /** 资讯类 **/
    private List<Info> mInfoList; //资讯列表
    private TextView mInfoDetailView;
    private RecyclerView mInfoRecyclerView;
    private InfoListBaseAdapter mInfoAdapter;
    private String mUserId; //用户id
    
    private int mShowSize = 3;

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
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
            initView(mFragmentView);
            initScreenListener();
            mLoadingProgress.setVisibility(View.VISIBLE); //显示加载框
            requestNoticeImgs(); //请求公告图
            requestTask(); //请求任务
            requestInfo(); //请求资讯
        }
        return mFragmentView;
    }

    private void initScreenListener() {
        mPartScrollView.setScreenListener(new PartScrollView.ScreenListener() {
            @Override
            public int getMaxScreenY() {
                int height = mTaskRecyclerView.getHeight();
                int[] location = new int[]{0, 0};
                mTaskRecyclerView.getLocationOnScreen(location);
                Log.d(TAG, "getMaxScreenY - location[1]=" + location[1] + ",height=" + height);
                return location[1] + height;
            }
        });
    }

    private void requestInfo() {
        Log.i(TAG, "requestInfo - 请求获取资讯");
        String url = RequestUrl.Info + "?pagenum=1&type=0"; //获取公司第一页动态
        OkHttpHelper.getInstance().doGet(url, new OkHttpHelper.RequestCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "requestInfo onSuccess 成功获取资讯 - " + result);

                mInfoList = GsonUtil.parseInfoJson(result);
                if (mInfoList == null) {
                    //从数据库中读取
                    mInfoList = DataSupport.findAll(Info.class);
                } else {
                    //更新到数据库
                    DataSupport.deleteAll(Info.class);
                    DataSupport.saveAll(mInfoList);
                }
                showInfoList(mInfoList);
            }

            @Override
            public void onFailure(IOException e) {
                Log.w(TAG, "requestInfo onFailure 获取资讯失败");
                e.printStackTrace();

                //从数据库中读取
                mInfoList = DataSupport.findAll(Info.class);
                showInfoList(mInfoList);
            }
        });
    }

    /**
     * 显示资讯列表
     * @param infoList
     */
    private void showInfoList(List<Info> infoList) {
        //关闭加载框
        mLoadingProgress.setVisibility(View.GONE);

        mInfoAdapter = new InfoListBaseAdapter(mActivity, infoList, mShowSize);
        mInfoRecyclerView.setAdapter(mInfoAdapter);
        mInfoAdapter.notifyDataSetChanged();
    }

    private void requestTask() {
        Log.i(TAG, "requestTask - 请求获取任务");
        //判断用户是否已经登录
        if (TextUtils.isEmpty(mUserId)) {
            Toast.makeText(mActivity, "您尚未登录", Toast.LENGTH_SHORT);
            return;
        }
        //请求获取任务
        String url = RequestUrl.Task + "?pagenum=1"; //获取第一页任务
        OkHttpHelper.getInstance().doGet(url, new OkHttpHelper.RequestCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "requestTask onSuccess 成功获取任务 - " + result);
                mTaskList = GsonUtil.parseTaskJson(result);
                if (mTaskList == null) {
                    //从数据库中读取
                    mTaskList = DataSupport.findAll(Task.class);
                } else {
                    //更新到数据库中
                    DataSupport.deleteAll(Task.class);
                    DataSupport.saveAll(mTaskList);
                }
                showTaskList(mTaskList);
            }

            @Override
            public void onFailure(IOException e) {
                Log.w(TAG, "requestTask onFailure 获取任务失败");
                e.printStackTrace();
                //从数据库中读取
                mTaskList = DataSupport.findAll(Task.class);
                //设置网络访问失败提示view可见
                mFailedTip.setVisibility(View.VISIBLE);
                showTaskList(mTaskList);
            }
        });
    }

    /**
     * 展示任务列表
     * @param taskList
     */
    private void showTaskList(List<Task> taskList) {
        mTaskLayout.setVisibility(View.VISIBLE);
        mTaskAdapter = new TaskListBaseAdapter(mActivity, taskList, mShowSize);
        mTaskRecyclerView.setAdapter(mTaskAdapter);
        mTaskAdapter.notifyDataSetChanged(); //更新页面
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart - " + mFragmentName);
        int[] outLocation = new int[]{0, 0};
        mTaskRecyclerView.getLocationOnScreen(outLocation);
        Log.w(TAG, "onStart - mTaskRecyclerView location=" + outLocation[0] + "," + outLocation[1]);
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

        mUserId = SharePreUtil.GetShareString(mActivity, "userId");
    }

    private void initView(View view) {
        Log.d(TAG, "initView ...");
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

        mFailedTip = (LinearLayout) view.findViewById(R.id.rl_http_failed);
        mFailedTip.setOnClickListener(this);
        mLoadingProgress = (RelativeLayout) view.findViewById(R.id.message_fregment_progress);
        mPartScrollView = (PartScrollView) view.findViewById(R.id.scroll_view);

        mInfoDetailView = (TextView) view.findViewById(R.id.fragment_home_task_more);
        mInfoDetailView.setOnClickListener(this);
        mTaskDetailView = (TextView) view.findViewById(R.id.fragment_home_info_more);
        mTaskDetailView.setOnClickListener(this);
        mTaskLayout = (RelativeLayout) view.findViewById(R.id.home_fragment_task);
        mNewShopView = (TextView) view.findViewById(R.id.fragment_sort_shop);
        mNewShopView.setOnClickListener(this);
        mNewVisitView = (TextView) view.findViewById(R.id.fragment_sort_visit);
        mNewVisitView.setOnClickListener(this);
        mTrainView = (TextView) view.findViewById(R.id.fragment_sort_train);
        mTrainView.setOnClickListener(this);
        mTaskRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_task_list);
        mInfoRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_info_list);

        LinearLayoutManager taskLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mTaskRecyclerView.setLayoutManager(taskLayoutManager);
        LinearLayoutManager infoLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mInfoRecyclerView.setLayoutManager(infoLayoutManager);

        //测试能否接收的MOVE事件
        mInfoRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.w(TAG, "mInfoRecyclerView onTouch - 接收到事件" + event.getAction());
                return false;
            }
        });

        mTaskRecyclerView.setNestedScrollingEnabled(false);
//        mInfoRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 获取广告轮播图
     */
    private void requestNoticeImgs() {
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
    public void onClick(View v) {

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
