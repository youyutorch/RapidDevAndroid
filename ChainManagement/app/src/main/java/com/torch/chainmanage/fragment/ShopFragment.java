package com.torch.chainmanage.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.torch.chainmanage.R;
import com.torch.chainmanage.adapter.VisitShopListAdapter;
import com.torch.chainmanage.constant.RequestUrl;
import com.torch.chainmanage.http.OkHttpHelper;
import com.torch.chainmanage.http.bean.ShopInfoResult;
import com.torch.chainmanage.model.ShopInfo;
import com.torch.chainmanage.util.SharePreUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 巡店
 */

public class ShopFragment extends BaseFragment implements XRecyclerView.LoadingListener,
        View.OnClickListener, TextView.OnEditorActionListener {
    private View view;
    private XRecyclerView recyclerView;
    private RelativeLayout progress, rl_http_failed;
    public static List<ShopInfo> info_list;
    private VisitShopListAdapter adapter;
    // private TextView none;
    private EditText search;
    private String shop_name;
    private int pagenum = 1;//当前页数

    private Boolean IsSearch;//是否进入搜索模式
    private String userid;//用户id
    private Boolean isLoad;//是否登录
    public static boolean isFirst;//是否是首次登录

    public static ShopFragment newInstance(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NAME, name);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        initView();//初始化视图
        return view;
    }

    /**
     * 初始化控件信息
     */
    private void initView() {
        IsSearch = false;
        recyclerView = (XRecyclerView) view.findViewById(R.id.activity_visitshop_list);
        recyclerView.setLoadingListener(this);
        rl_http_failed = (RelativeLayout) view.findViewById(R.id.activity_visitshop_refresh);
        progress = (RelativeLayout) view.findViewById(R.id.activity_visitshop_progress);
        rl_http_failed.setOnClickListener(this);
        search = (EditText) view.findViewById(R.id.et_search_shop);
        search.setOnEditorActionListener(this);
        if (info_list == null) {
            info_list = new ArrayList<ShopInfo>();
        }
        adapter = new VisitShopListAdapter(mActivity, info_list);
        //设置加载风格
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //设置线性列表展示
        recyclerView.setLayoutManager(
                new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        //设置空布局
        View emptyView = view.findViewById(R.id.activity_visitshop_none);
        emptyView.setOnClickListener(this);
        recyclerView.setEmptyView(emptyView);
    }

    @Override
    public void onResume() {
        super.onResume();
        userid = SharePreUtil.GetShareString(mActivity, "userId");
        if ("".equals(userid)) {
            isLoad = false;
            Toast.makeText(mActivity, R.string.please_login, Toast.LENGTH_SHORT).show();
        } else {
            isLoad = true;
            if (isFirst) {
                isFirst = false;
                pagenum = 1;
                initData();//加载数据
                progress.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 加载数据
     */
    private void initData() {
        Log.i(TAG, "pagenum-----" + pagenum);
        //店面查询请求
        String urlString = "";
        if (IsSearch) {
            urlString = RequestUrl.HistroyShop + "?userid=" + userid + "&pagenum=" + pagenum + "&shopName=" + shop_name;
        } else {
            urlString = RequestUrl.HistroyShop + "?userid=" + userid + "&pagenum=" + pagenum;
        }
        Log.d(TAG, "request url " + urlString);
        OkHttpHelper.getInstance().doGet(urlString, new OkHttpHelper.RequestCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "request history success - " + result);
                getShopSuccess(result);
            }

            @Override
            public void onFailure(IOException e) {
                Log.w(TAG, "request history failed!!");
                e.printStackTrace();
                getShopFailed();
            }
        });
    }

    /**
     * 获取历史巡店信息失败
     */
    private void getShopFailed() {
        onLoad();
        progress.setVisibility(View.GONE);
        Toast.makeText(mActivity, R.string.http_failed, Toast.LENGTH_LONG).show();
        //获取本地数据库信息
        if (info_list == null || info_list.size() == 0) {
            info_list = DataSupport.findAll(ShopInfo.class);
            if (info_list == null) {
                info_list = new ArrayList<ShopInfo>();
            }
            if (info_list.size() > 0) {
                //显示联网失败刷新界面
                rl_http_failed.setVisibility(View.GONE);
                adapter.setList(info_list);
                adapter.notifyDataSetChanged();
            } else {
                rl_http_failed.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取历史巡店信息成功
     */
    private void getShopSuccess(String resultStr) {
        onLoad();
        rl_http_failed.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        if (resultStr != null && !"".equals(resultStr)) {
            Gson gson = new Gson();
            ShopInfoResult infos = gson.fromJson(resultStr, ShopInfoResult.class);
            if (infos != null) {
                if (infos.getCode() == 0) {
                    if (infos.getDatelist() != null) {
                        if (info_list == null) {
                            info_list = new ArrayList<ShopInfo>();
                        }
                        //如果是第一页数据，则把之前数据清空
                        if (pagenum == 1) {
                            info_list.clear();
                        }
                        if (infos.getDatelist().size() == 0) {
                            Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT).show();
                        } else {
                            info_list.addAll(infos.getDatelist());
                        }
                        //当前页数递增
                        pagenum++;
                        adapter.setList(info_list);
                        adapter.notifyDataSetChanged();
                        //从数据库清除数据保存
                        DataSupport.deleteAll(ShopInfo.class);
                        //添加新数据到数据库
                        DataSupport.saveAll(infos.getDatelist());
                    }
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        pagenum = 1;
        initData();
    }

    @Override
    public void onLoadMore() {
        //加载更多
        initData();
    }

    /**
     * 结束上下拉刷新
     */
    private void onLoad() {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_visitshop_refresh://加载失败后点击刷新
                rl_http_failed.setVisibility(View.GONE);
                initData();
                break;
            case R.id.activity_visitshop_none://没有数据点击重新请求
                initData();
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        /**
         * 当点击搜索按钮时
         */
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard();
            shop_name = search.getText().toString().trim();
            progress.setVisibility(View.VISIBLE);
            pagenum = 1;
            //店面查询请求
            String urlString = RequestUrl.HistroyShop + "?userid=" + userid + "&pagenum=" + pagenum + "&shopName=" + shop_name;
            OkHttpHelper.getInstance().doGet(urlString, new OkHttpHelper.RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    getShopSuccess(result);
                }

                @Override
                public void onFailure(IOException e) {
                    getShopFailed();
                }
            });
            IsSearch = true;
        }
        return false;
    }

    public void hideKeyboard() {//隐藏软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(mActivity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 清空静态变量
         */
        info_list = null;
        isFirst = false;
    }
}
