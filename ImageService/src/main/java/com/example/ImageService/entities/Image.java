package com.example.ImageService.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedEntityGraph;

//@Entity
public class Image {

//    @Id @GeneratedValue
    private int id;

    public Image(int id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    private String title;

    public Image(String title, String url) {
        this.title = title;
        this.url = url;
    }

    private String url;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}