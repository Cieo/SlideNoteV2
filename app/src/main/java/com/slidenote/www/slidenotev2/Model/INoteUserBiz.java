package com.slidenote.www.slidenotev2.Model;

import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public interface INoteUserBiz {
    void getAllNote(BaseListener.OnGetAllNoteListener OnGetAllNoteListener);
    void getNoteFolders(BaseListener.OnGetNoteFoldersListener onGetNoteFoldersListener);
    void updateNote(Note note ,BaseListener.OnUpdateNoteListener onUpdateNoteListener);
    void storeNewNote(Note note, NoteFolder folder,BaseListener.OnStoreNewNoteListener onStoreNewNoteListener);
    void moveNote(List<Note> notes, NoteFolder folder, BaseListener.OnMoveNoteListener onMoveNoteListener);
    void deleteNote(List<Note> notes, BaseListener.OnDeleteNoteListener onDeleteNoteListener);
    void addNewFolder(String name, BaseListener.OnAddNewNoteFolderListener onAddNewNoteFolderListener);
}
