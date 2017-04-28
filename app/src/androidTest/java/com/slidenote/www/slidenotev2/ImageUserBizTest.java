package com.slidenote.www.slidenotev2;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.slidenote.www.slidenotev2.Model.BaseListener;
import com.slidenote.www.slidenotev2.Model.Image;
import com.slidenote.www.slidenotev2.Model.ImageFolder;
import com.slidenote.www.slidenotev2.Model.ImageUserBiz;
import com.slidenote.www.slidenotev2.Utils.Util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Cieo233 on 3/28/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ImageUserBizTest {
    private static final String TAG = "ImageUserBizTest";
    private ImageUserBiz imageUserBiz;

    @Before
    public void createUserBiz(){
        imageUserBiz = new ImageUserBiz();
    }


}
