package com.slidenote.www.slidenotev2.Model;

import java.util.List;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class BaseListener {
   public interface OnGetImagesListener {
       void getImagesSuccess(List<Image> images);
       void getImagesFail();
   }

   public interface OnGetImageFoldersListener {
       void getImageFoldersSuccess(List<ImageFolder> folders);
       void getImageFoldersFail();
   }

   public interface OnGetNotesListener {
       void getNotesSuccess(List<Note> notes);
       void getNotesFail();
   }

   public interface OnGetNoteFoldersListener {
       void getNoteFoldersSuccess(List<NoteFolder> folders);
       void getNoteFoldersFail();
   }

   public interface OnEventListener{
       void eventSuccess();
       void eventFail();
   }

}
