package com.slidenote.www.slidenotev2.View.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.SlideNoteApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/30/2017.
 */

public class ImageDetailAdapter extends PagerAdapter {
    private List<Image> images;
    private int deleted;

    public ImageDetailAdapter() {
        images = new ArrayList<>();
        deleted = -1;
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public Image getCurrentImage(int position){
        return images.get(position);
    }

    public void deleteImage(int position){
        images.remove(position);
        deleted = position;
        notifyDataSetChanged();
        deleted = -1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(SlideNoteApplication.getContext()).inflate(R.layout.item_image_detail,container,false);
        PhotoView image = (PhotoView) view.findViewById(R.id.image);
        view.setTag(position);
        Glide.with(SlideNoteApplication.getContext()).load(images.get(position).getPath()).into(image);
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        int position = (int) ((View) object).getTag();
        if (position == deleted) {
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
