package com.example.hienadz_budkouski.myapplication.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.hienadz_budkouski.myapplication.ActivityFactory;
import com.example.hienadz_budkouski.myapplication.Interfaces.iCallback;
import com.example.hienadz_budkouski.myapplication.R;
import com.rengwuxian.materialedittext.MaterialEditText;


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentLogIn extends Fragment {
    public final static String frgmTag = "FragmentLogIn";

    private MaterialEditText input_layout_Login, input_layout_Pass;


    public FragmentLogIn() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        input_layout_Login = (MaterialEditText) v.findViewById(R.id.editLoginL);
        input_layout_Pass = (MaterialEditText) v.findViewById(R.id.editLoginP);

        input_layout_Login.addTextChangedListener(new MyTextWatcher(input_layout_Login, new iCallback() {
                    @Override
                    public boolean ValidateField() {
                        return validateLengthField(input_layout_Login);
                    }
                }));

        input_layout_Pass.addTextChangedListener(new MyTextWatcher(input_layout_Pass, new iCallback() {
            @Override
            public boolean ValidateField() {
                return validateLengthField(input_layout_Pass);
            }
        }));


        v.findViewById(R.id.bLoginL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!validateLogin()) {
                        return;
                    }
                    if (!validatePassword()) {
                        return;
                    }

                boolean b = ActivityFactory.getMainActivity().userLogin(input_layout_Login.getText().toString(), input_layout_Pass.getText().toString());
                if (!b) {
                    input_layout_Pass.setError(getString(R.string.err_msg_password_not_pass));
                    requestFocus(input_layout_Pass);
                    return;
                }
            }
        });

        return v;
    }

    private boolean validateLogin() {
        if (!validateLengthField(input_layout_Login)) {
            return false;
        }
        boolean b = ActivityFactory.getMainActivity().checkLoginExist(input_layout_Login.getText().toString());
        if (!b) {
            input_layout_Login.setError(getString(R.string.err_msg_user_not_exist));
            requestFocus(input_layout_Login);
            return false;
        }

        input_layout_Login.setError(null);
        return true;
    }

    private boolean validatePassword() {
        if (!validateLengthField(input_layout_Pass)) {
            return false;
        }

        input_layout_Pass.setError(null);
        return true;
    }

    private boolean validateLengthField(MaterialEditText layout_text) {

        if (layout_text.getText().toString().isEmpty()) {

            layout_text.setError(getString(R.string.err_msg_empty));

            requestFocus(layout_text);
            return false;
        }

        if (layout_text.getText().toString().length() > 25
                || layout_text.getText().toString().length() < 6) {



            layout_text.setError(getString(R.string.err_msg_length));
            requestFocus(layout_text);
            return false;
        }

        layout_text.setError(null);

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;
        iCallback func;

        private MyTextWatcher(View view, iCallback func) {
            this.view = view;
            this.func = func;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            func.ValidateField();

        }
    }
}

