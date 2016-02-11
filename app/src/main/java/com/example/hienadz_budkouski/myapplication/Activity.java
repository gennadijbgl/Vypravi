package com.example.hienadz_budkouski.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.example.hienadz_budkouski.myapplication.Interfaces.iBackPressedListener;
import com.example.hienadz_budkouski.myapplication.Models.CItem;
import com.example.hienadz_budkouski.myapplication.Models.CUser;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Models.DatabaseHelper;
import com.example.hienadz_budkouski.myapplication.Models.HelperFactory;
import com.example.hienadz_budkouski.myapplication.Views.FragmentAdminMain;
import com.example.hienadz_budkouski.myapplication.Views.FragmentHello;
import com.example.hienadz_budkouski.myapplication.Views.FragmentItemAdd;
import com.example.hienadz_budkouski.myapplication.Views.FragmentListItems;
import com.example.hienadz_budkouski.myapplication.Views.FragmentListUsers;
import com.example.hienadz_budkouski.myapplication.Views.FragmentLogIn;
import com.example.hienadz_budkouski.myapplication.Views.FragmentRegister;
import com.example.hienadz_budkouski.myapplication.Views.FragmentStatusDialog;
import com.example.hienadz_budkouski.myapplication.Views.FragmentUserMain;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import java.sql.SQLException;
import java.util.List;

import static com.example.hienadz_budkouski.myapplication.ActivityFactory.getMainActivity;
import static com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity.Fragments.AdminMain;
import static com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity.Fragments.Hello;


public final class Activity extends AppCompatActivity implements iMainActivity {

    static final int ID_MAIN_PAGE = 1;
    static final int ID_ITEMS = 2;
    static final int ID_ITEMS_FOR_REPAIR = 3;
    static final int ID_SETTINGS = 4;
    static final int ID_HELP = 5;
    static final int ID_EXIT = 6;

    Drawer drawerResult;

    AccountHeader headerResult;

    private DatabaseHelper databaseHelper;

    public IUser mUser;

    IItem  item_temp;


    boolean doubleBackToExitPressedOnce = false;



    static final String SAVED_USER_L = "curr_user_l";
    static final String SAVED_USER_P = "curr_user_p";

    static final String DEBUG = "MyApp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        headerInit(savedInstanceState);
        drawerInit(toolbar);


       final ProgressDialog progDailog  = ProgressDialog.show(this, "Progress_bar or give anything you want",
                "Give message like ....please wait....", true);
        new Thread() {
            public void run() {
                try {

                    sleep(1000);
                } catch (Exception e) {
                }
                progDailog.dismiss();
            }
        }.start();

