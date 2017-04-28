package com.slidenote.www.slidenotev2.View.Adapter;

import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.Model.NoteFolder;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public interface RecyclerViewListener {
    interface OnDrawerClickListener{
        void drawerClick(String folderName, int position);
    }
    interface OnImageClickListener {
        void imageClick(Image image, int position);
    }
    interface OnImageFolderClickListener {
        void folderClick(String folderName, int[] xy);
    }

    interface OnNoteClickListener {
        void noteClick(Note note, int position);
    }
    interface OnNoteFolderClickListener {
        void folderClick(String folderName, int[] xy);
    }

}
