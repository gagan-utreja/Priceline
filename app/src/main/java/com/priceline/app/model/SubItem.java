package com.priceline.app.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SubItem {

    private String name;
    private Date date;
    private String listNameEncoded;

    public String getListNameEncoded() {
        return listNameEncoded;
    }

    public void setListNameEncoded(String listNameEncoded) {
        this.listNameEncoded = listNameEncoded;
    }

    private String id;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String date() {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
