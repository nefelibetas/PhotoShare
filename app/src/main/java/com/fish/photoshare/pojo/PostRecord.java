package com.fish.photoshare.pojo;

import java.util.ArrayList;

public class PostRecord {
    private int current;
    private int size;
    private int total;
    private ArrayList<PostDetail> records;

    public PostRecord(int current, int size, int total, ArrayList<PostDetail> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<PostDetail> getRecordDetail() {
        return records;
    }

    public void setRecordDetail(ArrayList<PostDetail> postDetailList) {
        this.records = postDetailList;
    }

    @Override
    public String toString() {
        return "Record{" +
                "current=" + current +
                ", size=" + size +
                ", total=" + total +
                ", recordDetailList=" + records +
                '}';
    }
}
