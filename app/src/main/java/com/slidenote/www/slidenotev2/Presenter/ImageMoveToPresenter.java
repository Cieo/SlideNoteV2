package com.slidenote.www.slidenotev2.Presenter;

import android.content.Intent;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;
import com.slidenote.www.slidenotev2.View.IImageMoveToView;
import com.slidenote.www.slidenotev2.View.ImageListView;
import com.slidenote.www.slidenotev2.View.ImageMoveToView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public class ImageMoveToPresenter {
    private IImageMoveToView iImageMoveToView;
    private List<Integer> selected;
    private String currentFolder;
    private ImageUserBiz imageUserBiz = new ImageUserBiz();
    private List<ImageFolder> folders;
    private List<Image> all;
    private Boolean isMoved;

    public ImageMoveToPresenter(IImageMoveToView iImageMoveToView) {
        this.iImageMoveToView = iImageMoveToView;
        imageUserBiz.getImageFolders(new BaseListener.OnGetImageFoldersListener() {
            @Override
            public void getImageFoldersSuccess(List<ImageFolder> folders) {
                ImageMoveToPresenter.this.folders = folders;
            }

            @Override
            public void getImageFoldersFail() {

            }
        });
        imageUserBiz.getAllImage(new BaseListener.OnGetAllImageListener() {
            @Override
            public void getAllImageSuccess(List<Image> images) {
                all = images;
            }

            @Override
            public void getAllImageFail() {

            }
        });
        isMoved = false;
    }

    public void itemClick(ImageFolder folder, int[] xy){
        List<Image> images = new ArrayList<>();
        List<Image> srcImages;
        if (currentFolder.equals(ImageListView.ALL)){
            srcImages = all;
        }else {
            srcImages = Util.findImageFolder(folders,currentFolder).getImages();
        }
        for (int i : selected){
            images.add(srcImages.get(i));
        }
        imageUserBiz.moveImage(images, folder, new BaseListener.OnMoveImageListener() {
            @Override
            public void moveImageSuccess(List<ImageFolder> folders) {
                ImageMoveToPresenter.this.folders = folders;
            }

            @Override
            public void moveImageFail() {

            }
        });
        iImageMoveToView.refreshContent(folders);
        selected.clear();
        isMoved = true;
    }

    public void onActivityStart(Intent intent){
        selected = intent.getIntegerArrayListExtra("selected");
        currentFolder = intent.getStringExtra("currentFolder");
        iImageMoveToView.refreshContent(folders);
        Image image;
        if (currentFolder.equals(ImageListView.ALL)){
            image = all.get(selected.get(0));
            iImageMoveToView.setImage(image);
        }else {
            ImageFolder folder = Util.findImageFolder(folders,currentFolder);
            if (folder != null){
                image = folder.getImages().get(selected.get(0));
                iImageMoveToView.setImage(image);
            }
        }
    }

    public void backToMainListActivity(){
        iImageMoveToView.backToMainListActivity(isMoved);
    }

    public void addFolderClick(){
        iImageMoveToView.showDialog();
    }

    public void addFolderConfirm(){
        String name = iImageMoveToView.getNewFolderName();
        imageUserBiz.addNewImageFolder(name, new BaseListener.OnAddNewImageFolderListener() {
            @Override
            public void addNewImageFolderSuccess(List<ImageFolder> folders) {
                ImageMoveToPresenter.this.folders = folders;
            }

            @Override
            public void addNewImageFolderFail() {

            }
        });
        iImageMoveToView.refreshContent(folders);
        iImageMoveToView.hideDialog();
    }

    public void addFolderCancel(){
        iImageMoveToView.hideDialog();
    }
}
