package com.slidenote.www.slidenotev2.Model;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class NoteFolder extends DataSupport {
    private String name;
    private List<Note> notes;

    private NoteFolder(Builder builder) {
        this.name = builder.name;
        this.notes = builder.notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public static class Builder {
        private String name;
        private List<Note> notes;

        public Builder(String name) {
            this.name = name;
            notes = new ArrayList<>();
        }

        public Builder notes(List<Note> notes) {
            this.notes = notes;
            return this;
        }

        public NoteFolder build() {
            return new NoteFolder(this);
        }
    }
}
