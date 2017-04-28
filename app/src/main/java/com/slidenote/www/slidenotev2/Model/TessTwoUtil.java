package com.slidenote.www.slidenotev2.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Cieo233 on 4/12/2017.
 */

public class TessTwoUtil {
    private static final String TAG = "TessTwoUtil";
    private static String language = "chi_sim";
    private static final String toPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "tessdata/";

    public static void install(Context context) {
        File toFolder = new File(toPath);
        if (!toFolder.exists()) {
            toFolder.mkdirs();
        }
        try {
            String data[] = context.getAssets().list("tessdata");
            for (String path : data) {
                File file = new File(toPath + path);
                if (!file.exists()) {
                    InputStream inputStream = context.getAssets().open("tessdata/" + path);
                    OutputStream outputStream = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                    inputStream.close();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setLanguage(String language) {
        TessTwoUtil.language = language;
    }

    public static String ocr(String path) {
        TessBaseAPI api = new TessBaseAPI();
        api.init(Environment.getExternalStorageDirectory().getAbsolutePath(), language);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = convertGreyImg(bitmap);
        bitmap = scaleImage(bitmap, 5);
        api.setImage(bitmap);
        String text = api.getUTF8Text();
        api.end();
        return text;
    }

    private static Bitmap convertGreyImg(Bitmap image) {
        //得到图像的宽度和长度
        int width = image.getWidth();
        int height = image.getHeight();
        //创建线性拉升灰度图像
        Bitmap linegray = null;
        linegray = image.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到每点的像素值
                int col = image.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 增加了图像的亮度
                red = (int) (1.1 * red + 30);
                green = (int) (1.1 * green + 30);
                blue = (int) (1.1 * blue + 30);
                //对图像像素越界进行处理
                if (red >= 255) {
                    red = 255;
                }

                if (green >= 255) {
                    green = 255;
                }

                if (blue >= 255) {
                    blue = 255;
                }
                // 新的ARGB
                int newColor = alpha | (red << 16) | (green << 8) | blue;
                //设置新图像的RGB值
                linegray.setPixel(i, j, newColor);
            }
        }
        return linegray;
    }

    private static Bitmap scaleImage(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
}
