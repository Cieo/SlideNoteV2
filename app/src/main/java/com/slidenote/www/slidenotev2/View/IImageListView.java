package com.slidenote.www.slidenotev2.View;

import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;

import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public interface IImageListView {
    void showBottomMenu();

    void hideBottomMenu();

    void refreshDrawerList(List<ImageFolder> folders, int highLightPosition);

    void refreshDrawerItem(List<Image> images, int highLightPosition);

    void refreshContentList(List<Image> images);

    void hideDrawer();

    void showDrawer();

    void showCancelButton();

    void hideCancelButton();

    void toMoveToActivity(List<Integer> selected, String currentFolder);

    void toDetailActivity(String folder, int position);

    void setHighLightButton(int position);

    void toNoteListActivity();

    void showDialog();

    void hideDialog();

    String getNewFolderName();

    void showHideSingleCheckSign(int position);

    void hideAllCheckSign();
}
