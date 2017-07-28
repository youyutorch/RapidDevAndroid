package com.torch.chainmanage.http.bean;

import com.torch.chainmanage.model.Info;

import java.util.List;

public class InfoResult {

    /**
     * code : 0
     * msg : 资讯获取成功
     * body : [{"title":"公司动态","summary":"资讯摘要","imgurl":"http://xxxxx.jpg","detail":"http:xxxxxx.html"},{"title":"公司动态","summary":"资讯摘要","imgurl":"http://xxxxx.jpg","detail":"http:xxxxxx.html"},{"title":"公司动态","summary":"资讯摘要","imgurl":"http://xxxxx.jpg","detail":"http:xxxxxx.html"},{"title":"公司动态","summary":"资讯摘要","imgurl":"http://xxxxx.jpg","detail":"http:xxxxxx.html"},{"title":"公司动态","summary":"资讯摘要","imgurl":"http://xxxxx.jpg","detail":"http:xxxxxx.html"}]
     */

    private int code;
    private String msg;
    /**
     * title : 公司动态
     * summary : 资讯摘要
     * imgurl : http://xxxxx.jpg
     * detail : http:xxxxxx.html
     */

    private List<Info> body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Info> getBody() {
        return body;
    }

    public void setBody(List<Info> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "InfoResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}
