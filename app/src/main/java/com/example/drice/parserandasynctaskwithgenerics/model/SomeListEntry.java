package com.example.drice.parserandasynctaskwithgenerics.model;

/**
 * Example model class of item acting as container of data being retrieved
 */
public class SomeListEntry {

    private String title;
    private String link;

    public SomeListEntry() {
        this.setTitle("");
        this.setLink("");
    }

    public SomeListEntry(String title, String link) {
        this.setLink(link);
        this.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}