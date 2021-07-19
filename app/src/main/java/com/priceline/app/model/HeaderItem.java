package com.priceline.app.model;

import java.util.ArrayList;

public class HeaderItem {

    private String type;
    private int count;
    private String id;

    public ArrayList<SubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(ArrayList<SubItem> subItems) {
        this.subItems = subItems;
    }

    ArrayList<SubItem> subItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
