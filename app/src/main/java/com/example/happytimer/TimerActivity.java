package com.example.happytimer;

import android.graphics.Color;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.HashMap;

public class TimerActivity extends AppCompatActivity {

    ListView list;
    ArrayList<Period> periods = new ArrayList<Period>();
    PeriodAdapter adapter;
    private LogicVM myVM;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);
        myVM = ViewModelProviders.of(this).get(LogicVM.class);
        id  = getIntent().getIntExtra("id",0);
        DataBaseHelper db = new DataBaseHelper(this);
        periods = db.getPeriods(id);

        list =(ListView)findViewById(R.id.List1);
        adapter = new PeriodAdapter(this,R.layout.period_listview,periods,this);

        list.setAdapter(adapter);
        Button addbtn= (Button)findViewById(R.id.addButton);
        addbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddDialog alert = new AddDialog(TimerActivity.this);
                alert.showDialog(TimerActivity.this,id);
            }
        });

        myVM.getPeriodList().observe(this, new Observer<ArrayList<Period>>() {
            @Override
            public void onChanged(ArrayList<Period> s) {
                    periods.clear();
                    periods.addAll(s);
                    adapter.notifyDataSetChanged();
            }
        });
      //  myVM.setLiveDataPeriodList(periods);
    }




}
