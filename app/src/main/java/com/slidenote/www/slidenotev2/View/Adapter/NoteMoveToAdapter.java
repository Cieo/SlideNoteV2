package com.slidenote.www.slidenotev2.View.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.NoteFolder;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.SlideNoteApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public class NoteMoveToAdapter extends RecyclerView.Adapter {
    private List<NoteFolder> folders;
    private RecyclerViewListener.OnNoteFolderClickListener onNoteFolderClickListener;

    public NoteMoveToAdapter(RecyclerViewListener.OnNoteFolderClickListener onNoteFolderClickListener) {
        this.onNoteFolderClickListener = onNoteFolderClickListener;
        folders = new ArrayList<>();
    }

    public void setFolders(List<NoteFolder> folders){
        this.folders = folders;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageMoveToHolder(LayoutInflater.from(SlideNoteApplication.getContext()).inflate(R.layout.item_drawer_main_list,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ImageMoveToHolder moveToHolder = (ImageMoveToHolder) holder;
        final NoteFolder folder = folders.get(position);
        int size = folder.getNotes().size();
        moveToHolder.itemBadge.setText(String.valueOf(size));
        moveToHolder.itemName.setText(folder.getName());
        final int[] xy = new int[2];
        moveToHolder.item.getLocationInWindow(xy);
        moveToHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNoteFolderClickListener.folderClick(folder.getName(), xy);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ImageMoveToHolder extends RecyclerView.ViewHolder{

        private TextView itemName, itemBadge;
        private RelativeLayout item;

        public ImageMoveToHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.drawerItemText);
            itemBadge = (TextView) view.findViewById(R.id.drawerItemBadge);
            item = (RelativeLayout) view.findViewById(R.id.drawerItem);
        }
    }
}
