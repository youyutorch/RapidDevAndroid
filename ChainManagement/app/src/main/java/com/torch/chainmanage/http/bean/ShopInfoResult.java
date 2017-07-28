package com.torch.chainmanage.http.bean;

import com.torch.chainmanage.model.ShopInfo;

import java.util.List;

public class ShopInfoResult {
    private int code;//请求返回码，0为成功
    private String msg;//请求放回信息
    private int page;//返回数据的页数
    private List<ShopInfo> datelist;//请求数据数组

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<ShopInfo> getDatelist() {
        return datelist;
    }

    public void setDatalist(List<ShopInfo> datalist) {
        this.datelist = datalist;
    }

    @Override
    public String toString() {
        return "HistoryShopResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", page=" + page +
                ", datalist=" + datelist +
                '}';
    }
}
