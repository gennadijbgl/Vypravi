package com.example.hienadz_budkouski.myapplication.Interfaces;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;


public interface iMainActivity {

    enum Fragments{Hello, Items, ItemAdd, Login, Register, UserMain, AdminMain, Users,Status};

    void userRegister(IUser user);
    void userLoginApply();
    void userPictureUpdate();
    void userLogout();
    void userSavePreferences();
    void userLoadPreferences();
    boolean userLogin(String login, String password);

    void confirmDialogLogOut();
    boolean checkLoginExist(String login);


    void initHelpers();
    void releaseHelpers();

    IUser getCurrentUser();
    IItem getItem(int id);

    IItem getTempItem();
    void setTempItem(IItem item);

    int getItemsCount();
    int getItemsForRepair(int id);
    void setItemRepairDone(int itemId);
    void setItemRepairDone(IItem item);
    void setItemStatus(IItem item, IItem.Status itemStatus,int balls);
    void setItemStatus(int id, IItem.Status itemStatus);

    void addItem(IItem item);
    void updateItem(IItem item);
    Uri resolveUriPath(Uri uri);


    void openFragment(Fragments f, boolean addToBackStack);
    void openFragment(Fragments f, boolean addToBackStack,Bundle arg);
    void popLastFragment();
    void removeAllFragment();

}
