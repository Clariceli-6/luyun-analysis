package com.luyun.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjectiveTypeCountVO {
    private int type;

    private int count;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
