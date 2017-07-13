package com.torch.chainmanage.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.torch.chainmanage.http.bean.Params;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class OkHttpHelper {
    private Handler mMainHandler;
    private OkHttpClient mOkHttpClient;
    private static OkHttpHelper mOkHttpHelper;

    private OkHttpHelper() {
        mMainHandler = new Handler(Looper.getMainLooper());
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized OkHttpHelper getInstance() {
        if (mOkHttpHelper == null) {
            mOkHttpHelper = new OkHttpHelper();
        }
        return mOkHttpHelper;
    }

    public static void doLogin(Callback callback) {
        //创建一个OkHttp实例
        OkHttpClient httpClient = new OkHttpClient();

        //创建一个body，包含params请求
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", "num01")
                .add("password", "123456")
                .build();

        //创建一个request对象
        String loginUrl = "http://192.168.50.131:8080/visitshop/login";
        Request request = new Request.Builder()
                .url(loginUrl)
                .post(requestBody)
                .build();

        //发起请求
        httpClient.newCall(request).enqueue(callback);
    }

    public void doGet(String url, final RequestCallback callback) {
        if (TextUtils.isEmpty(url)) {
            if (callback != null) {
                callback.onFailure(new IOException("请求地址不合法"));
            }
            return;
        }
        doRequest(url, null, callback);
    }

    public void doPost(String url, Params params, final RequestCallback callback) {
        if (TextUtils.isEmpty(url)) {
            if (callback != null) {
                callback.onFailure(new IOException("请求地址不合法"));
            }
            return;
        }
        //创建一个RequestBody对象
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry entry : params.getRequestMap().entrySet()) {
                builder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }
        doRequest(url, builder.build(), callback);
    }

    private void doRequest(String url, RequestBody body, final RequestCallback callback) {
        //创建一个request对象
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (body != null) {
            builder.post(body);
        }
        Request request = builder.build();

        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                postFailure(callback, e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = null;
                    try {
                        result = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        postFailure(callback, e);
                        return;
                    }
                    postSuccess(callback, result);
                } else {
                    postFailure(callback, new IOException("获取response出错"));
                }
            }
        });
    }

    private void postSuccess(final RequestCallback callback, final String result) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    private void postFailure(final RequestCallback callback, final IOException exception) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(exception);
                }
            }
        });
    }

    public interface RequestCallback {
        void onSuccess(String result);
        void onFailure(IOException e);
    }
}
