package camerademo;

/**
 * Created by Administrator on 2017/3/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*定义一个画矩形框的类*/
public class DrawView extends SurfaceView implements SurfaceHolder.Callback{

    protected SurfaceHolder sh;
    private int mWidth;
    private int mHeight;
    private int mov_x;//声明起点坐标
    private int mov_y;
    protected boolean EnableMotionEvent = false;
    private Canvas canvas;
    private Paint p;
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        sh = getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);

    }
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        // TODO Auto-generated method stub
        mWidth = w;
        mHeight = h;
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        if(EnableMotionEvent){
            canvas = sh.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawColor(Color.TRANSPARENT);
            p = new Paint();
            p.setAntiAlias(true);
            p.setStrokeWidth((float) 20);              //设置线宽
            p.setStyle(Style.FILL_AND_STROKE);
            p.setColor(Color.WHITE);
            p.setStrokeJoin(Paint.Join.MITER);
            p.setStrokeCap(Paint.Cap.ROUND);
            sh.unlockCanvasAndPost(canvas);
        }
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }
    public void drawLine(int []point,int previewwidth,int previewheight,int layoutwidth,int layoutheight)
    {
        if (sh.isCreating()){
            return;
        }
        canvas = sh.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.TRANSPARENT);
        p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth((float) 20);              //设置线宽
        p.setStyle(Style.FILL_AND_STROKE);
        p.setColor(Color.WHITE);
        p.setStrokeJoin(Paint.Join.MITER);
        p.setStrokeCap(Paint.Cap.ROUND);
        float[] temp = {0,0,0,0,0,0,0,0};
        for(int i = 0;i < 4;++i){
            temp[2 * i + 1] = (float)point[2 * i + 1] / previewheight * layoutheight;
            temp[2 * i] = (float)point[2 * i] / previewwidth * layoutwidth;
        }
        canvas.drawLine(temp[3],temp[2], temp[7],temp[6], p);
        canvas.drawLine(temp[5],temp[4], temp[1],temp[0], p);
        canvas.drawLine(temp[5],temp[4], temp[7],temp[6], p);
        canvas.drawLine(temp[3],temp[2], temp[1],temp[0], p);
        int indicatorColor = 0x66ffffff;
        p.setColor(indicatorColor);
        Path path1 = new Path();
        path1.moveTo(layoutheight,temp[0]);// 此点为多边形的起点
        path1.lineTo(temp[1],temp[0]);
        path1.lineTo(temp[3],temp[2]);
        path1.lineTo(temp[7],temp[6]);
        path1.lineTo(0,temp[6]);
        path1.lineTo(0,layoutwidth);
        path1.lineTo(layoutheight,layoutwidth);
        path1.close();
        canvas.drawPath(path1,p);
        Path path2 = new Path();
        path2.moveTo(0,temp[6] - 20);
        path2.lineTo(temp[7],temp[6] - 20);
        path2.lineTo(temp[5],temp[4]);
        path2.lineTo(temp[1],temp[0] - 20);
        path2.lineTo(layoutheight,temp[0] - 20);
        path2.lineTo(layoutheight,0);
        path2.lineTo(0,0);
        path2.close();
        canvas.drawPath(path2,p);
        sh.unlockCanvasAndPost(canvas);
    }
    public  boolean onTouchEvent(MotionEvent event) {
        if(EnableMotionEvent){
            canvas = sh.lockCanvas();
            if(event.getAction()== MotionEvent.ACTION_MOVE){
                canvas.drawLine(mov_x, mov_y, event.getX(), event.getY(), p);//画线
            }
            if(event.getAction()== MotionEvent.ACTION_DOWN){
                mov_x=(int) event.getX();
                mov_y=(int) event.getY();
                canvas.drawPoint(mov_x, mov_y, p);//画点
            }
            mov_x=(int) event.getX();
            mov_y=(int) event.getY();
            sh.unlockCanvasAndPost(canvas);
            return true;
        }
        return false;
    }
}