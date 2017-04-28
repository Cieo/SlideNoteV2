package com.slidenote.www.slidenotev2.View;

import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.Model.NoteFolder;

import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public interface INoteMoveToView {
    String getNewFolderName();
    void refreshContent(List<NoteFolder> folders);
    void showDialog();
    void hideDialog();
    void showAnimation();
    void backToMainListActivity(Boolean isMoved);
    void setNote(Note note, int size);
    void showDecorate();
    void hideDecorate();
}
