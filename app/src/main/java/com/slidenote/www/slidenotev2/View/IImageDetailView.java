package com.slidenote.www.slidenotev2.View;

import com.slidenote.www.slidenotev2.Model.Image;

import java.util.List;

/**
 * Created by Cieo233 on 3/30/2017.
 */

public interface IImageDetailView {
    void showDialog();
    void toOCR();
    void toNewNote();
    void toEdit();
    void backToMainList(Boolean isDeleted);
    void deleteImage();
    void refreshContent(List<Image> images, int position);
}
