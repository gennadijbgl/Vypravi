package com.example.hienadz_budkouski.myapplication.Views;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hienadz_budkouski.myapplication.Activity;
import com.example.hienadz_budkouski.myapplication.Interfaces.IFragment;
import com.example.hienadz_budkouski.myapplication.Interfaces.iMainActivity;
import com.example.hienadz_budkouski.myapplication.R;


public class FragmentHello extends Fragment implements IFragment {
    public final  static String frgmTag = "FragmentHello";

    public FragmentHello() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hello, container, false);

        Fragment f= getActivity().getSupportFragmentManager().findFragmentByTag(FragmentHello.frgmTag);

        Button bReg = (Button) v.findViewById(R.id.button_reg);
        Button test = (Button) v.findViewById(R.id.button);
        Button test2 = (Button) v.findViewById(R.id.button2);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frgmCont,new FragmentLogIn(),FragmentLogIn.frgmTag).addToBackStack(null).commit();
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frgmCont,new FragmentLogIn(),FragmentLogIn.frgmTag).addToBackStack(null).commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
                Fragment fragment = getActivity().getSupportFragmentManager()
                        .findFragmentByTag(FragmentLogIn.frgmTag);

                if(fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    getActivity().getSupportFragmentManager().executePendingTransactions();

            }
                for(Fragment f: getActivity().getSupportFragmentManager().getFragments())
                {
                    Log.d("MyApp",f.getTag()+" "+ f.getClass().getSimpleName());
                }
        }});








       // final Animation shake = AnimationUtils.loadAnimation(this.getContext(), R.anim.button_a);
     //   bReg.setAnimation(shake);

        bReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Activity)getActivity()).openFragment(iMainActivity.Fragments.Register,true);

            }
        });

        Button bList = (Button) v.findViewById(R.id.bView);
        bList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getActivity()).openFragment(iMainActivity.Fragments.Items, true);        }
        });

        Button bLogin = (Button) v.findViewById(R.id.bLogIn);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Activity)getActivity()).openFragment(iMainActivity.Fragments.Login,true);

            }
        });
        return v;
    }

    @Override
    public String getMyTag() {
        return frgmTag;
    }
}
