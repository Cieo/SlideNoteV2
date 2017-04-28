package com.slidenote.www.slidenotev2.Presenter;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;
import com.slidenote.www.slidenotev2.View.IImageListView;
import com.slidenote.www.slidenotev2.View.ImageListView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public class ImageListPresenter implements BaseListener.OnEventListener, BaseListener.OnGetImagesListener, BaseListener.OnGetImageFoldersListener {
    private ImageUserBiz imageUserBiz = new ImageUserBiz();
    private IImageListView iImageListView;
    private boolean isSelectMode;
    private String currentFolder;
    private List<Integer> selected;
    private List<ImageFolder> folders;
    private List<Image> all;
    private int highLightPosition;

    public ImageListPresenter(IImageListView iImageListView) {
        imageUserBiz.scanImage(this);
        this.iImageListView = iImageListView;
        isSelectMode = false;
        currentFolder = ImageListView.ALL;
        selected = new ArrayList<>();
        imageUserBiz.getImageFolders(this);
        imageUserBiz.getAllImage(this);
        highLightPosition = -1;
    }

    public void changeToolbarBtnState(String currentBtnText) {
        if (currentBtnText.equals("选择")){
            isSelectMode = true;
            iImageListView.showCancelButton();
            iImageListView.showBottomMenu();
        } else if (currentBtnText.equals("取消")){
            isSelectMode = false;
            iImageListView.hideCancelButton();
            iImageListView.hideBottomMenu();
            iImageListView.hideAllCheckSign();
        }
    }

    public void itemClick(Image image, int position) {
        if (isSelectMode){
            iImageListView.showHideSingleCheckSign(position);
            if (selected.contains(position)){
                selected.remove(Integer.valueOf(position));
            }else {
                selected.add(position);
            }
        }else {
            iImageListView.toDetailActivity(currentFolder,position);
        }
    }

    public void deleteSelected() {
        List<Image> images = new ArrayList<>();
        if (currentFolder.equals(ImageListView.ALL)){
            for (int i : selected){
                images.add(all.get(i));
            }
        } else {
            ImageFolder folder = Util.findImageFolder(folders,currentFolder);
            if (folder!=null){
                for (int i : selected){
                    images.add(folder.getImages().get(i));
                }
            }
        }

        imageUserBiz.deleteImage(images, this);
        imageUserBiz.getAllImage(this);
        imageUserBiz.getImageFolders(this);


        iImageListView.refreshDrawerList(folders,highLightPosition);
        iImageListView.refreshDrawerItem(all,highLightPosition);
        if (currentFolder.equals(ImageListView.ALL)){
            iImageListView.refreshContentList(all);
        }else {
            ImageFolder folder = Util.findImageFolder(folders,currentFolder);
            if (folder!=null){
                iImageListView.refreshContentList(folder.getImages());
            }
        }
        iImageListView.hideCancelButton();
        iImageListView.hideBottomMenu();
        isSelectMode = false;
    }

    public void folderClick(String folderName, int position) {
        currentFolder = folderName;
        highLightPosition = position;

        if (currentFolder.equals(ImageListView.ALL)){
            iImageListView.refreshContentList(all);
        }else {
            ImageFolder folder = Util.findImageFolder(folders,currentFolder);
            if (folder!=null){
                iImageListView.refreshContentList(folder.getImages());
            }
        }
        iImageListView.setHighLightButton(position);
        iImageListView.hideDrawer();
    }

    public void addNewFolderClick() {
        iImageListView.showDialog();
    }

    public void changeDrawerState(Boolean isDrawerOpen) {
        if (isDrawerOpen){
            iImageListView.hideDrawer();
        }else {
            if (!isSelectMode){
                iImageListView.showDrawer();
            }
        }
    }

    public void addNewFolderConfirm() {
        String name = iImageListView.getNewFolderName();
        imageUserBiz.addNewImageFolder(name, this);
        imageUserBiz.getAllImage(this);
        imageUserBiz.getImageFolders(this);
        iImageListView.refreshDrawerList(folders,highLightPosition);
        iImageListView.refreshDrawerItem(all,highLightPosition);
        if (currentFolder.equals(ImageListView.ALL)){
            iImageListView.refreshContentList(all);
        }else {
            ImageFolder folder = Util.findImageFolder(folders,currentFolder);
            if (folder!=null){
                iImageListView.refreshContentList(folder.getImages());
            }
        }
        iImageListView.hideDialog();
    }

    public void addNewFolderCancel() {
        iImageListView.hideDialog();
    }

    public void refreshDrawer() {
        imageUserBiz.getAllImage(this);
        imageUserBiz.getImageFolders(this);
        iImageListView.refreshDrawerList(folders,highLightPosition);
        iImageListView.refreshDrawerItem(all,highLightPosition);
    }

    public void refreshContent() {
        imageUserBiz.getAllImage(this);
        imageUserBiz.getImageFolders(this);
        if (currentFolder.equals(ImageListView.ALL)){
            iImageListView.refreshContentList(all);
        }else {
            ImageFolder folder = Util.findImageFolder(folders,currentFolder);
            if (folder!=null){
                iImageListView.refreshContentList(folder.getImages());
            }
        }
    }

    public void toNoteActivity() {
        iImageListView.toNoteListActivity();
    }

    public void toDetailActivity(String folderName, int position) {
        iImageListView.toDetailActivity(folderName,position);
    }

    public void fromDetailActivity() {

    }

    public void toMoveToActivity() {
        isSelectMode = false;
        iImageListView.toMoveToActivity(new ArrayList<Integer>(selected), currentFolder);
        iImageListView.hideBottomMenu();
        iImageListView.hideCancelButton();
        iImageListView.hideAllCheckSign();
        selected.clear();
    }

    public void fromMoveToActivity() {

    }

    @Override
    public void eventSuccess() {

    }

    @Override
    public void eventFail() {

    }

    @Override
    public void getImagesSuccess(List<Image> images) {
        ImageListPresenter.this.all = images;
    }

    @Override
    public void getImagesFail() {

    }

    @Override
    public void getImageFoldersSuccess(List<ImageFolder> folders) {
        ImageListPresenter.this.folders = folders;
    }

    @Override
    public void getImageFoldersFail() {

    }
}
