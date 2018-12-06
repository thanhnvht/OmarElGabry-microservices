package com.example.GalleryService.entities;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

//@Entity
public class Gallery {
    private int id;
//    @Transient
    private List<Object> images;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setImages(List<Object> images) {
        this.images = images;
    }

    public List<Object> getImages() {
        return images;
    }
}
