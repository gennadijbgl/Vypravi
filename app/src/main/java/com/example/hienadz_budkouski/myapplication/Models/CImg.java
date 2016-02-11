package com.example.hienadz_budkouski.myapplication.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.example.hienadz_budkouski.myapplication.Interfaces.IImg;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class CImg implements IImg {


    private Bitmap bImg;
    private int hashCodeCache = 0;

    @DatabaseField(canBeNull = true, dataType = DataType.BYTE_ARRAY)
    private byte[] img = null;


     public  CImg(){

    }

    public void setBitmapFromUri(Uri path){

        final BitmapFactory.Options options = new BitmapFactory.Options();

        // Используем конфигурацию без прозрачности
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        logMemory();
        this.setBitmap(BitmapFactory.decodeFile(path.getPath(), options));


       if(false) {
           File imagefile = new File(path.getPath());
           FileInputStream fis = null;
           try {

               fis = new FileInputStream(imagefile);
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }

           if (fis != null)
               this.setBitmap(BitmapFactory.decodeStream(fis));

           try {
               if (fis != null)
                   fis.close();

           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    public Bitmap getBitmap() {
        synchronized (CImg.this) {
            if (bImg == null && img != null) {
                bImg = BitmapFactory.decodeByteArray(img, 0, img.length);
            }
        }
        return bImg;
    }

    @Override
    public Bitmap getBitmapScalable(int new_height, int new_width) {
        if(new_height == 0 || new_width == 0 || bImg ==null)
        return bImg;

        getBitmap();

        int width = bImg.getWidth();
        int height = bImg.getHeight();

        if(width == new_width && height == new_height) return bImg;

        float scaleWidth = ((float) new_width) / width;
        float scaleHeight = ((float) new_height) / height;

        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =  Bitmap.createScaledBitmap(bImg,new_width, new_height, true);  //creacteBitmap(
         //       bImg, 0, 0, width, height, matrix, false);

      //  bImg.recycle();
        //bImg = null;
        //bImg = resizedBitmap;
        return resizedBitmap;
    }

    @Override
    public Bitmap getBitmapInView(View view) {

        return this.getBitmapScalable(view.getHeight(),view.getWidth());
    }


    public void setBitmap(Bitmap Img){
        hashCodeCache = 0;
        if(bImg!=null)  bImg = null;
        if(img!=null)   img = null;


        bImg =  Img;
        logMemory();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Log.d("MyApp", String.valueOf(bImg.getByteCount()));
        bImg.compress(Bitmap.CompressFormat.PNG, 100, baos);
        Log.d("MyApp", String.valueOf(bImg.getByteCount()));
        this.img = baos.toByteArray();

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CImg cImg = (CImg) o;

      return Arrays.equals(img, cImg.img);

    }

    @Override
    public int hashCode() {
        if(hashCodeCache == 0 )
            hashCodeCache = Arrays.hashCode(img);

        return 31 + hashCodeCache;
    }
    private void logMemory() {
        Log.i("MyApp", String.format("Total memory = %s",
                (int) (Runtime.getRuntime().totalMemory() / 1024)));
    }
}
