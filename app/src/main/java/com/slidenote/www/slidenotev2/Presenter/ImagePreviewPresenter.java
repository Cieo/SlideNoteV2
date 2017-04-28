package com.slidenote.www.slidenotev2.Presenter;

import android.content.Intent;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;
import com.slidenote.www.slidenotev2.View.IImagePreviewView;
import com.slidenote.www.slidenotev2.View.ImageListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 4/2/2017.
 */

public class ImagePreviewPresenter implements BaseListener.OnGetImagesListener, BaseListener.OnEventListener, BaseListener.OnGetImageFoldersListener {
    private IImagePreviewView iImagePreviewView;
    private ImageUserBiz imageUserBiz;
    private String currentFolder;
    private int position;
    private Image image;
    private List<Image> all;
    private List<ImageFolder> folders;

    public ImagePreviewPresenter(IImagePreviewView iImagePreviewView) {
        this.iImagePreviewView = iImagePreviewView;
        imageUserBiz = new ImageUserBiz();
        currentFolder = ImageListView.ALL;
        all = new ArrayList<>();
        folders = new ArrayList<>();
        imageUserBiz.getAllImage(this);
        imageUserBiz.getImageFolders(this);
    }

    public void onCompleteClick(){
        iImagePreviewView.toImageDetailActivity(currentFolder,position);
    }

    public void onActivityStart(Intent intent){
        currentFolder = intent.getStringExtra("currentFolder");
        position = intent.getIntExtra("position",0);
        if (currentFolder.equals(ImageListView.ALL)){
            image = all.get(position);
        }else {
            ImageFolder folder = Util.findImageFolder(folders,currentFolder);
            if (folder!=null){
                image = folder.getImages().get(position);
            }
        }
        iImagePreviewView.loadImage(image.getPath());
    }

    public void onDeleteClick(){
        List<Image> images = new ArrayList<>();
        images.add(image);
        imageUserBiz.deleteImage(images , this);
        iImagePreviewView.backToPhotoActivity();
    }

    public void onOCRClick(){
    }

    public void onOneMoreClick(){
        iImagePreviewView.backToPhotoActivity();
    }

    @Override
    public void getImagesSuccess(List<Image> images) {
        all.addAll(images);
    }

    @Override
    public void getImagesFail() {

    }

    @Override
    public void eventSuccess() {

    }

    @Override
    public void eventFail() {

    }

    @Override
    public void getImageFoldersSuccess(List<ImageFolder> folders) {
        ImagePreviewPresenter.this.folders.addAll(folders);
    }

    @Override
    public void getImageFoldersFail() {

    }
}
