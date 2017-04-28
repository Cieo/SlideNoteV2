package com.slidenote.www.slidenotev2.Model;


import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public class NoteUserBiz implements INoteUserBiz {
    @Override
    public void getAllNote(BaseListener.OnGetNotesListener onGetNotesListener) {
        List<Note> notes = DataSupport.findAll(Note.class,true);
        onGetNotesListener.getNotesSuccess(notes);
    }

    @Override
    public void getNoteFolders(BaseListener.OnGetNoteFoldersListener onGetNoteFoldersListener) {
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
        onGetNoteFoldersListener.getNoteFoldersSuccess(folders);
    }

    @Override
    public void updateNote(Note note, BaseListener.OnEventListener onEventListener) {
        note.save();
        onEventListener.eventSuccess();
    }

    @Override
    public void storeNewNote(Note note, String folderName, BaseListener.OnEventListener onEventListener) {
        NoteFolder folder = DataSupport.where("name=?",folderName).findFirst(NoteFolder.class,true);
        note.setFolder(folder);
        note.save();
        onEventListener.eventSuccess();
    }

    @Override
    public void moveNote(List<Note> notes, String folderName, BaseListener.OnEventListener onEventListener) {
        NoteFolder folder = DataSupport.where("name=?",folderName).findFirst(NoteFolder.class,true);
        for (Note note : notes){
            note.setFolder(folder);
            note.save();
        }
        onEventListener.eventSuccess();
    }

    @Override
    public void deleteNote(List<Note> notes, BaseListener.OnEventListener onEventListener) {
        for (Note note : notes){
            note.delete();
        }
        onEventListener.eventSuccess();
    }

    @Override
    public void addNewFolder(String name, BaseListener.OnEventListener onEventListener) {
        if (DataSupport.isExist(NoteFolder.class,"name=?",name)){
            onEventListener.eventFail();
            return;
        }
        NoteFolder folder = new NoteFolder.Builder(name).build();
        folder.save();
        onEventListener.eventSuccess();
    }
}
