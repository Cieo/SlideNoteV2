package com.slidenote.www.slidenotev2.Model;

import android.os.Environment;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class ImageUserBiz implements IImageUserBiz {

    private static final String TAG = "ImageUserBiz";
    private static final File IMAGE_ROOT = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SlideNoteImage/");


    @Override
    public void getAllImage(BaseListener.OnGetImagesListener onGetImagesListener) {
        List<Image> images = DataSupport.findAll(Image.class, true);
        onGetImagesListener.getImagesSuccess(images);
    }

    @Override
    public void getImageFolders(BaseListener.OnGetImageFoldersListener onGetImageFoldersListener) {
        List<ImageFolder> folders = DataSupport.findAll(ImageFolder.class,true);
        onGetImageFoldersListener.getImageFoldersSuccess(folders);
    }

    @Override
    public void storeNewImage(Image image, String folderName, BaseListener.OnEventListener onEventListener) {
        ImageFolder folder = DataSupport.where("name=?",folderName).findFirst(ImageFolder.class,true);
        image.setFolder(folder);
        image.save();
        onEventListener.eventSuccess();
    }

    @Override
    public void moveImage(List<Image> images, String folderName, BaseListener.OnEventListener onEventListener) {
        ImageFolder folder = DataSupport.where("name=?",folderName).findFirst(ImageFolder.class, true);
        for (Image image : images){
            File src = new File(image.getPath());
            File dst = new File(folder.getPath()+"/"+image.getName());
            if (dst.exists()){
                if (src.getAbsolutePath().equals(dst.getAbsolutePath())){
                    continue;
                } else {
                    dst.delete();
                }
            }
            try {
                copy(src,dst);
                src.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setPath(dst.getAbsolutePath());
            image.setFolder(folder);
            image.save();
        }
        onEventListener.eventSuccess();
    }

    private void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    @Override
    public void deleteImage(List<Image> images, BaseListener.OnEventListener onEventListener) {
        for (Image image : images) {
            image.delete();
            File file = new File(image.getPath());
            file.delete();
        }
        onEventListener.eventSuccess();
    }

    @Override
    public void addNewImageFolder(String name, BaseListener.OnEventListener onEventListener) {
        File newFolder = new File(IMAGE_ROOT.getAbsolutePath()+"/"+name);
        if (!newFolder.exists()){
            newFolder.mkdirs();
        } else {
            onEventListener.eventFail();
            return;
        }
        ImageFolder folder = new ImageFolder.Builder(name,newFolder.getAbsolutePath()).build();
        folder.save();
        onEventListener.eventSuccess();
    }


    @Override
    public void scanImage(BaseListener.OnEventListener onEventListener) {
        if (!IMAGE_ROOT.exists()) {
            IMAGE_ROOT.mkdirs();
        }
        scan(IMAGE_ROOT);
        onEventListener.eventSuccess();
    }

    private void scan(File imageRoot) {
        DataSupport.deleteAll(Image.class);
        DataSupport.deleteAll(ImageFolder.class);

        for (File file : imageRoot.listFiles()) {
            if (file.isDirectory()) {
                ImageFolder folder = new ImageFolder.Builder(file.getName(),file.getAbsolutePath()).build();
                folder.save();
                scanChild(file, folder);
            } else if (file.isFile()) {
                Image image = new Image.Builder(file.getName(),file.getAbsolutePath()).build();
                image.save();
            }
        }
    }

    private void scanChild(File imageRoot, ImageFolder folder) {
        for (File file : imageRoot.listFiles()) {
            if (file.isFile()) {
                Image image = new Image.Builder(file.getName(),file.getAbsolutePath()).folder(folder).build();
                image.save();
            }
        }
    }
}
