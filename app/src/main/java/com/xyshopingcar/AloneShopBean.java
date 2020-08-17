package com.xyshopingcar;

/**
 * Created by Xia_焱 on 2020/5/26.
 * 邮箱：xiaohaotianV@163.com
 * 但商品伪数据 Bean
 */
public class AloneShopBean {

    private String id;
    private String name;
    private String describe;
    private int num;
    private boolean isChecked;
    private String money;

    public AloneShopBean(String id, String name, String describe, int num, String money) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.num = num;
        this.money = money;
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
