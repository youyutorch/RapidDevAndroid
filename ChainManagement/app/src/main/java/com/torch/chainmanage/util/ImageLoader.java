package com.torch.chainmanage.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.torch.chainmanage.http.OkHttp3Downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * desc: 获取图片工具类
 * author: tianyouyu
 * date: 2017/7/19 0019 23:53
*/
public class ImageLoader {
    public static final String TAG = "ImageLoader";
    private static File mCacheFile;

    /**
     * 设置本地缓存目录
     * @param cachePath
     */
    public static void setDiskCache(String cachePath) {
        if (TextUtils.isEmpty(cachePath)) {
            mCacheFile = null;
            return;
        }

        Log.d(TAG, "setDiskCache - " + cachePath);
        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        mCacheFile = file;
    }

    /**
     * 根据地址获取图片
     * @param context
     * @param urls 图片访问地址
     * @param imgViews 显示图片的views
     * @return
     */
    public static void loadFromUrls(Context context, List<String> urls, List<ImageView> imgViews) {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        int count = urls.size(); //图片数量
        if (imgViews == null || imgViews.isEmpty()) {
            return;
        }
        if (imgViews.size() < count) {
            count = imgViews.size();
        }

        for (int i = 0; i < count; i++) {
            Log.d(TAG, "Picasso load url:" + urls.get(i));
            Picasso picasso = new Picasso.Builder(context).build();
            if (mCacheFile != null && mCacheFile.exists()) {
                picasso = new Picasso.Builder(context)
                        .downloader(new OkHttp3Downloader(mCacheFile))
                        .build();
            }
            picasso.load(urls.get(i))
                    .into(imgViews.get(i));
        }
    }

    public static void loadFromUrl(Context context, String url, ImageView imgView) {
        List<String> urls = new ArrayList<>();
        urls.add(url);
        List<ImageView> views = new ArrayList<>();
        views.add(imgView);
        loadFromUrls(context, urls, views);
    }
}
