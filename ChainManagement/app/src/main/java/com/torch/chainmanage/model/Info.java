package com.torch.chainmanage.model;

import org.litepal.crud.DataSupport;

public class Info extends DataSupport {
    private int id;
    private String title;
    private String summary;
    private String imgurl;
    private String detail;

    public Info() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "InfoResultBody{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
