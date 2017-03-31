package com.slidenote.www.slidenotev2.Model;


import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public class NoteUserBiz implements INoteUserBiz {
    @Override
    public void getAllNote(BaseListener.OnGetAllNoteListener OnGetAllNoteListener) {
        List<Note> notes = DataSupport.findAll(Note.class,true);
        OnGetAllNoteListener.getAllNoteSuccess(notes);
    }

    @Override
    public void getNoteFolders(BaseListener.OnGetNoteFoldersListener onGetNoteFoldersListener) {
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
        onGetNoteFoldersListener.getNoteFoldersSuccess(folders);
    }

    @Override
    public void updateNote(Note note, BaseListener.OnUpdateNoteListener onUpdateNoteListener) {
        note.save();
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
        onUpdateNoteListener.updateNoteSuccess(folders);
    }

    @Override
    public void storeNewNote(Note note, NoteFolder folder, BaseListener.OnStoreNewNoteListener onStoreNewNoteListener) {
        note.setFolder(folder);
        note.save();
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
        onStoreNewNoteListener.storeNewNoteSuccess(folders);
    }

    @Override
    public void moveNote(List<Note> notes, NoteFolder folder, BaseListener.OnMoveNoteListener onMoveNoteListener) {
        for (Note note : notes){
            note.setFolder(folder);
            note.save();
        }
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
        onMoveNoteListener.moveNoteSuccess(folders);
    }

    @Override
    public void deleteNote(List<Note> notes, BaseListener.OnDeleteNoteListener onDeleteNoteListener) {
        for (Note note : notes){
            note.delete();
        }
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
        onDeleteNoteListener.deleteNoteSuccess(folders);
    }

    @Override
    public void addNewFolder(String name, BaseListener.OnAddNewNoteFolderListener onAddNewNoteFolderListener) {
        if (DataSupport.isExist(NoteFolder.class,"name=?",name)){
            onAddNewNoteFolderListener.addNewNoteFolderFail();
            return;
        }
        NoteFolder folder = new NoteFolder.Builder(name).build();
        folder.save();
        List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
        onAddNewNoteFolderListener.addNewNoteFolderSuccess(folders);
    }
}
