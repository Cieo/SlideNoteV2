package com.slidenote.www.slidenotev2.View;

import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;

import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public interface IImageMoveToView {
    String getNewFolderName();
    void refreshContent(List<ImageFolder>folders);
    void showDialog();
    void hideDialog();
    void showAnimation();
    void backToMainListActivity(Boolean isMoved);
    void setImage(Image image, int size);
    void showDecorate();
    void hideDecorate();
}
