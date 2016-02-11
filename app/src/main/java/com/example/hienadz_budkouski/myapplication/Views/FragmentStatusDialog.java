package com.example.hienadz_budkouski.myapplication.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hienadz_budkouski.myapplication.Interfaces.IItem;
import com.example.hienadz_budkouski.myapplication.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.hienadz_budkouski.myapplication.ActivityFactory.getMainActivity;

/**
 * Created by Hienadz on 03.02.16.
 */
public class FragmentStatusDialog extends Fragment  {

    final String LOG_TAG = "myLogs";


    @Bind(R.id.spinner)
    Spinner mSpinner;

    @Bind(R.id.status_save)
    Button mSave;
    @Bind(R.id.status_balls)
    TextView mBalls;

    public  FragmentStatusDialog(){
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_status_dialog, container,false);
        ButterKnife.bind(this,v);

        ArrayAdapter s = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item);


        for(IItem.Status sf : IItem.Status.values())
        if(getMainActivity().getTempItem().getStatus().getId() <= sf.getId())
        s.add(sf.toString());

        mSpinner.setAdapter(s);

        if(getMainActivity().getTempItem().getStatus() == IItem.Status.Выяўляецца)
            v.findViewById(R.id.layout_balls).setVisibility(View.GONE);
        else {
            v.findViewById(R.id.layout_balls).setEnabled(false);
            v.findViewById(R.id.layout_balls).setVisibility(View.VISIBLE);
        }

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(IItem.Status.valueOf((String) adapterView.getItemAtPosition(i)) == IItem.Status.Дапушчано)
               {
                    v.findViewById(R.id.layout_balls).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
mSave.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        IItem.Status s = IItem.Status.getStatus((byte) mSpinner.getSelectedItemPosition());
        getMainActivity().setItemStatus(getMainActivity().getTempItem(),s,Integer.parseInt(mBalls.getText().toString()));
        getMainActivity().setTempItem(null);
    }
});
        return v;
    }




}