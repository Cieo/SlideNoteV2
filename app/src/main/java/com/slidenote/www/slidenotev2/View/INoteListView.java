package com.slidenote.www.slidenotev2.View;

import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.Model.NoteFolder;

import java.util.List;

/**
 * Created by Cieo233 on 3/30/2017.
 */

public interface INoteListView {
    void showBottomMenu();

    void hideBottomMenu();

    void refreshDrawerList(List<NoteFolder> folders, int highLightPosition);

    void refreshDrawerItem(List<Note> notes, int highLightPosition);

    void refreshContentList(List<Note> notes);

    void hideDrawer();

    void showDrawer();

    void showCancelButton();

    void hideCancelButton();

    void toMoveToActivity(List<Integer> selected, String currentFolder);

    void toDetailActivity(String folder, int position);

    void setHighLightButton(int position);

    void toImageListActivity();

    void showDialog();

    void hideDialog();

    String getNewFolderName();

    void showHideSingleCheckSign(int position);

    void hideAllCheckSign();

    void mergeNote(int srcPosition, int targetPosition, Note target);
}
