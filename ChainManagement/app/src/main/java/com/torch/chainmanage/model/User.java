package com.torch.chainmanage.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/7/7 0007.
 */

public class User extends DataSupport {
    private String name;
    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
