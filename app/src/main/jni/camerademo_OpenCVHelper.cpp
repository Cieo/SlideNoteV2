#include "camerademo_OpenCVHelper.h"
#include <stdio.h>
#include <stdlib.h>
#include<algorithm>
#include <opencv2/opencv.hpp>

using namespace cv;
IplImage * change4channelTo3InIplImage(IplImage * src);
extern "C" {

JNIEXPORT void JNICALL Java_camerademo_OpenCVHelper_Checkedge
        (JNIEnv *, jclass, jintArray, jintArray,jint, jint);

JNIEXPORT jintArray JNICALL Java_camerademo_OpenCVHelper_Cut
        (JNIEnv *, jclass, jintArray,jintArray, jint, jint, jintArray);

JNIEXPORT jintArray JNICALL Java_camerademo_OpenCVHelper_Rect
        (JNIEnv *, jclass, jintArray,jintArray, jint, jint);

JNIEXPORT void JNICALL Java_camerademo_OpenCVHelper_Checkedge(
        JNIEnv *env, jclass obj, jintArray buf, jintArray point,int w, int h) {

    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf, JNI_FALSE );
    if (cbuf == NULL) {
        return;
    }

    Mat src(h, w, CV_8UC4, (unsigned char *) cbuf);
    Mat I;
   // GaussianBlur(src,  I, Size(3, 3), 0, 0);
    Mat gray, thresh;
    cvtColor( src, gray, CV_BGR2GRAY);

    //轮廓提取
    medianBlur(gray,gray,31);
    threshold(gray,thresh,150,255,0);
    vector<vector<Point> > contours0;
    vector<Vec4i> hierarchy;
    findContours(thresh, contours0, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, Point());


    //多边形逼近
    vector<vector<Point> > contours;
    contours.resize(contours0.size());

    for (int i = 0; i < contours0.size(); i++)
    {
        approxPolyDP(contours0[i], contours[i], 90, true);//15是为了得到一个矩形，小于15的数回得到更多的点
    }

    //选出最大面积的多边形
    double area = 0;
    int index=0;
    for (int i = 0; i < contours.size(); i++)
    {
        if (contourArea(contours[i])>area)
        {
            area = contourArea(contours[i]);
            index = i;
        }
    }


    //最外围轮廓顶点的显示
    vector<Point> contours2;
    contours2.resize(4);

    Point center (src.size().width / 2,src.size().height / 2);

    contours2[0].x = center.x *2;
    contours2[0].y = center.y *2;
    contours2[1].y = center.y *2;
    contours2[2].x = center.x *2;
    //cout << center << endl;
// 左上 右上 左下 右下
    if(contours.size() != 0){
        for(int i = 0; i < contours[index].size(); i++){
            if(contours[index][i].x < center.x && contours[index][i].y < center.y){
                if(contours2[0].y > contours[index][i].y)
                    contours2[0].y = contours[index][i].y;
                if(contours2[0].x > contours[index][i].x)
                    contours2[0].x = contours[index][i].x;
            }
            else if(contours[index][i].x > center.x && contours[index][i].y < center.y){
                if(contours2[1].y > contours[index][i].y)
                    contours2[1].y = contours[index][i].y;
                if(contours2[1].x < contours[index][i].x)
                    contours2[1].x = contours[index][i].x;
            }
            else if(contours[index][i].x < center.x && contours[index][i].y > center.y){
                if(contours2[2].y < contours[index][i].y)
                    contours2[2].y = contours[index][i].y;
                if(contours2[2].x > contours[index][i].x)
                    contours2[2].x = contours[index][i].x;
            }
            else if(contours[index][i].x > center.x && contours[index][i].y > center.y){
                if(contours2[3].y < contours[index][i].y)
                    contours2[3].y = contours[index][i].y;
                if(contours2[3].x < contours[index][i].x)
                    contours2[3].x = contours[index][i].x;
            }
        }
    }
    else{
        return;
    }
    jint* temp = env->GetIntArrayElements(point, NULL);
    for(int i = 0;i < 4;++i){
        temp[i * 2] = contours2[i].x;
        temp[i* 2 + 1] =  contours2[i].y;
    }
    env->ReleaseIntArrayElements(buf, cbuf, 0);
    env->ReleaseIntArrayElements(point, temp, 0);
    return;
}
JNIEXPORT jintArray JNICALL Java_camerademo_OpenCVHelper_Cut
        (JNIEnv *env, jclass obj, jintArray buf, jintArray point,int w, int h,  jintArray newsize){
    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf, JNI_FALSE );
    if (cbuf == NULL) {
         return buf;
    }
    Mat src(h, w, CV_8UC4, (unsigned char *) cbuf);
    vector<Point> contours;
    jint* temp = env->GetIntArrayElements(point, NULL);
    jint* temp2 = env->GetIntArrayElements(newsize, NULL);
    for(int i = 0;i < 4;++i){
        Point a;
        a.x = temp[i * 2];
        a.y = temp[i * 2 + 1];
        contours.push_back(a);
    }
    vector<Point2f> corner;//上面提取轮廓的顶点
    for(int i = 0; i < 4; i++)
    {
        corner.push_back(contours[i]);
    }
    vector<Point2f> PerspectiveTransform;//透视变换后的顶点
    RotatedRect box = minAreaRect(cv::Mat(contours));
    PerspectiveTransform.push_back(Point(0, 0));
    PerspectiveTransform.push_back(Point(box.boundingRect().width - 1, 0));
    PerspectiveTransform.push_back(Point(0, box.boundingRect().height - 1));
    PerspectiveTransform.push_back(Point(box.boundingRect().width - 1, box.boundingRect().height - 1));

    //获取变换矩阵
    Mat M = getPerspectiveTransform(corner, PerspectiveTransform);//Order of points matters!

    //cout << "PerspectiveTransform: " << endl << PerspectiveTransform << endl;

    Mat out;
    cv::Size size(box.boundingRect().width, box.boundingRect().height);
    warpPerspective(src, out, M,size, 1, 0, 0);
