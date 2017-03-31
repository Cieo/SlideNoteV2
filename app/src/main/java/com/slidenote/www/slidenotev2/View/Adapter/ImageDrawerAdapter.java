package com.slidenote.www.slidenotev2.View.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.SlideNoteApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public class ImageDrawerAdapter extends RecyclerView.Adapter {

    private List<ImageFolder> folders;
    private RecyclerViewListener.OnDrawerClickListener onDrawerClickListener;
    private int highLightPosition;

    public ImageDrawerAdapter(RecyclerViewListener.OnDrawerClickListener onDrawerClickListener) {
        this.folders = new ArrayList<>();
        this.onDrawerClickListener = onDrawerClickListener;
        this.highLightPosition = -1;
    }

    public void setFolders(List<ImageFolder> folders) {
        this.folders = folders;
        notifyDataSetChanged();
    }

    public void setHighLightPosition(int highLightPosition) {
        if (this.highLightPosition != -1) {
            int oldHighLightPosition = this.highLightPosition;
            this.highLightPosition = -1;
            notifyItemChanged(oldHighLightPosition);
        }
        if (highLightPosition != -1) {
            this.highLightPosition = highLightPosition;
            notifyItemChanged(this.highLightPosition);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageDrawerHolder(LayoutInflater.from(SlideNoteApplication.getContext()).inflate(R.layout.item_drawer_main_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImageDrawerHolder drawerHolder = (ImageDrawerHolder) holder;
        final ImageFolder folder = folders.get(position);
        drawerHolder.itemBadge.setText(String.valueOf(folder.getImages().size()));
        drawerHolder.itemText.setText(folder.getName());
        drawerHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDrawerClickListener.drawerClick(folder.getName(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ImageDrawerHolder drawerHolder = (ImageDrawerHolder) holder;
        if (drawerHolder.getAdapterPosition() == highLightPosition) {
            drawerHolder.item.setBackgroundResource(R.color.mainColor1);
        } else {
            drawerHolder.item.setBackgroundResource(R.color.subColor1);
        }
    }

    public class ImageDrawerHolder extends RecyclerView.ViewHolder {
        private TextView itemText, itemBadge;
        private RelativeLayout item;

        public ImageDrawerHolder(View view) {
            super(view);
            itemText = (TextView) view.findViewById(R.id.drawerItemText);
            itemBadge = (TextView) view.findViewById(R.id.drawerItemBadge);
            item = (RelativeLayout) view.findViewById(R.id.drawerItem);
        }
    }
}
