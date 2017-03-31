package com.slidenote.www.slidenotev2.Model;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class ImageFolder extends DataSupport {
    private int id;
    private String name;
    private String path;
    private List<Image> images;

    private ImageFolder(Builder builder) {
        this.name = builder.name;
        this.images = builder.images;
        this.path = builder.path;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public static class Builder {
        private String name, path;
        private List<Image> images;

        public Builder(String name, String path) {
            this.name = name;
            this.path = path;
            this.images = new ArrayList<>();
        }

        public Builder images(List<Image> images) {
            this.images = images;
            return this;
        }

        public ImageFolder build() {
            return new ImageFolder(this);
        }
    }
}
