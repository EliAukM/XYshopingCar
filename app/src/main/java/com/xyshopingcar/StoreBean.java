package com.xyshopingcar;

/**
 * Created by Xia_焱 on 2020/5/26.
 * 邮箱：xiaohaotianV@163.com
 * 多店铺 ·店铺信息
 */
public class StoreBean {
    private String id;
    private String name;
    private boolean isChecked;


    public StoreBean(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
