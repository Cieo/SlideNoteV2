package com.slidenote.www.slidenotev2.Presenter;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.Model.NoteFolder;
import com.slidenote.www.slidenotev2.Model.NoteUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;
import com.slidenote.www.slidenotev2.View.INoteListView;
import com.slidenote.www.slidenotev2.View.ImageListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public class NoteListPresenter {
    private NoteUserBiz noteUserBiz = new NoteUserBiz();
    private INoteListView iNoteListView;
    private boolean isSelectMode;
    private String currentFolder;
    private List<Integer> selected;
    private List<NoteFolder> folders;
    private List<Note> all;
    private int highLightPosition;

    public NoteListPresenter(INoteListView iNoteListView) {
        this.iNoteListView = iNoteListView;
        isSelectMode = false;
        currentFolder = ImageListView.ALL;
        selected = new ArrayList<>();
        folders = DataSupport.findAll(NoteFolder.class,true);
        all = DataSupport.findAll(Note.class,true);
        highLightPosition = -1;
    }

    public void changeToolbarBtnState(String currentBtnText) {
        if (currentBtnText.equals("选择")){
            isSelectMode = true;
            iNoteListView.showCancelButton();
            iNoteListView.showBottomMenu();
        } else if (currentBtnText.equals("取消")){
            isSelectMode = false;
            iNoteListView.hideCancelButton();
            iNoteListView.hideBottomMenu();
            iNoteListView.hideAllCheckSign();
        }
    }

    public void itemClick(Note note, int position) {
        if (isSelectMode){
            iNoteListView.showHideSingleCheckSign(position);
            if (position != 0){
                if (selected.contains(position-1)){
                        selected.remove(Integer.valueOf(position-1));
                    }else {
                        selected.add(position-1);
                }
            }
        }else {
            iNoteListView.toDetailActivity(currentFolder,position-1);
        }
    }

    public void deleteSelected() {
        List<Note> notes = new ArrayList<>();
        if (currentFolder.equals(ImageListView.ALL)){
            for (int i : selected){
                notes.add(all.get(i));
            }
        } else {
            NoteFolder folder = Util.findNoteFolder(folders,currentFolder);
            if (folder!=null){
                for (int i : selected){
                    notes.add(folder.getNotes().get(i));
                }
            }
        }
        noteUserBiz.deleteNote(notes, new BaseListener.OnDeleteNoteListener() {
            @Override
            public void deleteNoteSuccess(List<NoteFolder> folders) {

            }

            @Override
            public void deleteNoteFail() {

            }
        });
        noteUserBiz.getAllNote(new BaseListener.OnGetAllNoteListener() {
            @Override
            public void getAllNoteSuccess(List<Note> note) {
                NoteListPresenter.this.all = note;
            }

            @Override
            public void getAllNoteFail() {

            }
        });
        noteUserBiz.getNoteFolders(new BaseListener.OnGetNoteFoldersListener() {
            @Override
            public void getNoteFoldersSuccess(List<NoteFolder> folders) {
                NoteListPresenter.this.folders = folders;
            }

            @Override
            public void getNoteFoldersFail() {

            }
        });
        iNoteListView.refreshDrawerList(folders,highLightPosition);
        iNoteListView.refreshDrawerItem(all,highLightPosition);
        if (currentFolder.equals(ImageListView.ALL)){
            iNoteListView.refreshContentList(all);
        }else {
            NoteFolder folder = Util.findNoteFolder(folders,currentFolder);
            if (folder!=null){
                iNoteListView.refreshContentList(folder.getNotes());
            }
        }
        iNoteListView.hideCancelButton();
        iNoteListView.hideBottomMenu();
        isSelectMode = false;
    }

    public void folderClick(String folderName, int position) {
        currentFolder = folderName;
        highLightPosition = position;

        if (currentFolder.equals(ImageListView.ALL)){
            iNoteListView.refreshContentList(all);
        }else {
            NoteFolder folder = Util.findNoteFolder(folders,currentFolder);
            if (folder!=null){
                iNoteListView.refreshContentList(folder.getNotes());
            }
        }
        iNoteListView.setHighLightButton(position);
        iNoteListView.hideDrawer();
    }

    public void addNewFolderClick() {
        iNoteListView.showDialog();
    }

    public void changeDrawerState(Boolean isDrawerOpen) {
        if (isDrawerOpen){
            iNoteListView.hideDrawer();
        }else {
            if (!isSelectMode){
                iNoteListView.showDrawer();
            }
        }
    }

    public void addNewFolderConfirm() {
        String name = iNoteListView.getNewFolderName();
        noteUserBiz.addNewFolder(name, new BaseListener.OnAddNewNoteFolderListener() {

            @Override
            public void addNewNoteFolderSuccess(List<NoteFolder> folders) {

            }

            @Override
            public void addNewNoteFolderFail() {

            }
        });
        noteUserBiz.getAllNote(new BaseListener.OnGetAllNoteListener() {
            @Override
            public void getAllNoteSuccess(List<Note> notes) {
                NoteListPresenter.this.all = notes;
            }

            @Override
            public void getAllNoteFail() {

            }
        });
        noteUserBiz.getNoteFolders(new BaseListener.OnGetNoteFoldersListener() {
            @Override
            public void getNoteFoldersSuccess(List<NoteFolder> folders) {

                NoteListPresenter.this.folders = folders;
            }

            @Override
            public void getNoteFoldersFail() {

            }
        });
        iNoteListView.refreshDrawerList(folders,highLightPosition);
        iNoteListView.refreshDrawerItem(all,highLightPosition);
        if (currentFolder.equals(ImageListView.ALL)){
            iNoteListView.refreshContentList(all);
        }else {
            NoteFolder folder = Util.findNoteFolder(folders,currentFolder);
            if (folder!=null){
                iNoteListView.refreshContentList(folder.getNotes());
            }
        }
        iNoteListView.hideDialog();
    }

    public void addNewFolderCancel() {
        iNoteListView.hideDialog();
    }

    public void refreshDrawer() {
        noteUserBiz.getAllNote(new BaseListener.OnGetAllNoteListener() {
            @Override
            public void getAllNoteSuccess(List<Note> notes) {
                NoteListPresenter.this.all = notes;
            }

            @Override
            public void getAllNoteFail() {

            }
        });
        noteUserBiz.getNoteFolders(new BaseListener.OnGetNoteFoldersListener() {
            @Override
            public void getNoteFoldersSuccess(List<NoteFolder> folders) {
                NoteListPresenter.this.folders = folders;
            }

            @Override
            public void getNoteFoldersFail() {

            }
        });
        iNoteListView.refreshDrawerList(folders,highLightPosition);
        iNoteListView.refreshDrawerItem(all,highLightPosition);
    }

    public void refreshContent() {
        noteUserBiz.getAllNote(new BaseListener.OnGetAllNoteListener() {
            @Override
            public void getAllNoteSuccess(List<Note> notes) {
                NoteListPresenter.this.all = notes;
            }

            @Override
            public void getAllNoteFail() {

            }
        });
        noteUserBiz.getNoteFolders(new BaseListener.OnGetNoteFoldersListener() {
            @Override
            public void getNoteFoldersSuccess(List<NoteFolder> folders) {
                NoteListPresenter.this.folders = folders;
            }

            @Override
            public void getNoteFoldersFail() {

            }
        });
        if (currentFolder.equals(ImageListView.ALL)){
            iNoteListView.refreshContentList(all);
        }else {
            NoteFolder folder = Util.findNoteFolder(folders,currentFolder);
            if (folder!=null){
                iNoteListView.refreshContentList(folder.getNotes());
            }
        }
    }

    public void toNoteActivity() {
        iNoteListView.toImageListActivity();
    }


    public void fromDetailActivity() {

    }

    public void toMoveToActivity() {
        isSelectMode = false;
        iNoteListView.toMoveToActivity(new ArrayList<Integer>(selected), currentFolder);
        iNoteListView.hideBottomMenu();
        iNoteListView.hideCancelButton();
        iNoteListView.hideAllCheckSign();
        selected.clear();
    }

    public void fromMoveToActivity() {

    }

    public void addSimData(){
        Note note = new  Note.Builder("test","1/9").build();
        note.save();
    }
}
