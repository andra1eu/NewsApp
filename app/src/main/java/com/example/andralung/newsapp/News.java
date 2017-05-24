package com.example.andralung.newsapp;

public class News {

    private String title;
    private String section;
    private final String url;

    public News(String title, String section, String url) {
        this.title = title;
        this.section = section;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return section;
    }

    public String getUrl() {
        return url;
    }

}
