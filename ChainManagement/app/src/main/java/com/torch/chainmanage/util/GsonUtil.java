package com.torch.chainmanage.util;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.torch.chainmanage.http.bean.LoginResult;
import com.torch.chainmanage.http.bean.NoticeResult;
import com.torch.chainmanage.model.NoticeImage;
import com.torch.chainmanage.model.User;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 用于json字符串的解析
 * author: tianyouyu
 * date: 2017/7/13 0013 16:13
*/

public class GsonUtil {
    private static Gson sGson = new Gson();
    private static String sReason;

    public static User parseLoginJson(String json) {
        sReason = null;
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        LoginResult loginResult = null;
        try {
            loginResult = sGson.fromJson(json, LoginResult.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

        //利用loginResult对象，构造user对象
        if (loginResult != null) {
            if (loginResult.getCode() == 0) {
                //成功获取到用户信息
                User user = new User();
                LoginResult.BodyBean bodyBean = loginResult.getBody();
                user.setUserId(bodyBean.getUserid());
                user.setArea(bodyBean.getArea());
                user.setImg(bodyBean.getImg());
                user.setNickName(bodyBean.getNickname());
                user.setSex(bodyBean.getSex());
                user.setPhoneNum(bodyBean.getPhonenum());
                user.setJob(bodyBean.getJob());
                return user;
            } else {
                sReason = loginResult.getMsg() + loginResult.getCode();
            }
        }

        return null;
    }

    public static List<NoticeImage> parseNoticeJson(String json) {
        sReason = null;
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        NoticeResult noticeResult = null;
        try {
            noticeResult = sGson.fromJson(json, NoticeResult.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

        //利用noticeResult对象，构造NoticeImage对象
        if (noticeResult != null) {
            if (noticeResult.getCode() == 0) {
                //成功获取到图片地址信息
                List<NoticeImage> images = new ArrayList<>();
                List<NoticeResult.BodyBean> bodyBeans = noticeResult.getBody();
                if (bodyBeans != null) {
                    for (NoticeResult.BodyBean bodyBean : bodyBeans) {
                        NoticeImage image = new NoticeImage();
                        image.setId(bodyBean.getId());
                        image.setImgUrl(bodyBean.getImgUrl());
                        images.add(0, image);
                    }
                }
                return images;
            } else {
                sReason = noticeResult.getMsg() + noticeResult.getCode();
            }
        }

        return null;
    }

    public static String getReason() {
        return sReason;
    }
}
