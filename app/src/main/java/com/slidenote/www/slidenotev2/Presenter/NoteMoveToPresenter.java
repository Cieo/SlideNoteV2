package com.slidenote.www.slidenotev2.Presenter;

import android.content.Intent;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;
import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.Model.NoteFolder;
import com.slidenote.www.slidenotev2.Model.NoteUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;
import com.slidenote.www.slidenotev2.View.IImageMoveToView;
import com.slidenote.www.slidenotev2.View.INoteMoveToView;
import com.slidenote.www.slidenotev2.View.ImageListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public class NoteMoveToPresenter implements BaseListener.OnGetNotesListener, BaseListener.OnGetNoteFoldersListener, BaseListener.OnEventListener {
    private INoteMoveToView iNoteMoveToView;
    private List<Integer> selected;
    private String currentFolder;
    private NoteUserBiz noteUserBiz = new NoteUserBiz();
    private List<NoteFolder> folders;
    private List<Note> all;
    private Boolean isMoved;

    public NoteMoveToPresenter(INoteMoveToView iNoteMoveToView) {
        this.iNoteMoveToView = iNoteMoveToView;
        noteUserBiz.getNoteFolders(this);
        noteUserBiz.getAllNote(this);
        isMoved = false;
    }

    public void itemClick(String folderName, int[] xy){
        List<Note> notes = new ArrayList<>();
        List<Note> srcNotes;
        if (currentFolder.equals(ImageListView.ALL)){
            srcNotes = all;
        }else {
            srcNotes = Util.findNoteFolder(folders,currentFolder).getNotes();
        }
        for (int i : selected){
            notes.add(srcNotes.get(i));
        }
        noteUserBiz.moveNote(notes, folderName, this);
        iNoteMoveToView.refreshContent(folders);
        selected.clear();
        isMoved = true;
    }

    public void onActivityStart(Intent intent){
        selected = intent.getIntegerArrayListExtra("selected");
        currentFolder = intent.getStringExtra("currentFolder");
        iNoteMoveToView.refreshContent(folders);
        Note note;
        if (currentFolder.equals(ImageListView.ALL)){
            note = all.get(selected.get(0));
            iNoteMoveToView.setNote(note, selected.size());
        }else {
            NoteFolder folder = Util.findNoteFolder(folders,currentFolder);
            if (folder != null){
                note = folder.getNotes().get(selected.get(0));
                iNoteMoveToView.setNote(note,selected.size());
            }
        }
    }

    public void backToMainListActivity(){
        iNoteMoveToView.backToMainListActivity(isMoved);
    }

    public void addFolderClick(){
        iNoteMoveToView.showDialog();
    }

    public void addFolderConfirm(){
        String name = iNoteMoveToView.getNewFolderName();
        noteUserBiz.addNewFolder(name, this);
        iNoteMoveToView.refreshContent(folders);
        iNoteMoveToView.hideDialog();
    }

    public void addFolderCancel(){
        iNoteMoveToView.hideDialog();
    }

    @Override
    public void getNotesSuccess(List<Note> notes) {
        all = notes;
    }

    @Override
    public void getNotesFail() {

    }

    @Override
    public void getNoteFoldersSuccess(List<NoteFolder> folders) {
        NoteMoveToPresenter.this.folders = folders;
    }

    @Override
    public void getNoteFoldersFail() {

    }

    @Override
    public void eventSuccess() {
        noteUserBiz.getNoteFolders(this);
    }

    @Override
    public void eventFail() {

    }
}
