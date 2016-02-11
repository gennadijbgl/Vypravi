package com.example.hienadz_budkouski.myapplication.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.graphics.Palette;
import android.widget.Toast;

import com.example.hienadz_budkouski.myapplication.Activity;
import com.example.hienadz_budkouski.myapplication.ActivityFactory;
import com.example.hienadz_budkouski.myapplication.Interfaces.IDao;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Models.DaoRecycler;
import com.example.hienadz_budkouski.myapplication.Models.ListLoader;
import com.example.hienadz_budkouski.myapplication.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentListItems extends Fragment implements LoaderManager.LoaderCallbacks<List<IItem>>{
    public final  static String frgmTag = FragmentListItems.class.getSimpleName();

    static final int ITEM_LOADER = 0;
    static final int ITEM_LOADER_REP = 2;

    static int id_menu;


    static FragmentListItems fragmentListItemsParams = null;

    public FragmentListItems() {
    }

    private Dyc mAdapter;
    SwipeRefreshLayout swipeLayout;
    int mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mUserId = (args.getInt(IItem.ITEM_PARAM));
        }

    }

   public static FragmentListItems getInstance(Bundle args){
      if(args!=null)
      {
          if(fragmentListItemsParams == null) {
              fragmentListItemsParams = new FragmentListItems();
              fragmentListItemsParams.setArguments(args);
          }
          fragmentListItemsParams.setUserId((args.getInt(IItem.ITEM_PARAM)));

      }
       else {
          if(fragmentListItemsParams == null) fragmentListItemsParams = new FragmentListItems();
          fragmentListItemsParams.setUserId(0);

      }
       return fragmentListItemsParams;
   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_items_placeholder, container, false);

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mUserId!=0){
                if(getActivity().getSupportLoaderManager().getLoader(ITEM_LOADER_REP)== null) {
                    initLoader();

                }
                    getActivity().getSupportLoaderManager().getLoader(ITEM_LOADER_REP).forceLoad();
                }
                else   getActivity().getSupportLoaderManager().getLoader(ITEM_LOADER).forceLoad();
            }
        });

        swipeLayout.setDistanceToTriggerSync(40);
        swipeLayout.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);

        swipeLayout.setSize(SwipeRefreshLayout.DEFAULT);

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.listItemsView);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        mAdapter = new Dyc(null);

        mRecyclerView.setAdapter(mAdapter);

        // добавляем контекстное меню к списку
      //  registerForContextMenu(mRecyclerView);


        FloatingActionButton myFab = (FloatingActionButton) v.findViewById(R.id.myFAB);

        Drawable d= new IconicsDrawable(getActivity())
                .icon(GoogleMaterial.Icon.gmd_add)
                .color(getResources().getColor(R.color.md_white_1000)).sizeDp(48);

        myFab.setImageDrawable(d);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        ((Activity) getActivity()).openFragment(iMainActivity.Fragments.ItemAdd,true);
                }
        });

        initLoader();

        return v;
    }

    public void initLoader() {
        Log.d("MyApp","INILAODER");
        if(mUserId == 0) {
           getLoaderManager().initLoader(ITEM_LOADER, null, this);
            getActivity().getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);
        }
        else {
            getLoaderManager().initLoader(ITEM_LOADER_REP, null, this);
           getActivity().getSupportLoaderManager().initLoader(ITEM_LOADER_REP, null, this);
        }
    }

    public void setUserId(int userId){
        mUserId = userId;
       // initLoader();
    }
    @Override
    public void onResume() {
        super.onResume();
        initLoader();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("MyApp","ViewDestoyred");
    }



    public class Dyc extends DaoRecycler<ItemViewHolder,IItem> {


        public Dyc(List<? extends IDao> list) {
            super(list);
        }


        @Override
        public void onBindViewHolder(final ItemViewHolder viewHolder, final IItem item)  {

            viewHolder.vTitle.setText(item.getTitle());
            viewHolder.vDesc.setText(item.getDesc());

            int bal = item.getBalls();
            if(bal==-1)
                viewHolder.vBalls.setText(R.string.not_defined);
            else  viewHolder.vBalls.setText(String.valueOf(bal));
            viewHolder.vStatus.setText(item.getStatus().toString());
            viewHolder.itemView.setTag(item.getId());
            viewHolder.vDate.setText(item.getStringDate());

            viewHolder.vUserSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view, item.getId());
                }});


                    try {

                        viewHolder.vImage.post(new Runnable() {
                            @Override
                            public void run() {
                            viewHolder.vImage.setImageBitmap(item.getBitmapInView(viewHolder.vImage));
                            }
                        });

                        Palette.generateAsync(item.getBitmap(), new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {

                              //  int bgColor = palette.getMutedColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                              //  viewHolder.vDesc.setBackgroundColor(bgColor);

                            }
                        });
                    }
                    catch (Exception a){
                        viewHolder.vImage.setScaleType(ImageView.ScaleType.CENTER);
                        viewHolder.vImage.setImageDrawable(new IconicsDrawable(getActivity())
                                .icon(GoogleMaterial.Icon.gmd_error)
                                .color(getResources().getColor(R.color.md_red_500)).sizeDpX(40).sizeDpY(40));
                    }

        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.card_item_view, parent, false);

            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public Loader<List<IItem>> onCreateLoader(int id, Bundle args) {
        if(id == ITEM_LOADER_REP)
        return new ListLoader(getActivity().getApplicationContext(),mUserId);
        else  return new ListLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<IItem>> loader, List<IItem> data) {
        mAdapter.changeCursor(data);
        swipeLayout.setRefreshing(false);
    }


    private static void showPopupMenu(final View v, final Object context) {

        if(ActivityFactory.getMainActivity().getCurrentUser()==null) return;

        id_menu = (int) context;
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        popupMenu.inflate(R.menu.menu_item);

        if(ActivityFactory.getMainActivity().getCurrentUser().getPost() == IUser.Posts.Адміністратар)
        popupMenu.getMenu().setGroupVisible(R.id.group_admin,true);
        else
        {
            //TODO:change admin menu

        }

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        IItem iItem = ActivityFactory.getMainActivity().getItem(id_menu);
                        IUser iUser = ActivityFactory.getMainActivity().getCurrentUser();
                        switch (item.getItemId()) {

                            case R.id.menu_item_change: {
                                if(iItem.getUserId() == iUser.getId())
                                {
                                    Bundle args = new Bundle();
                                    args.putInt(IItem.ITEM_PARAM, (int) v.getTag());
                                    ActivityFactory.getMainActivity().openFragment(iMainActivity.Fragments.ItemAdd, true, args);
                                }
                                return true;
                            }
                            case R.id.menu_item_user_set: {
                                Bundle args = new Bundle();
                                args.putInt(IItem.ITEM_PARAM, iItem.getId());
                                ActivityFactory.getMainActivity().openFragment(iMainActivity.Fragments.Users, true, args);
                                return true;
                            }
                            case R.id.menu_item_status_set: {
                                ActivityFactory.getMainActivity().setTempItem(iItem);
                                ActivityFactory.getMainActivity().openFragment(iMainActivity.Fragments.Status, true);

                                return true;
                            }
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }
    @Override
    public void onLoaderReset(Loader<List<IItem>> loader) {

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder  {

        protected TextView vTitle;
        protected TextView vDesc;
        protected TextView vDate;
        protected TextView vBalls;
        protected ImageView vImage;
        protected TextView vStatus;
        protected ImageButton vUserSet;

        public ItemViewHolder(final View v) {
            super(v);


            vTitle =  (TextView) v.findViewById(R.id.card_name);
            vDesc = (TextView)  v.findViewById(R.id.card_desc);
            vDate = (TextView)  v.findViewById(R.id.card_date);
            vBalls = (TextView) v.findViewById(R.id.card_balls);
            vImage = (ImageView) v.findViewById(R.id.card_img);
            vStatus = (TextView) v.findViewById(R.id.card_status);
            vUserSet = (ImageButton) v.findViewById(R.id.card_menu);





        }





    }

}
