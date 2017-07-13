package com.torch.chainmanage.http.bean;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * desc: 请求参数实体类
 * author: tianyouyu
 * date: 2017/7/13 0013 15:41
*/

public class Params {
    public static final String KEY_USERID = "userid";
    public static final String KEY_PASSWORD = "password";

    private Map<String, String> requestMap = new HashMap<>();

    public Params() {
    }

    public Params(Map<String, String> map) {
        if (map == null) {
            requestMap = new HashMap<>();
        } else {
            requestMap = map;
        }
    }

    public void putParam(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        requestMap.put(key, value);
    }

    public Map<String, String> getRequestMap() {
        return requestMap;
    }
}
