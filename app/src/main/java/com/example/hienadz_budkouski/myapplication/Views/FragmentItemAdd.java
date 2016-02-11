package com.example.hienadz_budkouski.myapplication.Views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hienadz_budkouski.myapplication.Activity;
import com.example.hienadz_budkouski.myapplication.ActivityFactory;
import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.Interfaces.iBackPressedListener;
import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.Models.CItem;
import com.example.hienadz_budkouski.myapplication.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.*;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import butterknife.*;

public class FragmentItemAdd extends Fragment implements iBackPressedListener, Validator.ValidationListener {

    public final static String frgmTag = "FragmentItemAdd";


    private static final int CHOOSE_PIC_OPEN = 3;
    private static final int CHOOSE_PIC_NEW = 2;
    private static final int TYPE_PHOTO = 1;

    File mDirectory;
    Uri mUri;


    @Bind(R.id.item_a_title)
    @Order(1)
    @NotEmpty(messageResId = R.string.err_msg_empty )
    @Length(min = 6, max = 25, messageResId = R.string.err_msg_length)
    MaterialEditText item_title;


    @Bind(R.id.item_a_desc)
    @NotEmpty(messageResId = R.string.err_msg_empty)
    @Order(2)
    @Length(min = 6, max = 500, messageResId = R.string.err_msg_length)
    MaterialEditText item_desc;


    @Bind(R.id.item_a_date)
    @NotEmpty(messageResId = R.string.err_msg_date_empty)
    @Order(3)
    MaterialEditText item_date;

    @Bind(R.id.item_a_photo)
       //TODO: Validator
    ImageView im;



    @Bind(R.id.item_a_photo_text)
    MaterialEditText photo_text;


    boolean isPhotoAttach;

    @Bind(R.id.bItemAdd)
    Button bApply;


    DatePickerDialog dialog;
    int myYear, myMonth, myDay;

    IItem item;
    boolean isNewItem;


    Validator validator;


    public FragmentItemAdd() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            item = ActivityFactory.getMainActivity().getItem(args.getInt(IItem.ITEM_PARAM));
            isNewItem = false;
        }
        else isNewItem = true;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_photo_add, menu);
        menu.setHeaderTitle(R.string.item_photo_dialog_title);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_open: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PIC_OPEN);
                return true;
            }
            case R.id.photo_new: {
                initOutputDirectory();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mUri = generateFileUri(TYPE_PHOTO);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(intent, CHOOSE_PIC_NEW);
                return true;
            }

        }
        return super.onContextItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_item_add, container, false);
        ButterKnife.bind(this, v);
        validator = new Validator(this);
        validator.setValidationListener(this);

        dialog = null;

        im.setOnCreateContextMenuListener(this);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               view.showContextMenu();
            }
        });

  if (!isNewItem) {

      getActivity().setTitle(getString(R.string.item_change));

            item_title.setText(item.getTitle());
            item_desc.setText(item.getDesc());
            Calendar.getInstance().setTime(item.getDate());
            item_date.setText(item.getStringDate());
            bApply.setText(R.string.save);
            im.setImageBitmap(item.getBitmap());

        } else {
            getActivity().setTitle(getString(R.string.item_new));
            item = new CItem();
            Drawable d = new IconicsDrawable(getActivity())
                    .icon(GoogleMaterial.Icon.gmd_add_a_photo)
                    .color(getResources().getColor(R.color.md_grey_500)).sizeDpX(80).sizeDpY(80);
            im.setImageDrawable(d);
            isPhotoAttach = false;

        }


        myYear = Calendar.getInstance().get(Calendar.YEAR);
        myMonth = Calendar.getInstance().get(Calendar.MONTH);
        myDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);







        item_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) item_date.callOnClick();
            }
        });


        item_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null)
                    InitDateDialog();
                dialog.show();
            }
        });

        bApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });


        return v;
    }


            void InitDateDialog() {
               Calendar calendar = Calendar.getInstance();

               dialog = new DatePickerDialog(getActivity(),
                       R.style.MaterialBaseTheme_Light_Dialog,
                       new DatePickerDialog.OnDateSetListener() {
                           public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                 int dayOfMonth) {
                               myYear = year;
                               myMonth = monthOfYear;
                               myDay = dayOfMonth;

                               item_date.setFloatingLabelText(getResources().getString(R.string.item_date));
                               item_date.setText(String.format(getResources().getString(R.string.date_format), myDay ,(myMonth + 1) ,myYear));

                           }
                       }, myYear, myMonth, myDay);

               dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
               calendar.add(Calendar.MONTH, -1);
               dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
           }


           @Override
           public void onBackPressed() {
               Snackbar.make(getView(),getString(R.string.abort_confirm),Snackbar.LENGTH_LONG).setAction((R.string.yes), new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       ((Activity) getActivity()).popLastFragment();
                     // getActivity().getContentResolver().delete(mUri, null, null);
                   }
               }).show();
           }


    @Override
    public void onValidationSucceeded() {


            if(isPhotoAttach) {
                 photo_text.setHintTextColor(getResources().getColor(R.color.md_deep_purple_600));
                photo_text.setError(null);
            }
            else {
                photo_text.setError(getString(R.string.item_photo_help));
                return;
            }

        //Todo: Check image replacing and additions system!!!

            item.setTitle(item_title.getText().toString());
            item.setDesc(item_desc.getText().toString());
            item.setUserId(ActivityFactory.getMainActivity().getCurrentUser().getId());
            item.setDateFromInts(myDay,myMonth,myYear);


        if(!isNewItem)
            ActivityFactory.getMainActivity().updateItem(item);
            else
            ActivityFactory.getMainActivity().addItem(item);

        ActivityFactory.getMainActivity().popLastFragment();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages ;)
            if (view instanceof MaterialEditText) {

                ((MaterialEditText) view).setError(message);
            }
            else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }






           @Override
           public void onActivityResult(int requestCode, int resultCode,
                                        Intent intent) {

               if (requestCode == CHOOSE_PIC_OPEN) {
                   if (resultCode == android.app.Activity.RESULT_OK) {
                       mUri = intent.getData();
                       Log.d("MyApp", "INTENT");

                       mUri = ((iMainActivity) getActivity()).resolveUriPath(mUri);

                       ProcessImage(false);
                   }

               } else if (requestCode == CHOOSE_PIC_NEW) {
                   if (resultCode == android.app.Activity.RESULT_OK) {
                       if (intent == null) {

                           ProcessImage(true);
                       }
                   }

               }
               super.onActivityResult(requestCode, resultCode, intent);
           }


           private boolean ProcessImage(final boolean isNew) {

               item.setBitmapFromUri(mUri);
               im.setImageBitmap(item.getBitmapInView(im));
               isPhotoAttach = true;

               return true;
           }


           private void initOutputDirectory() {
               mDirectory = new File(
                       Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                       "VypraviDyAtrymaj");
               if (!mDirectory.exists())
                   mDirectory.mkdirs();
           }

           private Uri generateFileUri(int type) {
               File file = null;
               switch (type) {
                   case TYPE_PHOTO:
                       file = new File(mDirectory.getPath() + "/" + "photo_"
                               + System.currentTimeMillis() + ".jpg");
                       break;

               }
               return Uri.fromFile(file);
           }

}

