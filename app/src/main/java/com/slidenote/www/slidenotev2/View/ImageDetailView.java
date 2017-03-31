package com.slidenote.www.slidenotev2.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Presenter.ImageDetailPresenter;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.View.Adapter.ImageDetailAdapter;

import java.util.List;

/**
 * Created by Cieo233 on 3/30/2017.
 */

public class ImageDetailView extends AppCompatActivity implements IImageDetailView {

    private ImageView back, share;

    private ViewPager content;
    private ImageDetailAdapter imageDetailAdapter;

    private ImageView ocr, newNote, edit, delete;

    private ImageDetailPresenter presenter;

    public static void actionStart(AppCompatActivity activity, String currentFolder, int position){
        Intent intent = new Intent(activity,ImageDetailView.class);
        intent.putExtra("currentFolder", currentFolder);
        intent.putExtra("position",position);
        activity.startActivityForResult(intent,1001);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        presenter = new ImageDetailPresenter(this);
        initView();
        initEvent();
        presenter.onActivityStart(getIntent());
    }

    void initView(){
        back = (ImageView) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);

        content = (ViewPager) findViewById(R.id.content);
        imageDetailAdapter = new ImageDetailAdapter();
        content.setAdapter(imageDetailAdapter);

        ocr = (ImageView) findViewById(R.id.ocr);
        newNote = (ImageView) findViewById(R.id.newNote);
        edit = (ImageView) findViewById(R.id.edit);
        delete = (ImageView) findViewById(R.id.delete);
    }

    void initEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.backClick();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.shareClick();
            }
        });
        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.ocrClick();
            }
        });
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.newNoteClick();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editClick();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteClick(imageDetailAdapter.getCurrentImage(content.getCurrentItem()));
            }
        });
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void toOCR() {

    }

    @Override
    public void toNewNote() {

    }

    @Override
    public void toEdit() {

    }

    @Override
    public void backToMainList(Boolean isDeleted) {
        if (isDeleted){
            setResult(RESULT_OK);
        }else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void deleteImage() {
        imageDetailAdapter.deleteImage(content.getCurrentItem());
    }

    @Override
    public void refreshContent(List<Image> images, int position) {
        imageDetailAdapter.setImages(images);
        content.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        presenter.backClick();
    }
}
