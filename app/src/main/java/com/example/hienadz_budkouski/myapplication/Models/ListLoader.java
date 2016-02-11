package com.example.hienadz_budkouski.myapplication.Models;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hienadz on 30.01.16.
 */
public class ListLoader extends AsyncTaskLoader<List<IItem>> {
    private static final String LOG_TAG = ListLoader.class.getSimpleName();


    private List<IItem> mData;
    private int mUserId;

    public ListLoader(Context context, int userId) {
        super(context);
        this.mUserId = userId;
    }

    public ListLoader(Context context) {
        super(context);
        this.mUserId = 0;
    }

    @Override
    public List<IItem> loadInBackground() {
        try {
            TimeUnit.SECONDS.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(mUserId == 0) {

            return (List<IItem>) HelperFactory.getHelper().getItemsDAO().getAllItems();
        }
        else {
            return (List<IItem>) HelperFactory.getHelper().getItemsDAO().getAllItemsByUserRepair(mUserId);
        }
    }

    @Override
    public void deliverResult(List<IItem> data) {
        if (isReset()) {
            return;
        }

        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            mData = null;
        }
    }

}
