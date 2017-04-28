package com.slidenote.www.slidenotev2.View.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.SlideNoteApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public class ImageMoveToAdapter extends RecyclerView.Adapter {
    private List<ImageFolder> folders;
    private RecyclerViewListener.OnImageFolderClickListener onImageFolderClickListener;

    public ImageMoveToAdapter(RecyclerViewListener.OnImageFolderClickListener onImageFolderClickListener) {
        this.onImageFolderClickListener = onImageFolderClickListener;
        folders = new ArrayList<>();
    }

    public void setFolders(List<ImageFolder> folders){
        this.folders = folders;
        notifyDataSetChanged();
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ImageMoveToHolder moveToHolder = (ImageMoveToHolder) holder;
        int position = holder.getAdapterPosition();
        ImageFolder folder = folders.get(position);
        int size = folder.getImages().size();
        if (size!=0){
            Glide.with(SlideNoteApplication.getContext()).load(folder.getImages().get(0).getPath()).into(moveToHolder.itemImage);
        }else {
            Glide.with(SlideNoteApplication.getContext()).load(R.color.subColor1).into(moveToHolder.itemImage);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageMoveToHolder(LayoutInflater.from(SlideNoteApplication.getContext()).inflate(R.layout.item_image_move_to,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ImageMoveToHolder moveToHolder = (ImageMoveToHolder) holder;
        final ImageFolder folder = folders.get(position);
        int size = folder.getImages().size();
        moveToHolder.itemBadge.setText(String.valueOf(size));
        moveToHolder.itemName.setText(folder.getName());
        final int[] xy = new int[2];
        moveToHolder.item.getLocationInWindow(xy);
        moveToHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageFolderClickListener.folderClick(folder.getName(), xy);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ImageMoveToHolder extends RecyclerView.ViewHolder{

        private ImageView itemImage;
        private TextView itemName, itemBadge;
        private LinearLayout item;

        public ImageMoveToHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemBadge = (TextView) view.findViewById(R.id.itemBadge);
            item = (LinearLayout) view.findViewById(R.id.item);
        }
    }
}