        initHelpers();
        userLoadPreferences();
    }

    @Override
    public void initHelpers() {
        if(HelperFactory.getHelper() == null){
            HelperFactory.setHelper(getApplicationContext());
            databaseHelper = HelperFactory.getHelper();
        }
        ActivityFactory.setHelper(this);
    }

    @Override
    public void releaseHelpers() {
        HelperFactory.releaseHelper();
    }

    public void userSavePreferences(){
        SharedPreferences.Editor ed = getPreferences(MODE_PRIVATE).edit();

        if (mUser != null) {
            ed.putString(SAVED_USER_L, mUser.getLogin());
            ed.putString(SAVED_USER_P, mUser.getPassword());
        }
        else
        {
            ed.clear();
        }
        ed.apply();
    }

    public void userLoadPreferences() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);

        final String l = sPref.getString(SAVED_USER_L, ""),
                     p = sPref.getString(SAVED_USER_P, "");

        if (!userLogin(l, p)) {
            openFragment(Hello, false);
        }
    }

    private void headerInit(Bundle savedInstanceState) {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
             //  .withHeaderBackground(R.drawable.header_bg)
                .withSavedInstance(savedInstanceState)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        openMainPage();
                        return false;
                    }
                })
                .build();

    }

    private void drawerInit(Toolbar toolbar) {

        drawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                      new PrimaryDrawerItem().withName(R.string.drawer_items_all).withIcon(GoogleMaterial.Icon.gmd_list).withIdentifier(ID_ITEMS),
                      new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(ID_SETTINGS),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_help).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(ID_HELP),

                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.action_exit).withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withIdentifier(ID_EXIT)
                )
                .withSelectedItemByPosition(ID_ITEMS)
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Скрываем клавиатуру при открытии Navigation Drawer
                        InputMethodManager inputMethodManager = (InputMethodManager) Activity.this.getSystemService(android.app.Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(Activity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (drawerItem.getIdentifier()){
                            case ID_MAIN_PAGE:{
                                openMainPage();
                                break;
                            }
                            case ID_EXIT:{
                                confirmDialogLogOut();
                                break;
                            }
                            case ID_ITEMS:{
                                openFragment(Fragments.Items, true);
                                break;
                            }
                            case ID_ITEMS_FOR_REPAIR:{
                                Bundle args = new Bundle();
                                args.putInt(IItem.ITEM_PARAM, (int) mUser.getId());
                                ActivityFactory.getMainActivity().openFragment(Fragments.Items, true, args);
                                break;
                            }
                            default: {
                                Log.d(DEBUG,"switch not exist");
                                break;
                            }
                        }

                        return false;
                    }
                })
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


    }

    private void openMainPage() {
        if (mUser == null) return;

            if (mUser.getPost() != IUser.Posts.Адміністратар)
                openFragment(AdminMain, false);
            else if (mUser.getPost() == IUser.Posts.Адміністратар)
                openFragment(AdminMain, false);
    }

    public void confirmDialogLogOut() {
        Snackbar s = Snackbar.make(((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0), Html.fromHtml("Сапраўды выйсці?"), Snackbar.LENGTH_LONG)
                .setAction("Так", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogout();}
        });
        s.setActionTextColor(Color.WHITE);
        s.show();
    }


    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        iBackPressedListener backPressedListener = null;

        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof iBackPressedListener) {
                backPressedListener = (iBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
            return;
        }


        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
            return;
        }

        if (doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = false;
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

            doubleBackToExitPressedOnce = true;
            Snackbar.make(findViewById(R.id.frgmCont), getResources().getString(R.string.press_back_againg),
                    Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3000);

        }
        else super.onBackPressed();

    }


    @Override
    protected void onDestroy() {
        ActivityFactory.releaseHelper();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        releaseHelpers();
        Log.d(DEBUG,"OnStop");
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(DEBUG,"OnRestart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(DEBUG,"OnPause");
    }

    @Override
    public void onResume(){
        super.onResume();
        updateBadges();
        Log.d(DEBUG,"OnResume");
    }

    private void updateBadges() {
        drawerResult.updateBadge(ID_ITEMS,new StringHolder(String.valueOf(ActivityFactory.getMainActivity().getItemsCount())));

        if(drawerResult.getDrawerItem(ID_ITEMS_FOR_REPAIR) !=null)
        drawerResult.updateBadge(ID_ITEMS_FOR_REPAIR,new StringHolder(String.valueOf(ActivityFactory.getMainActivity().getItemsForRepair(getCurrentUser().getId()))));
    }

    @Override
    protected void onStart() {
        Log.d(DEBUG,"OnStart");
        super.onStart();
        initHelpers();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    public void userLogout() {
        //TODO: TWO MY PAGE in drawer
        drawerResult.removeItem(ID_MAIN_PAGE);
        drawerResult.removeItem(ID_ITEMS_FOR_REPAIR);

        mUser = null;

        headerResult.clear();
        headerResult.setSelectionFirstLine(getString(R.string.user_guest_f));
        headerResult.setSelectionSecondLine(getString(R.string.user_guest_s));
        headerResult.setHeaderBackground(new ImageHolder(getResources().getDrawable(R.drawable.header_bg)));

        userSavePreferences();
        removeAllFragment();
        openFragment(Hello, false);
    }

    @Override
    public void userRegister(IUser user) {
        try {
            if (databaseHelper.getUsersDAO().create((CUser) user) == 1) {
                mUser = user;
                userLoginApply();
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkLoginExist(String login) {
        try {
            return databaseHelper.loginExist(login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }



    @Override
    public void userLoginApply() {



        IDrawerItem mDrawerItem = new PrimaryDrawerItem().withName(R.string.drawer_items_user).withIcon(GoogleMaterial.Icon.gmd_list).withBadge("").withIdentifier(ID_ITEMS_FOR_REPAIR);
        drawerResult.addItemAtPosition(mDrawerItem, 2);
        updateBadges();

       IProfile mProfile = new ProfileDrawerItem()
                .withName(mUser.getFirstName() + " " + mUser.getLastName())
                .withEmail(mUser.getEmail())
                .withIcon(mUser.getBitmapScalable(100,200));


        IDrawerItem mMainPage  = new PrimaryDrawerItem()
                    .withName(R.string.drawer_item_home)
                    .withIcon(GoogleMaterial.Icon.gmd_home)
                    .withIdentifier(ID_MAIN_PAGE);

        drawerResult.addItemAtPosition(mMainPage, 1);

        headerResult.addProfile(mProfile, 0);

        try {

            Palette.from(mUser.getBitmap()).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch vibrantSwatch = palette.getDarkMutedSwatch();
                    if (vibrantSwatch != null) {
                        headerResult.setHeaderBackground(new ImageHolder(new ColorDrawable(vibrantSwatch.getBodyTextColor())));
                    } else {
                        List<Palette.Swatch> f = palette.getSwatches();
                        headerResult.setHeaderBackground(new ImageHolder(new ColorDrawable(f.get(0).getBodyTextColor())));
                    }


                }
            });
        } catch (Exception s) {
s.printStackTrace();
        }
        headerResult.setSelectionFirstLine(null);
        headerResult.setSelectionSecondLine(null);

        userSavePreferences();
        openMainPage();
    }

    @Override
    public void userPictureUpdate() {
        HelperFactory.getHelper().updateUserPicture(mUser);
        headerResult.getActiveProfile().withIcon(mUser.getBitmap());
        headerResult.updateProfile(headerResult.getActiveProfile());
    }

    @Override
    public boolean userLogin(String login, String password) {
        mUser = databaseHelper.getUserAuth(login, password);
        if (mUser == null) return false;

        userLoginApply();
        return true;
    }


    @Override
    public IUser getCurrentUser() {
        return this.mUser;
    }

    @Override
    public IItem getItem(int id) {
        try {
            return databaseHelper.getItemsDAO().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IItem getTempItem() {
        return item_temp;
    }

    @Override
    public void setTempItem(IItem item) {
        item_temp = item;
    }

    @Override
    public int getItemsCount() {
        try {
            return (int) databaseHelper.getItemsDAO().countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getItemsForRepair(int id) {
        try {
            return databaseHelper.getItemsDAO().queryForEq("repairsId",id).size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void setItemRepairDone(int itemId) {
        try {
            IItem c =  databaseHelper.getItemsDAO().queryForId(itemId);
            c.setStatus(IItem.Status.Выпраўлено);
            databaseHelper.getItemsDAO().update((CItem) c);
            getCurrentUser().changeBalance(c);
            databaseHelper.getUsersDAO().update((CUser) getCurrentUser());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setItemRepairDone(IItem item) {
        try {
            databaseHelper.getItemsDAO().update((CItem) item);
            getCurrentUser().changeBalance(item);
            databaseHelper.getUsersDAO().update((CUser) getCurrentUser());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setItemStatus(IItem item, IItem.Status itemStatus,int balls) {

        item.setStatus(itemStatus);
        if(itemStatus == IItem.Status.Дапушчано){
            item.setBalls(balls);
        }

        if(itemStatus == IItem.Status.Праверано)
        {
            try {
                IUser iUser =   HelperFactory.getHelper().getUsersDAO().queryForId(item.getRepairsId());
                iUser.changeBalance(item);
                HelperFactory.getHelper().getUsersDAO().update((CUser) iUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        updateItem(item);
    }

    @Override
    public void setItemStatus(int id, IItem.Status itemStatus) {
        setItemStatus(getItem(id),itemStatus,0);
    }

    @Override
    public void addItem(IItem item) {
        try {
            databaseHelper.getItemsDAO().create((CItem) item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItem(IItem item) {
        databaseHelper.updateItem(item);
    }

    @Override
    public Uri resolveUriPath(Uri uri){
        if (uri != null && "content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                uri = Uri.parse(cursor.getString(0));
                cursor.close();
            } else Log.d(DEBUG,"Cursor is null");

        }
        return uri;
    }



    @Override
    public void openFragment(Fragments f, boolean addToBackStack, Bundle arg) {

        Fragment fr = null;
        Fragment t = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

if(true) {
    switch (f) {
        case Hello:
            fr = new FragmentHello();
            ft.add(R.id.frgmCont, fr, FragmentHello.frgmTag);
            break;

        case AdminMain:

            fr = new FragmentAdminMain();
            ft.add(R.id.frgmCont, fr, FragmentAdminMain.frgmTag);
            break;

        case ItemAdd:
            fr = new FragmentItemAdd();
            ft.add(R.id.frgmCont, fr, FragmentItemAdd.frgmTag);
            break;
        case Items:
            fr = FragmentListItems.getInstance(arg);
            t = getSupportFragmentManager().findFragmentByTag(FragmentListItems.frgmTag);
            if (t == null)
                ft.add(R.id.frgmCont, fr, FragmentListItems.frgmTag);
            break;
        case Login:
            fr = new FragmentLogIn();
            ft.add(R.id.frgmCont, fr, FragmentLogIn.frgmTag);
            break;
        case Register:
            fr = new FragmentRegister();
            ft.add(R.id.frgmCont, fr, FragmentRegister.frgmTag);
            break;
        case UserMain:
            fr = new FragmentUserMain();
            ft.add(R.id.frgmCont, fr, FragmentUserMain.frgmTag);
            break;
        case Users:
            fr = new FragmentListUsers();
            ft.add(R.id.frgmCont, fr, FragmentListUsers.frgmTag);
            break;
        case Status:
            fr = new FragmentStatusDialog();
            ft.add(R.id.frgmCont, fr, FragmentStatusDialog.class.getCanonicalName());
            break;
    }
}
        if(false) {
            fr = new FragmentStatusDialog();
            ft.add(R.id.frgmCont, fr, FragmentStatusDialog.class.getCanonicalName());

        }
            if (arg != null && t == null) fr.setArguments(arg);


            if (addToBackStack)
                ft.addToBackStack(null);
            ft.commit();



    }

    @Override
    public void openFragment(Fragments f, boolean addToBackStack) {
        openFragment(f, addToBackStack, null);
    }

    @Override
    public void popLastFragment() {

        if (this.getSupportFragmentManager().getBackStackEntryCount() > 0)
            this.getSupportFragmentManager()
                    .popBackStack(this.getSupportFragmentManager()
                            .getBackStackEntryAt(0).getName(), 0);


    }


    @Override
    public void removeAllFragment() {
        FragmentManager fm = getSupportFragmentManager();
        for(Fragment g : fm.getFragments())
        {
            getSupportFragmentManager().beginTransaction().remove(g).commit();
        }
    }
}
