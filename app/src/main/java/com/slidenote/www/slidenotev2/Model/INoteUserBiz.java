package com.slidenote.www.slidenotev2.Model;

import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public interface INoteUserBiz {
    void getAllNote(BaseListener.OnGetNotesListener onGetNotesListener);
    void getNoteFolders(BaseListener.OnGetNoteFoldersListener onGetNoteFoldersListener);
    void updateNote(Note note , BaseListener.OnEventListener onEventListener);
    void storeNewNote(Note note, String folderName , BaseListener.OnEventListener onEventListener);
    void moveNote(List<Note> notes, String folderName, BaseListener.OnEventListener onEventListener);
    void deleteNote(List<Note> notes, BaseListener.OnEventListener onEventListener);
    void addNewFolder(String name, BaseListener.OnEventListener onEventListener);
}
