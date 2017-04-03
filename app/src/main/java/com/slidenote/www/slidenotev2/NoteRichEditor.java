package com.slidenote.www.slidenotev2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.slidenote.www.slidenotev2.Model.Note;
import com.slidenote.www.slidenotev2.Model.NoteFolder;
import com.slidenote.www.slidenotev2.Utils.Util;
import com.slidenote.www.slidenotev2.View.ImageListView;

import org.litepal.crud.DataSupport;

import DrawableImageView.DrawOperation;
import DrawableImageView.DrawableImageView;
import RichEditorPac.RichEditor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class NoteRichEditor extends AppCompatActivity implements View.OnTouchListener{
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int WIDTH_PAINT = 10;
    private static final int WIDTH_PEN = 5;
    private static final int WIDTH_MARK = 25;
    private static final int WIDTH_ERASE = 30;
    private Context Activity_context;

    //    private static final String ALBUM_PATH = "file:///android_asset/";
    private static final String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_test/";
    private static final String OUTPUT_JPG = "Note_editor.jpg";
    private String mPicFileName;

    // 从数据库传来的标题
    private String TITLE_STRING = "我是一个莎莎";
    private String DATE_STRING = "2017-22-22 9:22";
    private RelativeLayout ImageShow;
    private boolean hasClickedImage = false;


    private Vibrator mVibrator;
    private RichEditor mEditor;
    private DrawableImageView mDrawableImageView;
    private ImageButton mPaintButton;
    private ImageButton mPenButton;
    private ImageButton mMarkButton;
    private ImageButton mEraseButton;
    private View.OnClickListener mPaintListener;

    private Bitmap mBitmap;
    private RelativeLayout MainEditor_activity;
    private LinearLayout PicViewer_activity;
    private ImageView Pic_ImageView;

    public WebViewScreenshot webViewScreenshot;

    /************************新增*****************************************/

    private Note note;
    private NoteFolder folder;
    private boolean isChanged;

    public static void startAction(AppCompatActivity activity,String currentFolder, int position){
        Intent intent = new Intent(activity,NoteRichEditor.class);
        intent.putExtra("currentFolder",currentFolder);
        intent.putExtra("position",position);
        activity.startActivityForResult(intent,10001);
    }


    private void onActivityStart(){
        isChanged = false;
        Intent intent = getIntent();
        String currentFolder = intent.getStringExtra("currentFolder");
        int position = intent.getIntExtra("position",-1);
        if (position == -1){
            return;
        }
        if (currentFolder.equals(ImageListView.ALL)){
            note = DataSupport.findAll(Note.class,true).get(position);
        }else {
            List<NoteFolder> folders = DataSupport.findAll(NoteFolder.class,true);
            folder = Util.findNoteFolder(folders,currentFolder);
            if (folder != null){
                note = folder.getNotes().get(position);
            }
        }
    }

    private void onActivityFinish(){
        note.setTitle("your title");
        note.setContent("your content");
        note.setDate("your date");
        note.setFolder(folder);
        note.save();
        if (isChanged){
            setResult(RESULT_OK);
        }else {
            setResult(RESULT_CANCELED);
        }
    }

    /*****************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_rich_editor);
        Activity_context = getApplicationContext();

        mDrawableImageView = (DrawableImageView) findViewById(R.id.IdentifyImage);
        ImageShow = (RelativeLayout) findViewById(R.id.showImage);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        MainEditor_activity = (RelativeLayout)findViewById(R.id.mainEditor);
        PicViewer_activity = (LinearLayout)findViewById(R.id.PicViewer);
        Pic_ImageView = (ImageView) findViewById(R.id.picView);
        Pic_ImageView.setOnTouchListener(this);


        initEditor();
        initPainter();
        ShowPicActivity();
        ShowPDFActivity();
        changeModel();

        Intent intent = getIntent();
        mEditor.setDate(intent.getStringExtra("noteDate"));
        mEditor.setTitle(intent.getStringExtra("noteTitle"));
        mEditor.setEditorContents(intent.getStringExtra("noteContent"));


        findViewById(R.id.ReturnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageShow.getVisibility() == View.VISIBLE){
                    ImageShow.setVisibility(View.GONE);
                }
                else {
                    // 结束掉该页面
                    NoteRichEditor.this.finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        Intent intent = getIntent();
        intent.putExtra("noteTitle",mEditor.getTitle());
        intent.putExtra("noteContent",mEditor.getHtml());
        intent.putExtra("noteDate",getDateContents());
        setResult(RESULT_OK,intent);
        super.onPause();
    }

    /************************** 此处设置画图、富文本编辑器模式的切换 **************************/
    private void changeModel(){
        findViewById(R.id.EditorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.RichEditorToolBar).setVisibility(View.VISIBLE);
                ImageShow.setVisibility(View.GONE);
                updateImage();
                mEditor.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.HandButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.RichEditorToolBar).setVisibility(View.GONE);
                ImageShow.setVisibility(View.VISIBLE);
                mEditor.setVisibility(View.GONE);
            }
        });
    }

    /************************************** 初始化涂鸦界面 ***********************************/
    void initPainter() {
        mPaintListener  = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_paint:
                        mDrawableImageView.setPenWidth(WIDTH_PAINT);
                        mDrawableImageView.setDrawingMode(DrawOperation.DrawingMode.DRAW);
                        break;
                    case R.id.button_pen:
                        mDrawableImageView.setPenWidth(WIDTH_PEN);
                        mDrawableImageView.setDrawingMode(DrawOperation.DrawingMode.DRAW);
                        break;
                    case R.id.button_mark:
                        mDrawableImageView.setPenWidth(WIDTH_MARK);
                        mDrawableImageView.setDrawingMode(DrawOperation.DrawingMode.DRAW);
                        break;
                    case R.id.button_erase:
                        mDrawableImageView.setPenWidth(WIDTH_ERASE);
                        mDrawableImageView.setDrawingMode(DrawOperation.DrawingMode.ERASE);
                        break;
                }
                setImageButtonStatus();
            }
        };
        mPaintButton = (ImageButton) findViewById(R.id.button_paint);
        mPenButton = (ImageButton) findViewById(R.id.button_pen);
        mMarkButton = (ImageButton) findViewById(R.id.button_mark);
        mEraseButton = (ImageButton) findViewById(R.id.button_erase);
        mPaintButton.setOnClickListener(mPaintListener);
        mPenButton.setOnClickListener(mPaintListener);
        mMarkButton.setOnClickListener(mPaintListener);
        mEraseButton.setOnClickListener(mPaintListener);
        mDrawableImageView.setPenWidth(WIDTH_PAINT);
        mDrawableImageView.setPenColor(Color.RED);
        mDrawableImageView.setInEditMode(false);
        setImageButtonStatus();
        mDrawableImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mDrawableImageView.isInEditMode()) {
                    mDrawableImageView.setPenType(DrawOperation.PenType.LINE);
                    mVibrator.vibrate(40);
                }
                return true;
            }
        });
    }

    /******************************* 初始化点击涂鸦的图片状态 ********************************/
    void setImageButtonStatus() {
        mPaintButton.setImageResource(R.mipmap.btn_paint_n);
        mPenButton.setImageResource(R.mipmap.btn_pen_n);
        mMarkButton.setImageResource(R.mipmap.btn_mark_n);
        mEraseButton.setImageResource(R.mipmap.btn_erase_n);
        if (mDrawableImageView.getDrawingMode() == DrawOperation.DrawingMode.ERASE) {
            mEraseButton.setImageResource(R.mipmap.btn_erase_h);
        } else if (mDrawableImageView.getPenWidth() == WIDTH_PAINT) {
            mPaintButton.setImageResource(R.mipmap.btn_paint_h);
        } else if (mDrawableImageView.getPenWidth() == WIDTH_PEN) {
            mPenButton.setImageResource(R.mipmap.btn_pen_h);
        } else if (mDrawableImageView.getPenWidth() == WIDTH_MARK) {
            mMarkButton.setImageResource(R.mipmap.btn_mark_h);
        }
    }

    /********************************* 初始化富文本编辑器 ************************************/
    void initEditor() {
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorFontColor(Color.BLACK);

        // 从数据库传入设置标题、时间
        mEditor.setTitle(TITLE_STRING);
        mEditor.setDate(DATE_STRING);
        // 启用javascript
        mEditor.getSettings().setJavaScriptEnabled(true);
        // 添加js交互接口类，并起别名 imagelistner
        mEditor.addJavascriptInterface(this, "imageListener");
        mEditor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return hasClickedImage;
            }
        });
        // 富文本编辑操作
        bindOperation();
    }

    // 加入监听事件
    void bindOperation() {
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.WHITE : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAlbum();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "Null");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
        findViewById(R.id.ShareButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.ShareBox).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.RichEditorMasking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.ShareBox).setVisibility(View.GONE);
            }
        });

    }

    // 涂鸦图片后保存为新图片，并调用JavaScript接口更新对应的img元素。
    void updateImage() {
        if (!mDrawableImageView.isInEditMode()) {
            return;
        }
        Log.d("update image", "in");
        String url = "javascript:updateImage('%s')";
        File image = new File(
                getFilesDir(),
                String.format("paint_%s.png", System.currentTimeMillis()));
        try {
            FileOutputStream fos = new FileOutputStream(image);
            fos.write(mDrawableImageView.createCaptureBytes());
            fos.flush();
            fos.close();
            url = String.format(url, image.getPath());
            Toast.makeText(NoteRichEditor.this, "保存成功"+ String.format(url, image.getPath()), Toast.LENGTH_SHORT).show();
            mEditor.loadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDrawableImageView.clearAll();
        mDrawableImageView.setInEditMode(false);
    }

    /******************************** 处理【分享为PDF的activity】*****************************/
    private void ShowPDFActivity(){
        findViewById(R.id.ShareAsPDF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.ShareBox).setVisibility(View.GONE);
                String html = getHTMLContents();
                System.out.println("开始跳转PDF:"+ html);
                Intent intent = new Intent(NoteRichEditor.this, PdfViewer.class);
                intent.putExtra("HTMLContents", html);
                startActivity(intent);
            }
        });
    }

    /******************************* 处理【分享为图片的activity】*****************************/

    private void ShowPicActivity(){
        // 跳转至图片预览
        findViewById(R.id.ShareAsPic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.ShareBox).setVisibility(View.GONE);
                mBitmap = convertViewToBitmap(mEditor);

                if (mBitmap != null){
                    Pic_ImageView.setImageBitmap(mBitmap);
//                    Pic_ImageView.setDrawingCacheEnabled(true);
//                    Canvas canvas = new Canvas(mBitmap);
//                    Pic_ImageView.draw(canvas);
                    Log.i("---------------"+mEditor.getResources(),"图片预览成功");
                }
                MainEditor_activity.setVisibility(View.GONE);
                PicViewer_activity.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.ShareAsPic_Save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPicFileName = String.format("Note_%s.jpg", System.currentTimeMillis());
                new Thread(saveFileRunnable).start();
                Toast.makeText(NoteRichEditor.this, String.format("图片已成功保存至%s", mPicFileName), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.ShareAsPic_Button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // ShareSDK插入
            }
        });
        findViewById(R.id.ShareAsPicReturnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainEditor_activity.setVisibility(View.VISIBLE);
                PicViewer_activity.setVisibility(View.GONE);
            }
        });
    }

    /********************************** webView转图片的保存  *********************************/
