package com.slidenote.www.slidenotev2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/3/18.
 */

public class WebViewScreenshot {
    private static Bitmap getViewBitmapWithoutBottom(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap bp = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight() - v.getPaddingBottom());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return bp;
    }

    public static Bitmap getViewBitmap(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return b;
    }

    /**
     * 获取 WebView 视图截图
     * @param context
     * @param view
     * @return
     */
    public static Bitmap getWebViewBitmap(Context context, WebView view) {
        if (null == view) return null;
        view.scrollTo(0, 0);
        view.buildDrawingCache(true);
        view.setDrawingCacheEnabled(true);
        view.setVerticalScrollBarEnabled(false);
        Bitmap b = getViewBitmapWithoutBottom(view);
        // 可见高度
        int vh = view.getHeight();
        // 容器内容实际高度
        int th = (int)(view.getContentHeight()*view.getScale());
        Bitmap temp = null;
        if (th > vh) {
            int w = getScreenWidth(context);
            int absVh = vh - view.getPaddingTop() - view.getPaddingBottom();
            do {
                int restHeight = th - vh;
                if (restHeight <= absVh) {
                    view.scrollBy(0, restHeight);
                    vh += restHeight;
                    temp = getViewBitmap(view);
                } else {
                    view.scrollBy(0, absVh);
                    vh += absVh;
                    temp = getViewBitmapWithoutBottom(view);
                }
                b = mergeBitmap(vh, w, temp, 0, view.getScrollY(), b, 0, 0);
            } while (vh < th);
        }
        // 回滚到顶部
        view.scrollTo(0, 0);
        view.setVerticalScrollBarEnabled(true);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 拼接图片
     * @param newImageH
     * @param newImageW
     * @param background
     * @param backX
     * @param backY
     * @param foreground
     * @param foreX
     * @param foreY
     * @return
     */
    private static Bitmap mergeBitmap(int newImageH, int newImageW, Bitmap background, float backX, float backY, Bitmap foreground, float foreX, float foreY) {
        if (null == background || null == foreground) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(newImageW, newImageH, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(bitmap);
        cv.drawBitmap(background, backX, backY, null);
        cv.drawBitmap(foreground, foreX, foreY, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return bitmap;
    }

    /**
     * get the width of screen
     */
    public static int getScreenWidth(Context ctx) {
        int w = 0;
        if (Build.VERSION.SDK_INT > 13) {
            Point p = new Point();
            ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(p);
            w = p.x;
        } else {
            w = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        }
        return w;
    }

    /**
     * 保存图片
     * @param context
     * @param bitmap
     * @param file
     * @param quality
     * @return
     */
    public static boolean save(Context context, Bitmap bitmap, File file, int quality) {
        if (bitmap == null) return false;
        // 获得后缀格式
        String abs = file.getAbsolutePath();
        String suffix = abs.substring(abs.lastIndexOf(".")+1).toLowerCase();
        Bitmap.CompressFormat format;
        if ("jpg".equals(suffix) || "jpeg".equals(suffix)) {
            format = Bitmap.CompressFormat.JPEG;
        } else {
            format = Bitmap.CompressFormat.PNG;
            quality = 100;
        }
        if (file.exists() && ! file.delete()) return false;
        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(format, quality, stream);
            stream.flush();
            stream.close();
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
