package com.example.hienadz_budkouski.myapplication.Models;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hienadz on 03.01.16.
 */
public class ItemsDAO extends BaseDaoImpl<CItem, Integer> {

    protected ItemsDAO(ConnectionSource connectionSource,
                       Class<CItem> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<? extends IItem> getAllItems() {

        try {
            return this.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<? extends IItem> getAllItemsByUserRepair(int id) {

        try {

            return this.queryForEq("repairsId",id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}