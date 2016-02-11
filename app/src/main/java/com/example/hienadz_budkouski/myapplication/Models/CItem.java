package com.example.hienadz_budkouski.myapplication.Models;

import android.support.annotation.Nullable;

import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@DatabaseTable(tableName = "Items")
public class CItem extends CImg implements IItem {

    static private final SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private Date date;
    @DatabaseField(canBeNull = false)
    private String title;
    @DatabaseField(canBeNull = false)
    private String desc;
    @DatabaseField(canBeNull = false)
    private int userId;
    @DatabaseField(canBeNull = false)
    private int repairsId;
    @DatabaseField(canBeNull = true)
    private int balls;
    @DatabaseField(canBeNull = false)
    private byte status;

    public CItem(){
    }


    public String getTitle() {
        return title;
    }


    public String getDesc() {
        return desc;
    }


    public long getUserId() {
        return userId;
    }


    public int getRepairsId() {
        return repairsId;
    }


    public int getBalls() {
        return balls;
    }


    public int getId() {
        return id;
    }


    @Override
    public Status getStatus() {
        return Status.getStatus(status);
    }

    @Override
    public void setStatus(Status status) {
        this.status = status.getId();
    }


    public Date getDate() {
        return date;
    }


    public String getStringDate(){

        String s;
        s  =  sd.format(this.date);
        return s;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void setRepairsId(int repairsId) {
        this.repairsId = repairsId;
    }

    @Override
    public void setBalls(int balls) {
        this.balls = balls;
    }


    @Override
    public void setDateFromInts(int d, int m, int y){
            try
            {
              date = sd.parse(String.valueOf(d) + "." + String.valueOf(m) + "." + String.valueOf(y));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CItem cItem = (CItem) o;

        if (id != cItem.id) return false;
        if (userId != cItem.userId) return false;
        if (repairsId != cItem.repairsId) return false;
        if (balls != cItem.balls) return false;
        if (status != cItem.status) return false;
        if (date != null ? !date.equals(cItem.date) : cItem.date != null) return false;
        if (title != null ? !title.equals(cItem.title) : cItem.title != null) return false;
        return desc != null ? desc.equals(cItem.desc) : cItem.desc == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + userId;
        result = 31 * result + repairsId;
        result = 31 * result + balls;
        result = 31 * result + ((int) status);
        return result;
    }
}
