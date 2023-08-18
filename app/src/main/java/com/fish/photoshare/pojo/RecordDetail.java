package com.fish.photoshare.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class RecordDetail implements Serializable {
    private int collectId;
    private int collectNum;
    private String content;
    private long createTime;
    private boolean hasCollect;
    private boolean hasFocus;
    private boolean hasLike;
    private int id;
    private int imageCode;
    private ArrayList<String> imageUrlList;
    private int likeId;
    private int likeNum;
    private int pUserId;
    private String title;
    private String username;
    public RecordDetail(){}
    public RecordDetail(int collectId, int collectNum, String content, long createTime, boolean hasCollect, boolean hasFocus, boolean hasLike, int id, int imageCode, ArrayList<String> imageUrlList, int likeId, int likeNum, int pUserId, String title, String username) {
        this.collectId = collectId;
        this.collectNum = collectNum;
        this.content = content;
        this.createTime = createTime;
        this.hasCollect = hasCollect;
        this.hasFocus = hasFocus;
        this.hasLike = hasLike;
        this.id = id;
        this.imageCode = imageCode;
        this.imageUrlList = imageUrlList;
        this.likeId = likeId;
        this.likeNum = likeNum;
        this.pUserId = pUserId;
        this.title = title;
        this.username = username;
    }
    public int getCollectId() {
        return collectId;
    }
    public void setCollectId(int collectId) {
        this.collectId = collectId;
    }
    public int getCollectNum() {
        return collectNum;
    }
    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public boolean isHasCollect() {
        return hasCollect;
    }
    public void setHasCollect(boolean hasCollect) {
        this.hasCollect = hasCollect;
    }
    public boolean isHasFocus() {
        return hasFocus;
    }
    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
    public boolean isHasLike() {
        return hasLike;
    }
    public void setHasLike(boolean hasLike) {
        this.hasLike = hasLike;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getImageCode() {
        return imageCode;
    }
    public void setImageCode(int imageCode) {
        this.imageCode = imageCode;
    }
    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }
    public void setImageUrlList(ArrayList<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    public int getLikeId() {
        return likeId;
    }
    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }
    public int getLikeNum() {
        return likeNum;
    }
    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
    public int getPUserId() {
        return pUserId;
    }
    public void setPUserId(int pUserId) {
        this.pUserId = pUserId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return "Record{" +
                "collectId=" + collectId +
                ", collectNum=" + collectNum +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", hasCollect=" + hasCollect +
                ", hasFocus=" + hasFocus +
                ", hasLike=" + hasLike +
                ", id=" + id +
                ", imageCode=" + imageCode +
                ", imageUrlList=" + imageUrlList +
                ", likeId=" + likeId +
                ", likeNum=" + likeNum +
                ", pUserId=" + pUserId +
                ", title='" + title + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}