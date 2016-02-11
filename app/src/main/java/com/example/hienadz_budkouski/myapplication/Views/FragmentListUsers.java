package com.example.hienadz_budkouski.myapplication.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hienadz_budkouski.myapplication.Activity;
import com.example.hienadz_budkouski.myapplication.ActivityFactory;
import com.example.hienadz_budkouski.myapplication.Interfaces.IDao;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.Models.DaoRecycler;
import com.example.hienadz_budkouski.myapplication.Models.HelperFactory;
import com.example.hienadz_budkouski.myapplication.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class FragmentListUsers extends Fragment implements LoaderManager.LoaderCallbacks<List<IUser>> {
    public final  static String frgmTag = FragmentListUsers.class.getSimpleName();
    static final int USER_LOADER = 1;
    public FragmentListUsers() {
    }

    private RecyclerView mRecyclerView;
    private Dyc mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;

    IItem item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            item = ActivityFactory.getMainActivity().getItem(args.getInt(IItem.ITEM_PARAM));
        }else  item = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_items_placeholder, container, false);

        mAdapter = new Dyc(null);

        if (item != null) {
          TextView  textView = (TextView) v.findViewById(R.id.textView);
            textView.setText(String.valueOf(item.getId() )+ " " + (item.getTitle()));
        }

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getSupportLoaderManager().getLoader(USER_LOADER).forceLoad();
            }
        });

        swipeLayout.setDistanceToTriggerSync(40);
        swipeLayout.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);

        swipeLayout.setSize(SwipeRefreshLayout.DEFAULT);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.listItemsView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);


      
        // specify an adapter (see also next example)


        mRecyclerView.setAdapter(mAdapter);

        registerForContextMenu(mRecyclerView);


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


        getLoaderManager().initLoader(USER_LOADER, null, this);
        getActivity().getSupportLoaderManager().initLoader(USER_LOADER, null, this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateData();
    }

    private void UpdateData() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
        getActivity().getSupportLoaderManager().getLoader(USER_LOADER).forceLoad();
    }


    public class Dyc extends DaoRecycler<UserViewHolder,IUser> {


        public Dyc(List<? extends IDao> list) {
            super(list);
        }


        @Override
        public void onBindViewHolder(final UserViewHolder viewHolder, final IUser user)  {

          if(item!=null)
              if(item.getRepairsId() == user.getId())
                  viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.md_amber_600));

            viewHolder.vName.setText(user.getStringFullName());
            viewHolder.vEmail.setText(user.getEmail());


            int bal = user.getBalance();
            if(bal==-1)
                viewHolder.vBalance.setText(R.string.not_defined);
            else  viewHolder.vBalance.setText(String.valueOf(bal));
            viewHolder.vPost.setText(user.getPost().toString());
            viewHolder.itemView.setTag(user.getId());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(item != null) {
                       item.setRepairsId(user.getId());
                       ActivityFactory.getMainActivity().updateItem(item);

                   }
                }
            });
            viewHolder.vImage.setImageBitmap(user.getBitmap());
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.card_user_view, parent, false);

            return new UserViewHolder(itemView);
        }
    }

    @Override
    public Loader<List<IUser>> onCreateLoader(int id, Bundle args) {
        return new CustomLoader(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<IUser>> loader, List<IUser> data) {
        mAdapter.changeCursor(data);
        mAdapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }


    @Override
    public void onLoaderReset(Loader<List<IUser>> loader) {

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vEmail;
        protected ImageView vImage;
        protected TextView vPost;
        protected TextView vBalance;

        public UserViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.card_name);
            vEmail = (TextView) v.findViewById(R.id.card_email);
            vPost = (TextView) v.findViewById(R.id.card_post);
            vBalance = (TextView) v.findViewById(R.id.card_balance);
            vImage = (ImageView) v.findViewById(R.id.card_img);
        }
    }


    static class CustomLoader extends AsyncTaskLoader<List<IUser>> {
        private static final String LOG_TAG = CustomLoader.class.getSimpleName();

        // DbBase is the parent class of all ORMLite data objects.
        private List<IUser> mData;

        public CustomLoader(Context context) {
            super(context);
        }

        @Override
        public List<IUser> loadInBackground() {
            try {
                TimeUnit.SECONDS.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return (List<IUser>) HelperFactory.getHelper().getUsersDAO().getAllUsers();
        }

        @Override
        public void deliverResult(List<IUser> data) {
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
}
