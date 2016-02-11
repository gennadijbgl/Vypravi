package com.example.hienadz_budkouski.myapplication.Models;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Hienadz on 03.01.16.
 */
public class HelperFactory{

    private static DatabaseHelper databaseHelper = null;

    public static synchronized DatabaseHelper getHelper(){
        return databaseHelper;
    }
    public static void setHelper(Context context){
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }
    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}