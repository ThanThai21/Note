package com.esp.note;


public class Item {

    private int id;
    private String title;
    private String content;
    private int backgroundResId;

    public Item(int id, String title, String description, int backgroundResId) {
        this.id = id;
        this.title = title;
        this.content = description;
        this.backgroundResId = backgroundResId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
