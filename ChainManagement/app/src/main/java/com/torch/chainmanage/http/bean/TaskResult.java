package com.torch.chainmanage.http.bean;

import com.torch.chainmanage.model.Task;

import java.util.List;

public class TaskResult {

    /**
     * code : 0
     * msg : 任务获取成功
     * body : [{"title":"任务1","detail":"任务描述：针对公司新产品XXX的市场调研。需要来店里咨询的客户填写问卷，并留联系方式。问卷已发送至各位邮箱，请下载并打印。","publishdate":"2016-06-01","executedate":"2016-06-01 至 2016-09-01","state":1},{"title":"任务1","detail":"任务描述：针对公司新产品XXX的市场调研。需要来店里咨询的客户填写问卷，并留联系方式。问卷已发送至各位邮箱，请下载并打印。","publishdate":"2016-06-01","executedate":"2016-06-01 至 2016-09-01","state":1},{"title":"任务1","detail":"任务描述：针对公司新产品XXX的市场调研。需要来店里咨询的客户填写问卷，并留联系方式。问卷已发送至各位邮箱，请下载并打印。","publishdate":"2016-06-01","executedate":"2016-06-01 至 2016-09-01","state":1},{"title":"任务1","detail":"任务描述：针对公司新产品XXX的市场调研。需要来店里咨询的客户填写问卷，并留联系方式。问卷已发送至各位邮箱，请下载并打印。","publishdate":"2016-06-01","executedate":"2016-06-01 至 2016-09-01","state":1},{"title":"任务1","detail":"任务描述：针对公司新产品XXX的市场调研。需要来店里咨询的客户填写问卷，并留联系方式。问卷已发送至各位邮箱，请下载并打印。","publishdate":"2016-06-01","executedate":"2016-06-01 至 2016-09-01","state":1}]
     */

    private int code;
    private String msg;
    /**
     * title : 任务1
     * detail : 任务描述：针对公司新产品XXX的市场调研。需要来店里咨询的客户填写问卷，并留联系方式。问卷已发送至各位邮箱，请下载并打印。
     * publishdate : 2016-06-01
     * executedate : 2016-06-01 至 2016-09-01
     * state : 1
     */

    private List<Task> body;

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

    public List<Task> getBody() {
        return body;
    }

    public void setBody(List<Task> body) {
        this.body = body;
    }
}
