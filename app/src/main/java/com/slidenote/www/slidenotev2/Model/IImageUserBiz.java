package com.slidenote.www.slidenotev2.Model;

import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public interface IImageUserBiz {
    void getAllImage(BaseListener.OnGetImagesListener onGetImagesListener);
    void getImageFolders(BaseListener.OnGetImageFoldersListener onGetImageFoldersListener);
    void storeNewImage(Image image, String folderName, BaseListener.OnEventListener onEventListener);
    void moveImage(List<Image> images, String folderName, BaseListener.OnEventListener onEventListener);
    void deleteImage(List<Image> images, BaseListener.OnEventListener onEventListener);
    void addNewImageFolder(String name, BaseListener.OnEventListener onEventListener);
    void scanImage(BaseListener.OnEventListener onEventListener);
}
