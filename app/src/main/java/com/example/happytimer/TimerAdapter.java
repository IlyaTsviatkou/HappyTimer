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

    public class TimerAdapter extends ArrayAdapter<Timer> {

        private LayoutInflater inflater;
        private int layout;
        private List<Timer> timers;
        private LogicVM myVM;
        private Intent intent;
        private DataBaseHelper db ;
        public TimerAdapter(Context context, int resource, List<Timer> timers, Activity activity) {
            super(context, resource, timers);
            this.timers = timers;
            this.layout = resource;
            this.inflater = LayoutInflater.from(context);
            db =  new DataBaseHelper(context);
            myVM = ViewModelProviders.of((FragmentActivity) activity).get(LogicVM.class);
        }
        public View getView( int position, View convertView, ViewGroup parent) {

            View view=inflater.inflate(this.layout, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.titleText);
            TextView colourView = (TextView) view.findViewById(R.id.colourText);

            intent = new Intent(getContext(), TimerActivity.class);
            Button del = (Button)view.findViewById(R.id.delbtn);

            final Timer timer = timers.get(position);
            del.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    myVM.deleteTimer(getContext(),timer);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {

                    intent.putExtra("id",timer.getId());
                    getContext().startActivity(intent);
                }
            });


            titleView.setText(timer.getTitle());
         //   colourView.setText(timer.getColour());
           // colourView.setTextColor(Color.parseColor(timer.getColour()));
            return view;
        }
    }

