package com.slidenote.www.slidenotev2.View.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.SlideNoteApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public class ImageContentAdapter extends RecyclerView.Adapter {
    private List<Image> images;
    private RecyclerViewListener.OnImageClickListener onImageClickListener;
    private List<Integer> selected;

    public ImageContentAdapter(RecyclerViewListener.OnImageClickListener onImageClickListener) {
        this.images = new ArrayList<>();
        this.onImageClickListener = onImageClickListener;
        selected = new ArrayList<>();
    }

    public void setImages(List<Image> images) {
        this.images = images;
        selected.clear();
        notifyDataSetChanged();
    }

    public void changeItemState(int position) {
        if (selected.contains(position)) {
            selected.remove(Integer.valueOf(position));
        } else {
            selected.add(position);
        }
        notifyItemChanged(position);
    }

    public void clearItemState() {
        List<Integer> oldSelected = new ArrayList<>(selected);
        selected.clear();
        for (int position : oldSelected) {
            notifyItemChanged(position);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ImageItemHolder itemHolder = (ImageItemHolder) holder;
        if (selected.contains(holder.getAdapterPosition())) {
            itemHolder.checkSign.setVisibility(View.VISIBLE);
        } else {
            itemHolder.checkSign.setVisibility(View.GONE);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageItemHolder(LayoutInflater.from(SlideNoteApplication.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImageItemHolder itemHolder = (ImageItemHolder) holder;
        final Image image = images.get(position);
        Glide.with(SlideNoteApplication.getContext()).load(image.getPath()).into(itemHolder.image);
        itemHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClickListener.imageClick(image, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageItemHolder extends RecyclerView.ViewHolder {

        private ImageView image, checkSign;

        public ImageItemHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            checkSign = (ImageView) view.findViewById(R.id.checkSign);
        }
    }
}
