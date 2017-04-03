package com.slidenote.www.slidenotev2.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.slidenote.www.slidenotev2.Presenter.ImagePreviewPresenter;
import com.slidenote.www.slidenotev2.R;

/**
 * Created by Cieo233 on 4/2/2017.
 */

public class ImagePreviewView extends AppCompatActivity implements IImagePreviewView {

    private TextView complete;
    private PhotoView preview;
    private ImageView oneMore, delete, ocr;
    private ImagePreviewPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_image_preview);
        presenter = new ImagePreviewPresenter(this);
        initView();
        initEvent();
        presenter.onActivityStart(getIntent());
    }

    void initView(){
        complete = (TextView) findViewById(R.id.complete);
        preview = (PhotoView) findViewById(R.id.preview);
        oneMore = (ImageView) findViewById(R.id.oneMore);
        delete = (ImageView) findViewById(R.id.delete);
        ocr = (ImageView) findViewById(R.id.ocr);
    }

    void initEvent(){
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCompleteClick();
            }
        });
        oneMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onOneMoreClick();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDeleteClick();
            }
        });
        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onOCRClick();
            }
        });
    }


    @Override
    public void toOCRActivity(String path) {

    }

    @Override
    public void toImageDetailActivity(String currentFolder, int position) {
        ImageDetailView.actionStart(this,currentFolder,position);
    }

    @Override
    public void backToPhotoActivity() {
        finish();
    }

    @Override
    public void loadImage(String path) {
        Glide.with(this).load(path).into(preview);
    }
}
