package com.fish.photoshare.pojo;

import java.util.ArrayList;

public class CommentRecord {
    private int current;
    private int size;
    private int total;
    private ArrayList<CommentDetail> records;
    public CommentRecord(int current, int size, int total, ArrayList<CommentDetail> records) {
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
    public ArrayList<CommentDetail> getRecords() {
        return records;
    }
    public void setRecords(ArrayList<CommentDetail> records) {
        this.records = records;
    }
    @Override
    public String toString() {
        return "CommentRecord{" +
                "current=" + current +
                ", size=" + size +
                ", total=" + total +
                ", records=" + records +
                '}';
    }
}
