package com.nhx.gmall.bean;

import java.io.Serializable;

public class PmsSearchParam implements Serializable {
    private Long catalog3Id;

    private String keyword;

    private Long[] valueId;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(Long catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public Long[] getValueId() {
        return valueId;
    }

    public void setValueId(Long[] valueId) {
        this.valueId = valueId;
    }
}
