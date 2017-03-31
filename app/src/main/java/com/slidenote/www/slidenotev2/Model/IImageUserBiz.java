package com.slidenote.www.slidenotev2.Model;

import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public interface IImageUserBiz {
    void getAllImage(BaseListener.OnGetAllImageListener onGetAllImageListener);
    void getImageFolders(BaseListener.OnGetImageFoldersListener onGetImageFoldersListener);
    void storeNewImage(Image image, ImageFolder folder,BaseListener.OnStoreNewImageListener onStoreNewImageListener);
    void moveImage(List<Image> images, ImageFolder folder, BaseListener.OnMoveImageListener onMoveImageListener);
    void deleteImage(List<Image> images, BaseListener.OnDeleteImageListener onDeleteImageListener);
    void addNewImageFolder(String name,BaseListener.OnAddNewImageFolderListener onAddNewImageFolderListener);
    void scanImage(BaseListener.OnScanImageListener onScanImageListener);
}