//    IplImage image=IplImage(out);
//    IplImage* image3channel = change4channelTo3InIplImage(&image);
    int* outImage=new int[size.width*size.height];
//    for(int i=0;i<size.width*size.height;i++)
//    {
//        outImage[i]=(int)image3channel->imageData[i];
//    }
    int rows = out.rows;
    int cols = out.cols * out.channels();
    if(out.isContinuous())//判断是否在内存中连续
    {
        cols = cols * rows;
        rows = 1;
    }
    for(int i = 0;i<rows;i++)
    {
        //调取存储图像内存的第i行的指针
        uchar *pointer = out.ptr<uchar>(i);
        int cnt = 0;
       for(int j = 0;j<cols;j += 4)
        {
            //1 r 2 g 3 B
            outImage[cnt] = (( pointer[j +3] * 256 +  pointer[j +2]) * 256 +pointer[j +1] ) * 256 +  pointer[j + 0];
            cnt++;
        }
    }
    jintArray result = env->NewIntArray(size.width*size.height);
    env->SetIntArrayRegion(result, 0, size.width*size.height, outImage);
    //jint *array = env->GetIntArrayElements(result, NULL);
    temp2[0] = size.width;
    temp2[1] = size.height;
    env->ReleaseIntArrayElements(buf, cbuf, 0);
    env->ReleaseIntArrayElements(point, temp, 0);
    env->ReleaseIntArrayElements(newsize, temp2, 0);
    return result;
}

bool cmp(Rect a,Rect b){

    if (a.y < b.y+10 && a.y > b.y -10)
    {
        return (a.x < b.x);
    }
    else if (a.y == b.y)
    {
        return (a.x < b.x);
    }
    else
    {
        return a.y < b.y;
    }
}
JNIEXPORT jintArray JNICALL Java_camerademo_OpenCVHelper_Rect
    (JNIEnv *env, jclass obj, jintArray buf, jintArray rectmessage,int w, int h){
    jint *cbuf;
    cbuf = env->GetIntArrayElements(buf, JNI_FALSE );
    if (cbuf == NULL) {
         return buf;
    }
    Mat src(h, w, CV_8UC4, (unsigned char *) cbuf);
    Mat gray;
    //转换到灰度图
    cvtColor( src, gray, CV_BGR2GRAY);
    Mat element(3,8,CV_8U);
    erode(gray,gray,element,Point(-1,-1),4);
    Mat thresh;
    //二值化
    threshold(gray, thresh, 0, 255, 1|8);
    //检测连通域，每一个连通域以一系列的点表示，FindContours方法只能得到第一个域
    vector<vector<Point> > contours0;
    vector<Vec4i> hierarchy;
    findContours(thresh, contours0, CV_RETR_EXTERNAL ,CV_CHAIN_APPROX_SIMPLE );
    int cnt = 0;
    vector<Rect> recttext;
    for (int i = 0; i < contours0.size(); i++)
    {
        Rect rect = boundingRect(Mat(contours0[i]));

        if(rect.height > 10 && rect.width > 10 && (rect.width * 1.0 / rect.height) > 0.2){
             recttext.push_back(rect);
            ++cnt;
         }
    }
    // cout << cnt << endl;
    sort(recttext.begin(),recttext.end(),cmp);

    Rect t;
    vector<Rect> rectcom;
    int count = 0;
    int flag  = recttext[0].y + recttext[0].height / 2 ;
    if(cnt != 0) t = recttext[0];
    for(int i = 1;i < cnt;++i){
        int line = recttext[i].y + recttext[i].height / 2 ;
        if(line < flag + 20 && line > flag -20){
        if((t.x < recttext[i].x && t.x + t.width + 30 > recttext[i].x) || (recttext[i].x < t.x && recttext[i].x + recttext[i].width + 30 > t.x) ){
             t = t | recttext[i];
        }
        else{
            ++count;
            rectcom.push_back(t);

            flag = line;
            t =recttext[i];
            }
        }
        else{
            ++count;
            rectcom.push_back(t);
            flag = line;
            t =recttext[i];
        }
         if(i == cnt - 1) rectcom.push_back(t);
    }
    jint* temp = env->GetIntArrayElements(rectmessage, NULL);
    int j = 0;
    for( vector<Rect>::iterator iter = rectcom.begin();iter != rectcom.end();++iter){
        temp[j++] = (*iter).x;
        temp[j++] = (*iter).y;
        temp[j++] = (*iter).width;
        temp[j++] = (*iter).height;
//  cout << (*iter).x<< " " <<  (*iter).y << " " <<  (*iter).width << " " << (*iter).height << " " <<  endl;
        rectangle(src,*iter,(167,40,68),2);
    }
    temp[500] = count;
    int size = w * h;
    jintArray result = env->NewIntArray(size); // 新建一个jintArray
    env->SetIntArrayRegion( result, 0, size, cbuf); // 将cbuf转存入result
    env->ReleaseIntArrayElements(buf, cbuf, 0); // 释放int数组元素
    env->ReleaseIntArrayElements(rectmessage, temp, 0);
    return result;
}
}
