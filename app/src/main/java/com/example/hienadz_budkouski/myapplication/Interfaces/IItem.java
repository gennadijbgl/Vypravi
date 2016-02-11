package com.example.hienadz_budkouski.myapplication.Interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;



public interface IItem  extends IImg, IDao {

    enum  Status
    {
        Выяўляецца((byte) 0x0), Дапушчано(((byte) 0x1)),
        Выпраўлено(((byte) 0x2)), Скасавана(((byte) 0x3)),
        Праверано(((byte) 0x4));

        private final byte id;

        Status(int id) {
            this.id = (byte) id;
        }

        public byte getId() {
            return this.id;
        }

        static public Status getStatus(byte id) {
            return Status.values()[id];
        }

    }

    Status getStatus();

    void setStatus(Status status);

    String ITEM_PARAM = "ITEM_PARAM";

    Date getDate();

    String getStringDate();

    String getTitle();

    String getDesc();

    long getUserId();

    int getRepairsId();

    int getBalls();

    void setDate(Date date);

    void setTitle(String title);

    void setDesc(String desc);

    void setUserId(int userId);

    void setRepairsId(int repairsId);

    void setBalls(int balls);

    void setDateFromInts(int d, int m, int y);

}
