package com.slidenote.www.slidenotev2;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import pdfview.PDFView;
import pdfview.listener.OnDrawListener;
import pdfview.listener.OnLoadCompleteListener;
import pdfview.listener.OnPageChangeListener;
import com.lowagie.text.DocumentException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/13.
 */

public class PdfViewer extends AppCompatActivity implements OnPageChangeListener
        , OnLoadCompleteListener, OnDrawListener {

    private static final String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_test/";
    private String HTMLContents = "";
    private String mPDFFileName;
    private PDFView pdfView;
    private ImageButton PDFButton;
//    private static final  String ASSERT_PATH = "file:///android_asset/";
    private static final String SET_UP = "Note_editor.pdf";
//    private static final String OUTPUT_PDF = "Note_editor.pdf";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer);
        Intent intent = getIntent();
        if (intent != null){
            Log.i("---------------","开始读取pdf");
            HTMLContents = intent.getStringExtra("HTMLContents");
            System.out.println("PDF网页内容为:"+HTMLContents);
            if (HTMLContents != null){
                pdfView = (PDFView) findViewById(R.id.pdfVIew);
                //从assets目录读取pdf
                displayFromAssets(SET_UP);
//                try {
//                    if (htmlToPdf(ALBUM_PATH, SET_UP)){
//                        //从文件中读取pdf
//                        displayFromFile( new File(ALBUM_PATH + mPDFFileName));
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            findViewById(R.id.PDFButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mPDFFileName = String.format("Note_editor_%s.pdf", System.currentTimeMillis());
//                    new Thread(saveFileRunnable).start();
                }
            });
            findViewById(R.id.ReturnButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PdfViewer.this.finish();
                }
            });
        }
    }

//    public boolean htmlToPdf(String path, String outputFile)
//            throws Exception {
//
//        File file = new File(path);
//        if (!file.exists()){
//            file.mkdir();
//        }
//        File fileName = new File(path+outputFile);
//        if (fileName.exists()) {
//            fileName.delete();
//        }
//        fileName.createNewFile();
//
//        OutputStream os = new FileOutputStream(outputFile);
//        ITextRenderer renderer = new ITextRenderer();
////        String url = new File(inputFile).toURI().toURL().toString();
//
//        renderer.setDocument(HTMLContents);
//
//        // 解决中文支持问题
//        ITextFontResolver fontResolver = renderer.getFontResolver();
//        fontResolver.addFont("file:///android_asset/font/Lanting_founder_black.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        //解决图片的相对路径问题
////        renderer.getSharedContext().setBaseURL("file:/D:/");
//        renderer.layout();
//        renderer.createPDF(os);
//
//        os.flush();
//        os.close();
//        return true;
//    }
//    private void htmlToPdf(String path, String outputFile) throws IOException, DocumentException {
//        File file = new File(path);
//        if (!file.exists()){
//            file.mkdir();
//        }
//        File fileName = new File(path+outputFile);
//        if (fileName.exists()) {
//            fileName.delete();
//        }
//        fileName.createNewFile();
//        OutputStream os = new FileOutputStream(path+outputFile);
//        ITextRenderer renderer = new ITextRenderer();
//        ITextFontResolver fontResolver = renderer.getFontResolver();
//        fontResolver.addFont("file:///android_asset/font/Lanting_founder_black.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        StringBuffer html = new StringBuffer();
//        html.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
//        html.append(HTMLContents);
//        renderer.setDocumentFromString(html.toString());
//        renderer.layout();
//        renderer.createPDF(os);
//        Log.i("----------------","======转换成功!");
//        os.close();
//    }

//    private void ToPdf(String filename){
//        //创建一个文档对象
//        Document doc = new Document();
//        try {
//            File file = new File(filename);
//            if (!file.exists()){
//                file.createNewFile();
//            }
//            //定义输出文件的位置
//            PdfWriter.getInstance(doc, new FileOutputStream(filename));
//            //开启文档
//            doc.open();
//            //设定字体 为的是支持中文
//            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
//
//            //向文档中加入图片
//            Image jpg1 = Image.getInstance(mPicFileName); //原来的图片的路径
//            //设置图片居中显示
//            jpg1.setAlignment(Image.MIDDLE);
//            doc.add(jpg1);
//            //关闭文档并释放资源
//            doc.close();
//        } catch (DocumentException | IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void saveFile (String fileName) throws IOException, DocumentException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH+fileName);
        if (!myCaptureFile.exists()){
            myCaptureFile.createNewFile();
        }
//        try {
//            if (htmlToPdf(ALBUM_PATH,fileName)){
//                System.out.println("保存PDF");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try{
                saveFile(mPDFFileName);
            }catch (IOException | DocumentException ex){
                ex.printStackTrace();
            }
        }
    };

    private void displayFromAssets(String assetFileName ) {
        pdfView.fromAsset(assetFileName)   //设置pdf文件地址
                .defaultPage(6)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical( false )  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻页
                // .pages( 2 , 3 , 4 , 5  )  //把2 , 3 , 4 , 5 过滤掉
                .load();
    }

    private void displayFromFile( File file ) {
        pdfView.fromFile(file)   //设置pdf文件地址
                .defaultPage(6)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical( false )  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻
                // .pages( 2 , 3 , 4 , 5  )  //把2 , 3 , 4 , 5 过滤掉
                .load();
    }

    /**
     * 翻页回调
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        Toast.makeText( PdfViewer.this , "page= " + page +
                " pageCount= " + pageCount , Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载完成回调
     * @param nbPages  总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText( PdfViewer.this ,  "加载完成" + nbPages  , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        // Toast.makeText( ImageListActivity.this ,  "pageWidth= " + pageWidth + "
        // pageHeight= " + pageHeight + " displayedPage="  + displayedPage , Toast.LENGTH_SHORT).show();
    }
}
