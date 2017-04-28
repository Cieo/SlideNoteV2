package camerademo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.slidenote.www.slidenotev2.Model.TessTwoUtil;
import com.slidenote.www.slidenotev2.NoteRichEditor;
import com.slidenote.www.slidenotev2.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import RichEditorPac.RichEditor;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/3/18.
 */

public class OcrActivity extends AppCompatActivity {
    ImageView Ocrimgae;
    int imagewidth, imageheight;
    Uri uri;
    int[] rectmessage;
    int textnum;
    private Uri outputMediaFileUri;
    private List<String> paths;

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private File getOutputMediaFile(String str, int number) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        Log.i(TAG, mediaStorageDir.getPath());
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i(TAG, "failed to create directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + str + "_" + number + ".jpg");

        outputMediaFileUri = Uri.fromFile(mediaFile);
        return mediaFile;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        Ocrimgae = (ImageView) findViewById(R.id.ocr_image);
        paths = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            uri = intent.getData();
            rectmessage = intent.getIntArrayExtra("rectmessage");
            textnum = rectmessage[500];
            Log.i("OCR", textnum + " ");
            Ocrimgae.setImageURI(uri);
        }
        imagewidth = Ocrimgae.getWidth();
        imageheight = Ocrimgae.getHeight();
        ImageButton OcrButton = (ImageButton) findViewById(R.id.ocr_btn_fiximge);
        final Bitmap text = decodeUriAsBitmap(uri);
        OcrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = 0;
                Log.e(TAG, "onClick: textnum"+textnum);
                for (int i = 0; i < textnum; i++) {
                    Bitmap bitmaptemp = Bitmap.createBitmap(text, rectmessage[cnt], rectmessage[cnt + 1], rectmessage[cnt + 2], rectmessage[cnt + 3]);
                    Log.i("OCR", rectmessage[cnt] + " " + rectmessage[cnt + 1] + " " + rectmessage[cnt + 2] + " " + rectmessage[cnt + 3]);
                    cnt += 4;
                    File pictureFile = getOutputMediaFile("text", i);
                    if (pictureFile == null) {
                        Log.i(TAG, "Error creating media file, check storage permissions");
                        return;
                    }
                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);//将图片压缩到流中
                        bitmaptemp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();//输出
                        bos.close();//关闭
                        paths.add(outputMediaFileUri.getPath());
                    } catch (FileNotFoundException e) {
                        Log.i(TAG, "File not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.i(TAG, "Error accessing file: " + e.getMessage());
                    }
                }
                Log.e(TAG, "onClick: " + "ocrClicked");
                NoteRichEditor.startAction(OcrActivity.this,paths);
                finish();
            }

        });

    }

}
