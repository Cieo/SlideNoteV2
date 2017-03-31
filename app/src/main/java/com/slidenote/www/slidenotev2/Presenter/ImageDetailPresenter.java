package com.slidenote.www.slidenotev2.Presenter;

import android.content.Intent;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;
import com.slidenote.www.slidenotev2.View.IImageDetailView;
import com.slidenote.www.slidenotev2.View.ImageListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/30/2017.
 */

public class ImageDetailPresenter {
    private ImageUserBiz imageUserBiz = new ImageUserBiz();
    private IImageDetailView iImageDetailView;
    private Boolean isDeleted;
    private List<Image> deleted;

    public ImageDetailPresenter(IImageDetailView iImageDetailView) {
        this.iImageDetailView = iImageDetailView;
        isDeleted = false;
        deleted = new ArrayList<>();
    }

    public void onActivityStart(Intent intent){
        final String currentFolder = intent.getStringExtra("currentFolder");
        int position = intent.getIntExtra("position",0);
        final List<Image> showingImages = new ArrayList<>();
        if (currentFolder.equals(ImageListView.ALL)){
            imageUserBiz.getAllImage(new BaseListener.OnGetAllImageListener() {
                @Override
                public void getAllImageSuccess(List<Image> images) {
                    showingImages.addAll(images);
                }

                @Override
                public void getAllImageFail() {

                }
            });
        }else {
            imageUserBiz.getImageFolders(new BaseListener.OnGetImageFoldersListener() {
                @Override
                public void getImageFoldersSuccess(List<ImageFolder> folders) {
                    ImageFolder folder = Util.findImageFolder(folders,currentFolder);
                    if (folder!=null){
                        showingImages.addAll(folder.getImages());
                    }
                }

                @Override
                public void getImageFoldersFail() {

                }
            });
        }
        iImageDetailView.refreshContent(showingImages, position);
    }

    public void ocrClick(){

    }

    public void newNoteClick(){

    }

    public void editClick(){

    }

    public void shareClick(){

    }

    public void backClick(){
        if (!deleted.isEmpty()){
            isDeleted = true;
            imageUserBiz.deleteImage(deleted, new BaseListener.OnDeleteImageListener() {
                @Override
                public void deleteImageSuccess(List<ImageFolder> folders) {

                }

                @Override
                public void deleteImageFail() {

                }
            });
        }
        iImageDetailView.backToMainList(isDeleted);
    }

    public void deleteClick(Image image){
        deleted.add(image);
        iImageDetailView.deleteImage();
    }
}
