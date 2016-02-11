package com.example.hienadz_budkouski.myapplication.Views;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.hienadz_budkouski.myapplication.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentLoading extends Fragment {
    public   final  static String frgmTag = "FragmentLoading";

    public FragmentLoading() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loading, container, false);
        Button button = (Button) v.findViewById(R.id.loading_butt);
        final Animation shake = AnimationUtils.loadAnimation(this.getContext(), R.anim.loading);
        button.setAnimation(shake);
        button.animate();

        int i=0;
        while (i++<2500)
        {

          //  SystemClock.sleep(1000);

        }


        return  v;
    }

}
