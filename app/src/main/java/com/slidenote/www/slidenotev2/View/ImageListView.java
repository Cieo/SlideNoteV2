package com.slidenote.www.slidenotev2.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Presenter.ImageListPresenter;
import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.View.Adapter.ImageContentAdapter;
import com.slidenote.www.slidenotev2.View.Adapter.ImageDrawerAdapter;
import com.slidenote.www.slidenotev2.View.Adapter.RecyclerViewListener;

import java.util.List;

import camerademo.OcrActivity;

/**
 * Created by Cieo233 on 3/28/2017.
 */

public class ImageListView extends AppCompatActivity implements IImageListView, RecyclerViewListener.OnImageClickListener, RecyclerViewListener.OnDrawerClickListener {

    private ImageView toolbarMenu;
    private TextView toolbarBtn;

    private RecyclerView content;
    private ImageContentAdapter imageContentAdapter;

    private RelativeLayout bottomMenu;
    private ImageView bottomMenuShare, bottomMenuDelete;
    private TextView bottomMenuMoveTo;

    private DrawerLayout drawerLayout;

    private LinearLayout drawerSwitch;
    private TextView drawerSwitchText;

    private LinearLayout drawerAddNewFolder;
    private TextView drawerAddNewFolderText;

    private RelativeLayout drawerItem;
    private TextView drawerItemText, drawerItemBadge;

    private RecyclerView drawer;
    private ImageDrawerAdapter imageDrawerAdapter;

    private Dialog newFolderDialog;
    private TextView newFolderConfirm, newFolderCancel, newFolderTitle, newFolderHint;
    private EditText newFolderName;

    private FloatingActionButton toPhoto;

    private ImageListPresenter presenter;

    public static final String ALL = "AllSpecialMark_______________";

