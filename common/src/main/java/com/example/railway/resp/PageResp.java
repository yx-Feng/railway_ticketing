package com.example.railway.resp;

import java.io.Serializable;
import java.util.List;

public class PageResp<T> implements Serializable {

    private Long total; // 总条数

    private List<T> list; // 当前页的列表

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
