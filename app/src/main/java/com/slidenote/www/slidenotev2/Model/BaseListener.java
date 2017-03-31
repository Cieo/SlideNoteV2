package com.slidenote.www.slidenotev2.Model;

import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class BaseListener {
    public interface OnGetAllImageListener{
        void getAllImageSuccess(List<Image> images);

        void getAllImageFail();
    }

    public interface OnGetImageFoldersListener {
        void getImageFoldersSuccess(List<ImageFolder> folders);

        void getImageFoldersFail();
    }

    public interface OnStoreNewImageListener {
        void storeNewImageSuccess(List<ImageFolder> folders);

        void storeNewImagefail();
    }

    public interface OnMoveImageListener {
        void moveImageSuccess(List<ImageFolder> folders);

        void moveImageFail();
    }

    public interface OnDeleteImageListener {
        void deleteImageSuccess(List<ImageFolder> folders);

        void deleteImageFail();
    }

    public interface OnAddNewImageFolderListener {
        void addNewImageFolderSuccess(List<ImageFolder> folders);

        void addNewImageFolderFail();
    }

    public interface OnScanImageListener {
        void scanImageSuccess(List<ImageFolder> folders);

        void scanImageFail();
    }


    public interface OnGetAllNoteListener {
        void getAllNoteSuccess(List<Note> notes);

        void getAllNoteFail();
    }

    public interface OnGetNoteFoldersListener {
        void getNoteFoldersSuccess(List<NoteFolder> folders);

        void getNoteFoldersFail();
    }

    public interface OnUpdateNoteListener {
        void updateNoteSuccess(List<NoteFolder> folders);

        void updateNoteFail();
    }

    public interface OnStoreNewNoteListener {
        void storeNewNoteSuccess(List<NoteFolder> folders);

        void storeNewNoteFail();
    }

    public interface OnMoveNoteListener {
        void moveNoteSuccess(List<NoteFolder> folders);

        void moveNoteFail();
    }

    public interface OnDeleteNoteListener {
        void deleteNoteSuccess(List<NoteFolder> folders);

        void deleteNoteFail();
    }

    public interface OnAddNewNoteFolderListener {
        void addNewNoteFolderSuccess(List<NoteFolder> folders);

        void addNewNoteFolderFail();
    }
}
