package com.torch.chainmanage.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Task extends DataSupport implements Serializable {
    private int id;
    private String title;
    private String detail;
    private String publishdate;
    private String executedate;
    private int state;

    public Task() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(String publishdate) {
        this.publishdate = publishdate;
    }

    public String getExecutedate() {
        return executedate;
    }

    public void setExecutedate(String executedate) {
        this.executedate = executedate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
