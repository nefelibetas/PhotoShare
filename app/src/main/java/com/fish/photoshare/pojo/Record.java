package com.fish.photoshare.pojo;

import java.util.ArrayList;

public class Record {
    private int current;
    private int size;
    private int total;
    private ArrayList<RecordDetail> recordDetailList;
    public Record(){}
    public Record(int current, int size, int total, ArrayList<RecordDetail> recordDetailList) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.recordDetailList = recordDetailList;
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
    public ArrayList<RecordDetail> getRecordDetail() {
        return recordDetailList;
    }
    public void setRecordDetail(ArrayList<RecordDetail> recordDetailList) {
        this.recordDetailList = recordDetailList;
    }
    @Override
    public String toString() {
        return "Record{" +
                "current=" + current +
                ", size=" + size +
                ", total=" + total +
                ", recordDetailList=" + recordDetailList +
                '}';
    }
}
