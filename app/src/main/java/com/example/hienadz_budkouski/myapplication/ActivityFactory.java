package com.example.hienadz_budkouski.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.Models.DatabaseHelper;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Hienadz on 05.01.16.
 */
public class ActivityFactory  {

    private static iMainActivity sMainActivity = null;

    public static iMainActivity getMainActivity(){

        return sMainActivity;
    }

    public static void setHelper(iMainActivity activity){
        if(sMainActivity!=null) sMainActivity = null;
        sMainActivity = activity;
    }
    public static void releaseHelper(){
        sMainActivity = null;
    }

}
