package com.slidenote.www.slidenotev2.Model;

import org.litepal.crud.DataSupport;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class Image extends DataSupport {
    private int id;
    private String path, name;
    private ImageFolder folder;

    private Image(Builder builder) {
        path = builder.path;
        name = builder.name;
        folder = builder.folder;
    }

    public int getId() {
        return id;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageFolder getFolder() {
        return folder;
    }

    public void setFolder(ImageFolder folder) {
        this.folder = folder;
    }

    public static class Builder {
        private String path, name;
        private ImageFolder folder;

        public Builder(String name, String path) {
            this.path = path;
            this.name = name;
        }

        public Builder folder(ImageFolder folder) {
            this.folder = folder;
            return this;
        }

        public Image build() {
            return new Image(this);
        }
    }
}