    public static void startAction(AppCompatActivity activity){
        Intent intent = new Intent(activity, ImageListView.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        presenter = new ImageListPresenter(this);
        initView();
        initEvent();
    }

    private void initView() {
        toolbarMenu = (ImageView) findViewById(R.id.toolbarMenu);
        toolbarBtn = (TextView) findViewById(R.id.toolbarBtn);
        toolbarBtn.setText("选择");

        content = (RecyclerView) findViewById(R.id.content);
        content.setLayoutManager(new GridLayoutManager(this, 3));
        imageContentAdapter = new ImageContentAdapter(this);
        content.setAdapter(imageContentAdapter);

        bottomMenu = (RelativeLayout) findViewById(R.id.bottomMenu);
        bottomMenu.setVisibility(View.GONE);
        bottomMenuShare = (ImageView) findViewById(R.id.bottomMenuShare);
        bottomMenuDelete = (ImageView) findViewById(R.id.bottomMenuDelete);
        bottomMenuMoveTo = (TextView) findViewById(R.id.bottomMenuMoveTo);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerSwitch = (LinearLayout) findViewById(R.id.drawerSwitch);
        drawerSwitchText = (TextView) findViewById(R.id.drawerSwitchText);
        drawerSwitchText.setText("所有笔记");

        drawerAddNewFolder = (LinearLayout) findViewById(R.id.drawerAddFolder);
        drawerAddNewFolderText = (TextView) findViewById(R.id.drawerAddFolderText);
        drawerAddNewFolderText.setText("添加新的相簿");

        drawerItem = (RelativeLayout) findViewById(R.id.drawerItem);
        drawerItemText = (TextView) findViewById(R.id.drawerItemText);
        drawerItemBadge = (TextView) findViewById(R.id.drawerItemBadge);
        drawerItemText.setText("所有幻灯片");

        drawer = (RecyclerView) findViewById(R.id.drawer);
        drawer.setLayoutManager(new LinearLayoutManager(this));
        imageDrawerAdapter = new ImageDrawerAdapter(this);
        drawer.setAdapter(imageDrawerAdapter);

        toPhoto = (FloatingActionButton) findViewById(R.id.toPhoto);

        presenter.refreshDrawer();
        presenter.refreshContent();
    }

    private void initEvent() {
        toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeDrawerState(drawerLayout.isDrawerOpen(GravityCompat.START));
            }
        });
        toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeToolbarBtnState(toolbarBtn.getText().toString());
            }
        });
        bottomMenuShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 3/29/2017
            }
        });
        bottomMenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteSelected();
            }
        });
        bottomMenuMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.toMoveToActivity();
            }
        });
        drawerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.toNoteActivity();
            }
        });
        drawerAddNewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNewFolderClick();
            }
        });
        drawerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.folderClick(ALL, -1);
            }
        });
        toPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageListView.this, OcrActivity.class);
                ImageListView.this.startActivity(intent);
            }
        });
    }

    @Override
    public void showBottomMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
        toPhoto.setVisibility(View.GONE);
    }

    @Override
    public void hideBottomMenu() {
        bottomMenu.setVisibility(View.GONE);
        toPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshDrawerList(List<ImageFolder> folders, int highLightPosition) {
        imageDrawerAdapter.setFolders(folders);
        imageDrawerAdapter.setHighLightPosition(highLightPosition);
    }

    @Override
    public void refreshDrawerItem(List<Image> images, int highLightPosition) {
        drawerItemBadge.setText(String.valueOf(images.size()));
        if (highLightPosition == -1) {
            drawerItem.setBackgroundResource(R.drawable.drawer_item_yellow);
        } else {
            drawerItem.setBackgroundResource(R.drawable.drawer_item_white);
        }
    }

    @Override
    public void refreshContentList(List<Image> images) {
        imageContentAdapter.setImages(images);
    }

    @Override
    public void hideDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void showCancelButton() {
        toolbarBtn.setText("取消");
    }

    @Override
    public void hideCancelButton() {
        toolbarBtn.setText("选择");
    }

    @Override
    public void toMoveToActivity(List<Integer> selected, String currentFolder) {
        ImageMoveToView.startAction(this,selected,currentFolder);
    }

    @Override
    public void toDetailActivity(String folderName, int position) {
        ImageDetailView.actionStart(this,folderName,position);
    }

    @Override
    public void setHighLightButton(int position) {
        imageDrawerAdapter.setHighLightPosition(position);
        if (position == -1) {
            drawerItem.setBackgroundResource(R.drawable.drawer_item_yellow);
        } else {
            drawerItem.setBackgroundResource(R.drawable.drawer_item_white);
        }
    }

    @Override
    public void toNoteListActivity() {
        NoteListView.startAction(this);
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
        newFolderTitle = (TextView) newFolderDialog.findViewById(R.id.newFolderTitle);
        newFolderHint = (TextView) newFolderDialog.findViewById(R.id.newFolderHint);
        newFolderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNewFolderConfirm();
            }
        });
        newFolderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addNewFolderCancel();
            }
        });
        newFolderTitle.setText("新建相册");
        newFolderHint.setText("请输入相册名称");
        newFolderDialog.show();
    }

    @Override
    public void hideDialog() {
        newFolderDialog.cancel();
    }

    @Override
    public String getNewFolderName() {
        return newFolderName.getText().toString();
    }

    @Override
    public void showHideSingleCheckSign(int position) {
        imageContentAdapter.changeItemState(position);
    }

    @Override
    public void hideAllCheckSign() {
        imageContentAdapter.clearItemState();
    }

    @Override
    public void imageClick(Image image, int position) {
        presenter.itemClick(image, position);
    }

    @Override
    public void drawerClick(String folderName, int position) {
        presenter.folderClick(folderName, position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 101:
                if (resultCode == RESULT_OK){
                    presenter.refreshDrawer();
                    presenter.refreshContent();
                }
                break;
            case 1001:
                if (resultCode == RESULT_OK){
                    presenter.refreshDrawer();
                    presenter.refreshContent();
                }
                break;
        }
    }
}
