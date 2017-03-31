package com.slidenote.www.slidenotev2.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.Model.NoteFolder;
import com.slidenote.www.slidenotev2.Presenter.ImageMoveToPresenter;
import com.slidenote.www.slidenotev2.Presenter.NoteMoveToPresenter;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.SlideNoteApplication;
import com.slidenote.www.slidenotev2.View.Adapter.ImageMoveToAdapter;
import com.slidenote.www.slidenotev2.View.Adapter.NoteMoveToAdapter;
import com.slidenote.www.slidenotev2.View.Adapter.RecyclerViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cieo233 on 3/29/2017.
 */

public class NoteMoveToView extends AppCompatActivity implements INoteMoveToView, RecyclerViewListener.OnNoteFolderClickListener {
    private ImageView backBtn;

    private RelativeLayout imageContainer;
    private ImageView image;

    private LinearLayout addFolder;
    private TextView addFolderText;

    private RecyclerView content;
    private NoteMoveToAdapter noteMoveToAdapter;

    private Dialog newFolderDialog;
    private EditText newFolderName;
    private TextView newFolderConfirm;
    private TextView newFolderCancel;


    private NoteMoveToPresenter presenter;

    public static void startAction(AppCompatActivity activity, List<Integer> selected, String currentFolder){
        Intent intent = new Intent(activity, NoteMoveToView.class);
        intent.putIntegerArrayListExtra("selected", (ArrayList<Integer>) selected);
        intent.putExtra("currentFolder",currentFolder);
        activity.startActivityForResult(intent,101);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to);
        presenter = new NoteMoveToPresenter(this);
        initView();
        initEvent();
        presenter.onActivityStart(getIntent());
    }

    void initView(){
        backBtn = (ImageView) findViewById(R.id.backBtn);

        imageContainer = (RelativeLayout) findViewById(R.id.imageContainer);
        image = (ImageView) findViewById(R.id.image);
        imageContainer.setVisibility(View.VISIBLE);

        addFolder = (LinearLayout) findViewById(R.id.addFolder);
        addFolderText = (TextView) findViewById(R.id.addFolderText);
        addFolderText.setText("新建相册");

        content = (RecyclerView) findViewById(R.id.content);
        noteMoveToAdapter = new NoteMoveToAdapter(this);
        content.setLayoutManager(new LinearLayoutManager(this));
        content.setAdapter(noteMoveToAdapter);
    }

    void initEvent(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.backToMainListActivity();
            }
        });
        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addFolderClick();
            }
        });
    }

    @Override
    public String getNewFolderName() {
        return newFolderName.getText().toString();
    }

    @Override
    public void refreshContent(List<NoteFolder> folders) {
        noteMoveToAdapter.setFolders(folders);
    }

    @Override
    public void showDialog() {
        newFolderDialog = new Dialog(this);
        newFolderDialog.setCancelable(true);
        newFolderDialog.setContentView(R.layout.dialog_create_new_folder);
        newFolderDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newFolderDialog.getWindow().setDimAmount(0.4f);
        newFolderName = (EditText) newFolderDialog.findViewById(R.id.newFolderName);
        newFolderConfirm = (TextView) newFolderDialog.findViewById(R.id.newFolderConfirm);
        newFolderCancel = (TextView) newFolderDialog.findViewById(R.id.newFolderCancel);
        newFolderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addFolderConfirm();
            }
        });
        newFolderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addFolderCancel();
            }
        });
        newFolderDialog.show();
    }

    @Override
    public void hideDialog() {
        newFolderDialog.cancel();
    }

    @Override
    public void showAnimation() {

    }

    @Override
    public void backToMainListActivity(Boolean isMoved) {
        if (isMoved){
            setResult(RESULT_OK);
        }else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void setNote(Note note) {
    }

    @Override
    public void showDecorate() {

    }

    @Override
    public void hideDecorate() {

    }

    @Override
    public void folderClick(NoteFolder folder, int[] xy) {
        presenter.itemClick(folder,xy);
    }

    @Override
    public void onBackPressed() {
        presenter.backToMainListActivity();
    }
}
