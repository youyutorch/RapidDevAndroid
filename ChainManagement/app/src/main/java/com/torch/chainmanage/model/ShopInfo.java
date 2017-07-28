package com.torch.chainmanage.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/** 
 * desc: 历史巡店信息
 * author: tianyouyu
 * date: 2017/7/28 0028 22:11
*/
public class ShopInfo extends DataSupport implements Serializable {
    private int id;
    private String imgname;//图片名称
    private String imgpath;//图片路径
    private String name;//店面名称
    private String shopid;//店面id
    private String shoplocation;//店面位置
    private String shoplevel;//店面评分
    private String userid;//用户id
    private String visitdate;//巡店时间
    private String feedback;//店面评语

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShoplocation() {
        return shoplocation;
    }

    public void setShoplocation(String shoplocation) {
        this.shoplocation = shoplocation;
    }

    public String getShoplevel() {
        return shoplevel;
    }

    public void setShoplevel(String shoplevel) {
        this.shoplevel = shoplevel;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVisitdate() {
        return visitdate;
    }

    public void setVisitdate(String visitdate) {
        this.visitdate = visitdate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "DateList{" +
                "id=" + id +
                ", imgname='" + imgname + '\'' +
                ", imgpath='" + imgpath + '\'' +
                ", name='" + name + '\'' +
                ", shopid='" + shopid + '\'' +
                ", shoplocation='" + shoplocation + '\'' +
                ", shoplevel='" + shoplevel + '\'' +
                ", userid='" + userid + '\'' +
                ", visitdate='" + visitdate + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
