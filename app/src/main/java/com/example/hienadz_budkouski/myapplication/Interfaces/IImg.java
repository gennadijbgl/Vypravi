package com.example.hienadz_budkouski.myapplication.Interfaces;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Hienadz on 04.01.16.
 */
public interface IImg {

    Bitmap getBitmap();


    Bitmap getBitmapScalable(int new_height, int new_width);

    Bitmap getBitmapInView(View view);
    void setBitmapFromUri(Uri path);
    void setBitmap(Bitmap Img);
}
