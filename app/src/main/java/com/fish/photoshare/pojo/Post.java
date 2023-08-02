package com.fish.photoshare.pojo;


public class Post {
    private String title;
    private String author;
    private int imageId;

    public Post(){}
    public Post(String title, String author, int imageId){
        this.title = title;
        this.author = author;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
