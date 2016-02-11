package com.example.hienadz_budkouski.myapplication.Models;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Hienadz on 03.01.16.
 */
public class UsersDAO extends BaseDaoImpl<CUser, Integer> {

    protected UsersDAO(ConnectionSource connectionSource,
                       Class<CUser> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public  List<? extends IUser> getAllUsers() {
        try {
            return this.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}