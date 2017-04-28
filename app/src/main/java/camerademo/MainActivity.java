package camerademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.slidenote.www.slidenotev2.R;
import com.slidenote.www.slidenotev2.View.ImageListView;
import com.slidenote.www.slidenotev2.View.NoteListView;

import java.io.ByteArrayOutputStream;

/**
 * Created by cky on 2017/2/11.
 */
public class MainActivity extends AppCompatActivity implements Camera.PreviewCallback {
    private CameraPreview mPreview;
    private DrawView mDrawview;
    private Button toMediaBtn, toNoteBtn;
    static Camera.Size Imagesize;
    static Camera.Size Previewsize;
    private int LayoutWidth, LayoutHeight, PreviewWidth, PreviewHeight, ImageWidth, ImageHeight;
    int[] result = {0, 0, 0, 0, 0, 0, 0, 0};
    PptTask mPptTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Btn_TakePicture = (Button) findViewById(R.id.Btn_TakePicture);
        Btn_TakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageWidth = Imagesize.width;
                ImageHeight = Imagesize.height;
                Log.i("debug1", ImageWidth + " " + ImageHeight);
                int[] resulttoimage = {0, 0, 0, 0, 0, 0, 0, 0};
                for (int i = 0; i < 4; ++i) {
                    Log.i("kaychentran", result[2 * i] + " " + result[2 * i + 1]);
                    resulttoimage[2 * i] = (int) (result[2 * i] * ((float) ImageWidth / (float) PreviewWidth));
                    resulttoimage[2 * i + 1] = (int) (result[2 * i + 1] * ((float) ImageHeight / (float) PreviewHeight));
                    Log.i("kaychentran", resulttoimage[2 * i] + " " + resulttoimage[2 * i + 1]);
                    Log.i("kaychentran", "--------------------------------------");
                }
                mPreview.takePicture(resulttoimage);
            }
        });
        final LinearLayout Preview = (LinearLayout) findViewById(R.id.Preview);
        ViewTreeObserver vto = Preview.getViewTreeObserver(); //获取屏幕预览宽度与高度
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                LayoutWidth = Preview.getMeasuredHeight();
                LayoutHeight = Preview.getMeasuredWidth();
                //  Log.i("debug1",LayoutWidth + " " + LayoutHeight);
                return true;
            }
        });
        toMediaBtn = (Button) findViewById(R.id.Btn_MediaPreview);
        toMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageListView.startAction(MainActivity.this);
            }
        });
        toNoteBtn = (Button) findViewById(R.id.Btn_Note);
        toNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteListView.startAction(MainActivity.this);
            }
        });
    }

    private void InitPreview() { //初始化预览
        mPreview = (CameraPreview) findViewById(R.id.CameraPreview);
        mDrawview = (DrawView) findViewById(R.id.DrawView);
        mDrawview.setZOrderOnTop(true);
        //mPreview.init();
        mPreview.mCamera.setPreviewCallback(this);

    }

    protected void onResume() {
        super.onResume();
        if (mPreview == null)
            InitPreview();
    }

    protected void onPause() {
        super.onPause();
        mPreview = null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (null != mPptTask) {
            switch (mPptTask.getStatus()) {
                case RUNNING:
                    return;
                case PENDING:
                    mPptTask.cancel(false);
                    break;
            }
        }

        mPptTask = new PptTask(data);
        mPptTask.execute((Void) null);
    }

    private class PptTask extends AsyncTask<Void, Void, Void> {

        private byte[] mData;

        //构造函数
        PptTask(byte[] data) {
            this.mData = data;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            PreviewWidth = Previewsize.width;  //宽度
            PreviewHeight = Previewsize.height;
            Log.i("debug1", PreviewWidth + " " + PreviewHeight);
            // Log.i("debug1","识别");
            final YuvImage image = new YuvImage(mData, ImageFormat.NV21, PreviewWidth, PreviewHeight, null);
            ByteArrayOutputStream os = new ByteArrayOutputStream(mData.length);
            if (!image.compressToJpeg(new Rect(0, 0, PreviewWidth, PreviewHeight), 100, os)) {
                return null;
            }
            byte[] tmp = os.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
            PPtdetection(bmp);
            return null;
        }

        void trans(int[] point, int[] data) {
            for (int i = 0; i < 4; i++) {
                point[i * 2] = data[i * 2];
                point[i * 2 + 1] = PreviewHeight - data[i * 2 + 1];
            }
        }

        public boolean isConvexPolygon(int[] point) {
            if (!(point[1] < point[5] && point[0] < point[2] && point[3] < point[7] && point[6] > point[4]))
                return false;
            else {
                int t1 = (point[6] - point[0]) * (point[3] - point[1]) - (point[7] - point[1]) * (point[2] - point[0]);
                int t2 = (point[0] - point[2]) * (point[5] - point[3]) - (point[1] - point[3]) * (point[4] - point[2]);
                int t3 = (point[2] - point[4]) * (point[7] - point[5]) - (point[3] - point[5]) * (point[6] - point[4]);
                int t4 = (point[4] - point[6]) * (point[1] - point[7]) - (point[5] - point[7]) * (point[0] - point[6]);
                if (t1 * t2 * t3 * t4 < 0)
                    return false;
            }
            return true;
        }

        public void PPtdetection(Bitmap bitmap) {

            int w = bitmap.getWidth(), h = bitmap.getHeight();
            int[] pix = new int[w * h];
            //  Log.i("kaychen",w + "," + h);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);
            int[] solvepoint = {0, 0, 0, 0, 0, 0, 0, 0};
            int[] tempresult = {0, 0, 0, 0, 0, 0, 0, 0};
            OpenCVHelper.Checkedge(pix, tempresult, w, h);

            for (int i = 0; i < 4; ++i) {
                Log.i("kaychen", result[i * 2] + "," + result[i * 2 + 1] + " " + solvepoint[i * 2] + "," + solvepoint[i * 2 + 1]);
            }
            if (isConvexPolygon(tempresult)) {
                for (int i = 0; i < 8; ++i) {
                    result[i] = tempresult[i];
                }
                trans(solvepoint, result);
                mDrawview.drawLine(solvepoint, PreviewWidth, PreviewHeight, LayoutWidth, LayoutHeight);
            }
        }
    }
}