package com.torch.chainmanage.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * desc: 网页加载帮助类，先使用第三方浏览器打开网页
 * author: tianyouyu
 * date: 2017/7/20 0020 23:29
*/

public class WebPageHelper {
    public static void loadUrl(Context context, String url) {
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}
