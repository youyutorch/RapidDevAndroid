package com.torch.chainmanage.constant;

/** 
 * desc: 地址常量
 * author: tianyouyu
 * date: 2017/7/13 0013 15:14 
*/

public class RequestUrl {
    public static final int HttpOk = 7000;//成功
    public static final int HttpFail = 7001;//失败


    public static final String BaseUrl = "http://192.168.17.124:8080";//模拟器根接口
//    public static final String BaseUrl = "http://192.168.43.232:8080";//模拟器根接口
//    public static final String BaseUrl = "http://www.mofada.cn:8080";//远程服务器根接口

    public static final String Login = BaseUrl + "/visitshop/login";//登录get
    public static final String FeedBack = BaseUrl + "/visitshop/feedback";//意见反馈post
    public static final String Announcement = BaseUrl + "/visitshop/announcement";//公告获取get
    public static final String Task = BaseUrl + "/visitshop/task";//任务获取get
    public static final String Info = BaseUrl + "/visitshop/info";//咨询获取get
    public static final String AppUpdate = BaseUrl + "/visitshop/appinfo";//app更新get
    public static final String HistroyShop = BaseUrl + "/visitshop/history";//历史巡店get
    public static final String ShopSelect = BaseUrl + "/visitshop/shop";//店面选择get
    public static final String VisitShopSubmit = BaseUrl + "/visitshop/visitupload";//巡店数据提交post
    public static final String Train = BaseUrl + "/visitshop/historytrain";//培训列表接口get
    public static final String TrainDetail = BaseUrl + "/visitshop/triandetail";//培训详情get
    public static final String TrainSubmit = BaseUrl + "/visitshop/trainupload";//培训数据提交post
    public static final String InterviewSubmit = BaseUrl + "/visitshop/interviewsubmit";//拜访提交post
    public static final String HistoryInterview = BaseUrl + "/visitshop/historyinterview";//历史拜访post
    public static final String UpdateUser = BaseUrl + "/visitshop/updateuser";//更新用户资料
    public static final String UpdateHead = BaseUrl + "/visitshop/uploadhead";//更新用户资料
}
