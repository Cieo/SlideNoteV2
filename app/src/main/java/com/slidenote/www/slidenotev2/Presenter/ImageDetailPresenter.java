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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Cieo233 on 3/30/2017.
 */

public class ImageDetailPresenter implements BaseListener.OnGetImagesListener, BaseListener.OnGetImageFoldersListener, BaseListener.OnEventListener {
    private ImageUserBiz imageUserBiz = new ImageUserBiz();
    private IImageDetailView iImageDetailView;
    private Boolean isDeleted;
    private List<Image> deleted;
    private List<Image> showingImages = new ArrayList<>();
    private String currentFolder;
    int position;

    public ImageDetailPresenter(IImageDetailView iImageDetailView) {
        this.iImageDetailView = iImageDetailView;
        isDeleted = false;
        deleted = new ArrayList<>();
    }

    public void onActivityStart(Intent intent){
        currentFolder = intent.getStringExtra("currentFolder");
        position = intent.getIntExtra("position",0);

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                if (currentFolder.equals(ImageListView.ALL)){
                    imageUserBiz.getAllImage(ImageDetailPresenter.this);
                }else {
                    imageUserBiz.getImageFolders(ImageDetailPresenter.this);
                }
                e.onNext(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean){
                    iImageDetailView.refreshContent(showingImages, position);
                }
            }
        });

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
            imageUserBiz.deleteImage(deleted, this);
        }
        iImageDetailView.backToMainList(isDeleted);
    }

    public void deleteClick(Image image){
        deleted.add(image);
        iImageDetailView.deleteImage();
    }

    @Override
    public void getImagesSuccess(List<Image> images) {
        showingImages.addAll(images);
    }

    @Override
    public void getImagesFail() {

    }

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

    @Override
    public void eventSuccess() {

    }

    @Override
    public void eventFail() {

    }
}
