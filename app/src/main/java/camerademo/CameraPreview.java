package camerademo;

/**
 * Created by cky on 2017/2/11.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static camerademo.MainActivity.Imagesize;
import static camerademo.MainActivity.Previewsize;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "debug1";
    private SurfaceHolder mHolder;
    protected Camera mCamera;
    private float oldDist = 1f;

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mCamera = getCameraInstance();
    }

    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
            Log.i(TAG, "camera is available");

        } catch (Exception e) {
            Log.i(TAG, "camera is not available");
        }
        return c;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            initCamera();
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Log.i(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder.removeCallback(this);
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {//自动对焦
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                if (b) {
                    initCamera();
                    camera.cancelAutoFocus();
                }
            }
        });
        try {
            mCamera.startPreview();
        } catch (Exception e) {
            // TODO: handle exception
            mCamera.release();
            mCamera = null;
        }
    }

    public Size getBestSupportedSize(List<Size> sizes) {//获取最大像素
        Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Size s : sizes) {
            int area = s.width * s.height;
            // Log.i(TAG,"" + s.width + " " + s.height);
            if (area > largestArea && area < 40000000) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    private void initCamera() //初始化相机参数
    {
        Camera.Parameters parameters = mCamera.getParameters();
        //Size s = parameters.getPreviewSize();
        Imagesize = getBestSupportedSize(parameters.getSupportedPictureSizes());
        Previewsize = getBestSupportedSize(parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(Previewsize.width, Previewsize.height);
        Log.i(TAG, "" + Previewsize.width + " " + Previewsize.height + "设置");
        parameters.setPictureSize(Imagesize.width, Imagesize.height);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        mCamera.setParameters(parameters);
        mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
        //  mCamera.setPreviewCallback(CameraPreview.this);
    }

    public Camera.Size GetPresize() {
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        return size;
    }

    private Uri outputMediaFileUri;
    private String outputMediaFileType;

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SlideNoteImage/");
        Log.i(TAG, mediaStorageDir.getPath());
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i(TAG, "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        outputMediaFileType = "image/*";
        outputMediaFileUri = Uri.fromFile(mediaFile);
        // Log.i(TAG, outputMediaFileUri.getPath());
        return mediaFile;
    }

    public Uri getOutputMediaFileUri() {
        return outputMediaFileUri;
    }

    public String getOutputMediaFileType() {
        return outputMediaFileType;
    }

    public void takePicture(final int[] point) {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFile = getOutputMediaFile();
                if (pictureFile == null) {
                    Log.i(TAG, "Error creating media file, check storage permissions");
                    return;
                }
                try {
                    Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    int w = Imagesize.width;  //宽度
                    int h = Imagesize.height;
                    int[] pix = new int[w * h];
                    Bitmap bMap2;
                    bMap.getPixels(pix, 0, w, 0, 0, w, h);
                    int[] newsize = {0, 0};
                    int[] pix2;
                    int[] rectmessage = new int[1000];
                    for (int i = 0; i < 4; ++i) {
                        Log.i("kaychentran", point[2 * i] + " " + point[2 * i + 1]);
                        Log.i("kaychentran", "--------------------------------------");
                    }
                    pix2 = OpenCVHelper.Cut(pix, point, w, h, newsize);
                    bMap2 = Bitmap.createBitmap(newsize[0], newsize[1], Bitmap.Config.ARGB_8888);
                    bMap2.setPixels(pix2, 0, newsize[0], 0, 0, newsize[0], newsize[1]);
                    Log.i("kaychen1", newsize[0] * newsize[1] + " ");
                    Bitmap bMapRotate;
                    Matrix matrix = new Matrix();  //将图片旋转90度
                    matrix.reset();
                    matrix.postRotate(90);
                    bMapRotate = Bitmap.createBitmap(bMap2, 0, 0, bMap2.getWidth(),
                            bMap2.getHeight(), matrix, true);
                    bMap2 = bMapRotate;
                    int wBmp2 = bMap2.getWidth();
                    int hBmp2 = bMap2.getHeight();
                    int[] pix3 = new int[wBmp2 * hBmp2];
                    bMap2.getPixels(pix3, 0, wBmp2, 0, 0, wBmp2, hBmp2);
                    pix3 = OpenCVHelper.Rect(pix3, rectmessage, wBmp2, hBmp2);
                    bMap2.setPixels(pix3, 0, wBmp2, 0, 0, wBmp2, hBmp2);
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);//将图片压缩到流中
                    bMap2.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();//输出
                    bos.close();//关闭
                    Log.i(TAG, "拍照成功");
                    Log.e(TAG, "onPictureTaken: "+pictureFile.getAbsolutePath() );
                    new ImageUserBiz().scanImage(new BaseListener.OnEventListener() {
                        @Override
                        public void eventSuccess() {

                        }

                        @Override
                        public void eventFail() {

                        }
                    });
                    Intent intent = new Intent();
                    intent.setClass(getContext(), OcrActivity.class);
                    Uri uri = getOutputMediaFileUri();
                    intent.setData(uri);
                    intent.putExtra("rectnum", rectmessage[500]);
                    intent.putExtra("rectmessage", rectmessage);
                    getContext().startActivity(intent);
                    //mCamera.startPreview();
                } catch (FileNotFoundException e) {
                    Log.i(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.i(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        });
    }


    private static Rect calculateTapArea(float x, float y, float coefficient, Camera.Size previewSize) { //转换坐标系
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) ((y / previewSize.width) * 2000 - 1000);
        int centerY = (int) ((x / previewSize.height) * -2000 + 1000);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        int right = clamp(centerX + areaSize / 2, -1000, 1000);
        int bottom = clamp(centerY + areaSize / 2, -1000, 1000);
        Log.i(TAG, "x--->" + x + ",,,y--->" + y);//对的
        Log.i(TAG, "previewSize.width--->" + previewSize.width + ",,,,previewSize.height--->" + previewSize.height);
        Log.i(TAG, "centerX--->" + centerX + ",,,centerY--->" + centerY);
        Log.i(TAG, "left--->" + left + ",,,top--->" + top + ",,,right--->" + (left + areaSize) + ",,,bottom--->" + (top + areaSize));
        RectF rectF = new RectF(left, top, right, bottom);

        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    private static void handleFocusMetering(MotionEvent event, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, Previewsize);
        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, Previewsize);
        if (parameters.getMaxNumMeteringAreas() > 0) { //触摸测光
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));
            parameters.setMeteringAreas(meteringAreas);
        } else {
            Log.i(TAG, "metering areas not supported");
        }
        camera.cancelAutoFocus();

        if (parameters.getMaxNumFocusAreas() > 0) { //触摸对焦
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 1000));
            parameters.setFocusAreas(focusAreas);
        } else {
            Log.i(TAG, "focus areas not supported");
        }
        final String currentFocusMode = parameters.getFocusMode();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(parameters);

        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success)//success表示对焦成功
                {
                    Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(currentFocusMode);
                    camera.setParameters(params);

                }
            }
        });
    }

    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void handleZoom(boolean isZoomIn, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.isZoomSupported()) {
            int maxZoom = parameters.getMaxZoom();
            int zoom = parameters.getZoom();
            if (isZoomIn && zoom < maxZoom) {
                zoom++;
            } else if (zoom > 0) {
                zoom--;
            }
            Log.i(TAG, " " + zoom);
            parameters.setZoom(zoom);
            camera.setParameters(parameters);
        } else {
            Log.i(TAG, "zoom not supported");
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            handleFocusMetering(event, mCamera);
        } else {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = getFingerSpacing(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newDist = getFingerSpacing(event);
                    if (newDist > oldDist + 20) {
                        handleZoom(true, mCamera);
                        oldDist = newDist;
                    } else if (newDist < oldDist - 20) {
                        handleZoom(false, mCamera);
                        oldDist = newDist;
                    }
                    oldDist = newDist;
                    break;
            }
        }
        return true;
    }

}