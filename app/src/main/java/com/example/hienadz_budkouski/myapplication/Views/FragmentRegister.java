package com.example.hienadz_budkouski.myapplication.Views;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.Toast;

import com.example.hienadz_budkouski.myapplication.ActivityFactory;
import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.Interfaces.IUser;
import com.example.hienadz_budkouski.myapplication.Activity;
import com.example.hienadz_budkouski.myapplication.Models.CUser;
import com.example.hienadz_budkouski.myapplication.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;


public class FragmentRegister extends Fragment implements Validator.ValidationListener {

    public   final  static String frgmTag = "FragmentRegister";

    private Validator validator;


    @Length(min = 3,max = 25)
    @NotEmpty(messageResId = R.string.err_msg_empty)
    private MaterialEditText mMidName;

    @Length(min = 3,max = 25)
    @NotEmpty(messageResId = R.string.err_msg_empty)
    private MaterialEditText mLastName;

    @Length(min = 3,max = 25)
    @NotEmpty(messageResId = R.string.err_msg_empty)
    private MaterialEditText mFirstName;

    @NotEmpty(messageResId = R.string.err_msg_empty)
    @Email(messageResId = R.string.err_msg_email)
    private MaterialEditText     mMail;

    @NotEmpty(messageResId = R.string.err_msg_empty)
    @Password(scheme = Password.Scheme.ALPHA_NUMERIC)
    @Length(min = 6,max = 25)
    private MaterialEditText     mPassword;

    @NotEmpty(messageResId = R.string.err_msg_empty)
    private MaterialEditText      mLogin;



    public FragmentRegister() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reg, container, false);
        
         mLogin = (MaterialEditText) v.findViewById(R.id.inputLogin);
         mPassword = (MaterialEditText) v.findViewById(R.id.inputPass);
         mMail = (MaterialEditText) v.findViewById(R.id.inputMail);

         mFirstName = (MaterialEditText) v.findViewById(R.id.inputFName);
         mLastName = (MaterialEditText) v.findViewById(R.id.inputLName);
         mMidName = (MaterialEditText) v.findViewById(R.id.inputMName);


        Button b =   (Button)v.findViewById(R.id.reg_butt);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);

        return  v;
    }



    @Override
    public void onValidationSucceeded() {


            if(((iMainActivity) getActivity()).checkLoginExist(mLogin.getText().toString())){
                mLogin.setError(getString(R.string.err_msg_user_exist));
                return;
            }

        Bitmap d= new IconicsDrawable(getActivity())
                .icon(GoogleMaterial.Icon.gmd_person)
                .color(getResources().getColor(R.color.md_deep_purple_700)).sizeDp(32).toBitmap();

        CUser.UserBuilder userBuilder =  CUser.newBuilder();

       IUser u= userBuilder
               .setLogin(mLogin.getText().toString())
               .setPassword(mPassword.getText().toString())
               .setFirstName(mFirstName.getText().toString())
               .setLastName(mLastName.getText().toString())
               .setMiddleName(mMidName.getText().toString())
               .setEmail(mMail.getText().toString())
               .setPost(IUser.Posts.Практыкант)
               .setBitmap((d))
               .build();


        ActivityFactory.getMainActivity().userRegister(u);


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());


            if (view instanceof MaterialEditText) {
                ((MaterialEditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


}
