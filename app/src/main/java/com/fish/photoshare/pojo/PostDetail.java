package com.fish.photoshare.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class PostDetail implements Serializable {
    private String collectId;
    private String collectNum;
    private String content;
    private String createTime;
    private boolean hasCollect;
    private boolean hasFocus;
    private boolean hasLike;
    private String id;
    private String imageCode;
    private ArrayList<String> imageUrlList;
    private String likeId;
    private String likeNum;
    private String pUserId;
    private String title;
    private String username;

    public PostDetail(String collectId, String collectNum, String content, String createTime, boolean hasCollect, boolean hasFocus, boolean hasLike, String id, String imageCode, ArrayList<String> imageUrlList, String likeId, String likeNum, String pUserId, String title, String username) {
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
    public String getCollectId() {
        return collectId;
    }
    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }
    public String getCollectNum() {
        return collectNum;
    }
    public void setCollectNum(String collectNum) {
        this.collectNum = collectNum;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getImageCode() {
        return imageCode;
    }
    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }
    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }
    public void setImageUrlList(ArrayList<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    public String getLikeId() {
        return likeId;
    }
    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }
    public String getLikeNum() {
        return likeNum;
    }
    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }
    public String getPUserId() {
        return pUserId;
    }
    public void setPUserId(String pUserId) {
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