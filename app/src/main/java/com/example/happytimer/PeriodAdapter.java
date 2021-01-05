package com.example.happytimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

public class PeriodAdapter extends ArrayAdapter<Period> {

    private LayoutInflater inflater;
    private int layout;
    private List<Period> periods;
    private LogicVM myVM;
    private Intent intent;

    public PeriodAdapter(Context context, int resource, List<Period> periods, Activity activity) {
        super(context, resource, periods);
        this.periods = periods;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        myVM = ViewModelProviders.of((FragmentActivity) activity).get(LogicVM.class);
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView titleView = (TextView) view.findViewById(R.id.titleText);
        TextView timerView = (TextView) view.findViewById(R.id.timerText);


        Button del = (Button)view.findViewById(R.id.delbtn);

        final Period period = periods.get(position);
        del.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                myVM.deletePeriod(getContext(),period);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(position==0)
                {
                myVM.skipPeriod(getContext(),period);
                }
            }
        });



        titleView.setText(period.getTitle());
        timerView.setText(String.valueOf(period.getSeconds()));
       // view.setBackgroundColor(Color.parseColor(period.getColour()));
       // colourView.setTextColor(Color.parseColor(period.getColour()));
        return view;
    }
}