//    public void ViewToJPG (View editorView){
//        editorView.setDrawingCacheEnabled(true);
//        mBitmap = Bitmap.createBitmap(editorView.getWidth(), editorView.getHeight(), Bitmap.Config.ARGB_8888);
//        Log.i("-----------------------"+editorView.getResources(),"已成功保存");
//    }
    public Bitmap convertViewToBitmap(WebView view) {
        /********************  短截屏  ********************/
//        view.destroyDrawingCache();
//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.setDrawingCacheEnabled(true);
//        return view.getDrawingCache(true);
        /********************  短截屏END  ********************/

        Bitmap bitmap = WebViewScreenshot.getWebViewBitmap(Activity_context, view);
        return bitmap;
    }
    public void saveFile (Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH+fileName);
        if (!myCaptureFile.exists()){
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }
    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try{
                saveFile(mBitmap, mPicFileName);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    };

    // 打开系统相册
    private void OpenAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        Log.i("--------跳转", "开始跳转");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            Uri SelectedImage = data.getData();
            String imageUrl = SelectedImage.toString();
            Log.e("-----图片路径", imageUrl);
            String imageAlt = "Empty Image!";
            mEditor.insertImage(imageUrl, imageAlt);
        }
    }

    /********************************   点击图片进行识别跳转   **************************/
    @android.webkit.JavascriptInterface
    public void actionFromJsWithParam(final String image_url_string){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                String assertPrefix = "file:///android_asset/";
                String uriPrefix = "content://";
                String path = Uri.decode(image_url_string);

                InputStream is= null;
                Bitmap img = null;
                if (path.startsWith(uriPrefix)) {
                    mDrawableImageView.setImageURI(Uri.parse(path));
                } else if (path.startsWith(assertPrefix)) {
                    path = path.substring(assertPrefix.length());
                    try {
                        is = getAssets().open(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    img = BitmapFactory.decodeStream(is, null, options);
                    mDrawableImageView.setImageBitmap(img);
                } else {
                    img = BitmapFactory.decodeFile(path.substring(7));
                    mDrawableImageView.setImageBitmap(img);
                }
                Log.d("path", path);
                if (img == null) {
                    Log.d("open image", "null "+path);
                }
                mDrawableImageView.setInEditMode(true);
                findViewById(R.id.showImage).setVisibility(View.VISIBLE);
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void clickImage() {
        hasClickedImage = true;
    }

    @android.webkit.JavascriptInterface
    public void endClickImage() {
        hasClickedImage = false;
    }

    private String HTMLContents;
    private String TitleContents;
    private String DateContents;
    private String EditorContents;

    @android.webkit.JavascriptInterface
    public void setHTMLContents(final String str){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        HTMLContents = str;
        System.out.println("网页内容js：" + str);
//            }
//        });
    }

    @android.webkit.JavascriptInterface
    public void setTitleContents(final String str){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        TitleContents = str;
        System.out.println("网页标题js："+str);
//            }
//        });
    }

    @android.webkit.JavascriptInterface
    public void setDateContents(final String str){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        DateContents = str;
        System.out.println("网页时间js："+str);
//            }
//        });
    }

    @android.webkit.JavascriptInterface
    public void setEditorContents(final String str){
        EditorContents = str;
    }

    /******************** 接口函数 ******************/
    public String getEditorContents(){
        mEditor.load("javascript:RE.getEditorContents();");
        return EditorContents;

    }
    public String getHTMLContents(){
        mEditor.loadUrl("javascript:RE.getHTMLContents();");
        HTMLContents = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\">\n" + HTMLContents;
        return HTMLContents;
    }

    public String getDateContents(){
        mEditor.loadUrl("javascript:RE.getDateContents();");
        return DateContents;
    }

    public String getTitleContents(){
        mEditor.loadUrl("javascript:RE.getTitleContents();");
        return TitleContents;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
