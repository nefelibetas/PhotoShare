package com.fish.photoshare.pojo;

import java.util.ArrayList;

public class Record {
    private int current;
    private int size;
    private int total;
    private ArrayList<RecordDetail> records;
    public Record(){}
    public Record(int current, int size, int total, ArrayList<RecordDetail> records) {
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
    public ArrayList<RecordDetail> getRecordDetail() {
        return records;
    }
    public void setRecordDetail(ArrayList<RecordDetail> recordDetailList) {
        this.records = recordDetailList;
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
