package com.fish.photoshare.pojo;

public class CommentDetail {
    private String appKey;
    private String commentLevel;
    private String content;
    private String createTime;
    private String id;
    private String pUserId;
    private String parentCommentId;
    private String parentCommentUserId;
    private String replyCommentId;
    private String replyCommentUserId;
    private String shareId;
    private String userName;

    public CommentDetail(String appKey, String commentLevel, String content, String createTime, String id, String pUserId, String parentCommentId, String parentCommentUserId, String replyCommentId, String replyCommentUserId, String shareId, String userName) {
        this.appKey = appKey;
        this.commentLevel = commentLevel;
        this.content = content;
        this.createTime = createTime;
        this.id = id;
        this.pUserId = pUserId;
        this.parentCommentId = parentCommentId;
        this.parentCommentUserId = parentCommentUserId;
        this.replyCommentId = replyCommentId;
        this.replyCommentUserId = replyCommentUserId;
        this.shareId = shareId;
        this.userName = userName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(String commentLevel) {
        this.commentLevel = commentLevel;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpUserId() {
        return pUserId;
    }

    public void setpUserId(String pUserId) {
        this.pUserId = pUserId;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getParentCommentUserId() {
        return parentCommentUserId;
    }

    public void setParentCommentUserId(String parentCommentUserId) {
        this.parentCommentUserId = parentCommentUserId;
    }

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getReplyCommentUserId() {
        return replyCommentUserId;
    }

    public void setReplyCommentUserId(String replyCommentUserId) {
        this.replyCommentUserId = replyCommentUserId;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "CommentDetail{" +
                "appKey='" + appKey + '\'' +
                ", commentLevel='" + commentLevel + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", id='" + id + '\'' +
                ", pUserId='" + pUserId + '\'' +
                ", parentCommentId='" + parentCommentId + '\'' +
                ", parentCommentUserId='" + parentCommentUserId + '\'' +
                ", replyCommentId='" + replyCommentId + '\'' +
                ", replyCommentUserId='" + replyCommentUserId + '\'' +
                ", shareId='" + shareId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
