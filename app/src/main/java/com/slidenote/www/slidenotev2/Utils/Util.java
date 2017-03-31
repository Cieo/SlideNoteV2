package com.slidenote.www.slidenotev2.Utils;

import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.NoteFolder;

import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public class Util {
    public static ImageFolder findImageFolder(List<ImageFolder> folders, String name){
        for (ImageFolder folder : folders){
            if (folder.getName().equals(name)){
                return folder;
            }
        }
        return null;
    }

    public static NoteFolder findNoteFolder(List<NoteFolder> folders, String name){
        for (NoteFolder folder : folders){
            if (folder.getName().equals(name)){
                return folder;
            }
        }
        return null;
    }
}
