package com.example.hienadz_budkouski.myapplication.Models;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hienadz_budkouski.myapplication.Interfaces.IDao;
import com.example.hienadz_budkouski.myapplication.Interfaces.IImg;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;

import java.util.Collection;
import java.util.List;


public abstract class DaoRecycler<VH extends RecyclerView.ViewHolder, T extends IDao> extends RecyclerView.Adapter<VH> {

    private List<? extends IDao> mList;

    private boolean mDataValid;

    public DaoRecycler(List<? extends IDao> list) {

        mList = list;
        mDataValid = list != null;

    }


    @Override
    public int getItemCount() {
        if (mDataValid && mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mList != null) {
            return mList.get(position).getId();
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
      //  super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, T item);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        onBindViewHolder(viewHolder, (T) mList.get(position));
    }

    public void changeCursor(List<? extends IDao> list) {

        if(list != null && mList == null) {
            mList = list;
            mDataValid = true;
            this.notifyDataSetChanged();
            return;
        }



        if(!(list).equals(mList))
        {
           if(list.size()<mList.size()) {
               mList.retainAll(list);
           }
            else {
               list.removeAll(mList);
               ((List<IDao>)mList).addAll(list);
           }
            //mList = (list);
            mDataValid = true;
            this.notifyDataSetChanged();
        }
    }

}