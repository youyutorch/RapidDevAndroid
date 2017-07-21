package com.torch.chainmanage;

import android.os.Environment;

import com.torch.chainmanage.util.ImageLoader;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2017/7/7 0007.
 */

public class ChainManagementApp extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        //修改图片本地缓存目录
        String cachePath = Environment.getExternalStorageDirectory().getPath() + "/image-cache";
        ImageLoader.setDiskCache(cachePath);
    }
}
