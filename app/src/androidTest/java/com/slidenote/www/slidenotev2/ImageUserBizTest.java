package com.slidenote.www.slidenotev2;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ImageUserBizTest {
    private static final String TAG = "ImageUserBizTest";
    private ImageUserBiz imageUserBiz;

    @Before
    public void createUserBiz(){
        imageUserBiz = new ImageUserBiz();
    }

    @Test
    public void testScanImage(){
        imageUserBiz.scanImage(new BaseListener.OnScanImageListener() {
            @Override
            public void scanImageSuccess(List<ImageFolder> folders) {

            }

            @Override
            public void scanImageFail() {

            }
        });
        imageUserBiz.addNewImageFolder("album3", new BaseListener.OnAddNewImageFolderListener() {
            @Override
            public void addNewImageFolderSuccess(List<ImageFolder> folders) {

            }

            @Override
            public void addNewImageFolderFail() {

            }
        });
        List<ImageFolder> folders = DataSupport.findAll(ImageFolder.class,true);
        imageUserBiz.moveImage(Util.findImageFolder(folders,"album1").getImages(), Util.findImageFolder(folders,"album2"), new BaseListener.OnMoveImageListener() {
            @Override
            public void moveImageSuccess(List<ImageFolder> folders) {

            }

            @Override
            public void moveImageFail() {

            }
        });
        imageUserBiz.deleteImage(Util.findImageFolder(folders, "album2").getImages(), new BaseListener.OnDeleteImageListener() {
            @Override
            public void deleteImageSuccess(List<ImageFolder> folders) {

            }

            @Override
            public void deleteImageFail() {

            }
        });
        folders = DataSupport.findAll(ImageFolder.class,true);
        for (ImageFolder folder : folders){
            Log.e(TAG, "testScanImage: "+folder.getName() );
            for (Image image : folder.getImages()){
                Log.e(TAG, "testScanImage: "+image.getName() );
            }
            Log.e(TAG, "testScanImage: "+"-----------------" );
        }
        Log.e(TAG, "testScanImage: "+"TEstSkip" );
    }

}
