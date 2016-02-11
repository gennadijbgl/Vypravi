package com.example.hienadz_budkouski.myapplication.Views;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hienadz_budkouski.myapplication.ActivityFactory;
import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.hienadz_budkouski.myapplication.ActivityFactory.getMainActivity;

public  class FragmentUserMain extends Fragment implements View.OnClickListener {
    public final  static String frgmTag = FragmentUserMain.class.getSimpleName();
    final  static int CHOOSE_PIC = 96;

    @Bind(R.id.bChangePicture)  Button mChange;
    @Bind(R.id.bAdd)            Button mAdd;
    @Bind(R.id.bLogOut)         Button mLogOut;
    @Bind(R.id.editBalls)       EditText tBalls;
    @Bind(R.id.edit_user)       TextView tf;
    @Bind(R.id.imageUserPic)    CircleImageView im;
    @Bind(R.id.textHello)       TextView textHello;

    public FragmentUserMain() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_main, container, false);
        ButterKnife.bind(this,v);


        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openFragment(iMainActivity.Fragments.ItemAdd, true);
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityFactory.getMainActivity().userLogout();
            }
        });

        im.setOnClickListener(this);
        mChange.setOnClickListener(this);

        tf.setText(ActivityFactory.getMainActivity().getCurrentUser().getStringFullName());
        im.setImageBitmap(ActivityFactory.getMainActivity().getCurrentUser().getBitmap());
        tBalls.setText(String.valueOf(ActivityFactory.getMainActivity().getCurrentUser().getBalance()));
        textHello.setText(getString(R.string.hello)+" "+ActivityFactory.getMainActivity().getCurrentUser().getPost().toString());



        return  v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        switch (requestCode) {
            case CHOOSE_PIC: {

                if (resultCode == android.app.Activity.RESULT_OK) {
                    Uri mUri = data.getData();

                    im.setImageURI(mUri);

                  mUri = ActivityFactory.getMainActivity().resolveUriPath(mUri);

                    final Uri finalMUri = mUri;
                    Snackbar s = Snackbar.make(getView(), getString(R.string.user_change_pic_commit),
                            Snackbar.LENGTH_LONG)
                            .setAction("Так", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityFactory.getMainActivity().getCurrentUser().setBitmapFromUri(finalMUri);
                                    ActivityFactory.getMainActivity().userPictureUpdate();
                                }
                            });

                    s.setCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            im.setImageBitmap(ActivityFactory.getMainActivity().getCurrentUser().getBitmap());
                            super.onDismissed(snackbar, event);
                        }
                    });
                    s.setActionTextColor(Color.WHITE);
                    s.show();
                }

            } break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PIC);
    }
}
