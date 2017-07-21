package com.torch.chainmanage.model;

import org.litepal.crud.DataSupport;

/**
 * desc: 广告图片实体类
 * author: tianyouyu
 * date: 2017/7/19 0019 22:52
*/

public class NoticeImage extends DataSupport {
    private int id;
    private String imgUrl;

    @Override
    public String toString() {
        return "Body{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
