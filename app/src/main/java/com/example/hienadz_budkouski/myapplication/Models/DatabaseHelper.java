package com.example.hienadz_budkouski.myapplication.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="myappname.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 7;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
    private ItemsDAO itemsDao = null;
    private UsersDAO usersDao = null;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try
        {
            TableUtils.createTable(connectionSource, CItem.class);
            TableUtils.createTable(connectionSource, CUser.class);
        }
        catch (SQLException e){
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer,
                          int newVer){
        try{
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
            TableUtils.dropTable(connectionSource, CItem.class, true);
            TableUtils.dropTable(connectionSource, CUser.class, true);
            onCreate(db, connectionSource);
        }
        catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
            throw new RuntimeException(e);
        }
    }


    public synchronized UsersDAO getUsersDAO(){
        if(usersDao == null){
            try {
                usersDao = new UsersDAO(getConnectionSource(), CUser.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usersDao;
    }

    public boolean loginExist(String login) throws SQLException {
        if(getUsersDAO().countOf()==0) return false;
        int size = getUsersDAO().queryForEq("login", login).size();
        return size  > 0;
    }

    public void updateUserPicture(IUser user){
        try {

            getUsersDAO().update((CUser) user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(IItem item){
        try {

            getItemsDAO().update((CItem) item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public IUser getUserAuth(String login,String password) {

        QueryBuilder<CUser,Integer> queryBuilder = getUsersDAO().queryBuilder();

        IUser  user = null;

        try {
            user = queryBuilder.where().eq("login", login).and().eq("password", password).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }
    //синглтон для UsersDAO
    public synchronized ItemsDAO  getItemsDAO() {
        if(itemsDao == null){

            try {
                itemsDao = new ItemsDAO(getConnectionSource(), CItem.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return itemsDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
        itemsDao = null;
        usersDao = null;
    }
}

