package com.slidenote.www.slidenotev2.Model;

import org.litepal.crud.DataSupport;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class Note extends DataSupport {
    private int id;
    private String title, date, content;
    private NoteFolder folder;

    private Note(Builder builder) {
        this.title = builder.title;
        this.date = builder.date;
        this.content = builder.content;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NoteFolder getFolder() {
        return folder;
    }

    public void setFolder(NoteFolder folder) {
        this.folder = folder;
    }

    public static class Builder {
        private String title, date, content;
        private NoteFolder folder;

        public Builder(String title, String date) {
            this.title = title;
            this.date = date;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder folder(NoteFolder folder) {
            this.folder = folder;
            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }
}
