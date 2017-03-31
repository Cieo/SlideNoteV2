package com.slidenote.www.slidenotev2;

import android.app.Application;
import android.content.Context;
import org.litepal.LitePal;

/**
 * Created by Cieo233 on 3/27/2017.
 */

public class SlideNoteApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}