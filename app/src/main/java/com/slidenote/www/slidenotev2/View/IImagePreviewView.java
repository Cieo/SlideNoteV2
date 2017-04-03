package com.slidenote.www.slidenotev2.View;

/**
 * Created by Cieo233 on 4/2/2017.
 */

public interface IImagePreviewView {
    void toOCRActivity(String path);
    void toImageDetailActivity(String currentFolder, int position);
    void backToPhotoActivity();
    void loadImage(String path);
}
