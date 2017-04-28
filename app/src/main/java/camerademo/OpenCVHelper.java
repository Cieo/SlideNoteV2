package camerademo;

/**
 * Created by Administrator on 2017/3/6.
 */

public class OpenCVHelper {
    static {
        System.loadLibrary("OpenCV");
    }
    public static native void Checkedge(int[] buf,int[] point, int w, int h);
    public static native int[] Cut(int[] buf, int[] point,int w, int h,int[] newsize);
    public static native int[] Rect(int[] buf, int[] rectmessage,int w, int h);
}