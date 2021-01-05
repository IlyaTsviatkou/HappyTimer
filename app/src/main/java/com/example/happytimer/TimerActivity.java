package com.example.happytimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class TimerActivity extends AppCompatActivity {

    ListView list;
    ArrayList<Period> periods = new ArrayList<Period>();
    DataBaseHelper db;
    PeriodAdapter adapter;
    private LogicVM myVM;
    TextView timer,tv_timer;
    private CountDownTimer mCountDownTimer;
    private int numberOfSecuence = 0,id=0;
    private long SecondsofAll=0;
    String secMassive;
    Intent intent ;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    Button playbtn,pausebtn,addbtn;


    private ArrayList<Integer> timersSeconds= new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);
        intent = new Intent(getApplicationContext(),Timer_Service.class);
        myVM = ViewModelProviders.of(this).get(LogicVM.class);
        id  = getIntent().getIntExtra("id",0);
        tv_timer  = (TextView)findViewById(R.id.tv_timer);
        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();
        mEditor.clear().commit();
        db = new DataBaseHelper(this);
        list =(ListView)findViewById(R.id.List1);
        periods = db.getPeriods(id);
        secMassive = "";
        for (Period p : periods)
        {
            secMassive+=String.valueOf(p.getSeconds())+"|";
            timersSeconds.add(p.getSeconds());
            SecondsofAll+=p.getSeconds();
        }
        adapter = new PeriodAdapter(this,R.layout.period_listview,periods,this);
        list.setAdapter(adapter);
        addbtn= (Button)findViewById(R.id.addButton);
        playbtn= (Button)findViewById(R.id.playButton);
        pausebtn =(Button)findViewById(R.id.pauseButton);
        timer = (TextView) findViewById(R.id.timer);
        try{
            timer.setText(String.valueOf(periods.get(0).getSeconds()));
            mEditor.putString("activeTimer",String.valueOf(periods.get(0).getSeconds())).commit();
        }
        catch (Exception e)
        {
            timer.setText("0");
        }

        addbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddDialog alert = new AddDialog(TimerActivity.this);
                alert.showDialog(TimerActivity.this,id);
            }
        });
        playbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                if (timer.getText().toString().length() > 0&&Integer.parseInt(timer.getText().toString())>0) {
                    try {
                        pausebtn.setVisibility(View.VISIBLE);
                        playbtn.setVisibility(View.INVISIBLE);
                        startService();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please select value", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pausebtn.setVisibility(View.INVISIBLE);
                playbtn.setVisibility(View.VISIBLE);
                PeriodChange();
                stopService();
                //tv_timer.setText("");
            }
        });


        myVM.getPeriodList().observe(this, new Observer<ArrayList<Period>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(ArrayList<Period> s) {
                    //stopService(intent);
                //timersSeconds.remove(0);
                if(s.size()==0 && db.getPeriods(id).size()!=0)
                    PeriodChange();
                else
                    PeriodChange(s);
                //    mEditor.putInt("secuence",-1).commit();
                  //  mEditor.putString("activeTimer","0").commit();
                if(playbtn.getVisibility()==View.INVISIBLE)
                {

                    stopService();
                    startService();}

            }
        });
      //  myVM.setLiveDataPeriodList(periods);
    }

    void PeriodChange(ArrayList<Period> s)
    {
        if(s!=periods) {
        periods.clear();
        periods.addAll(s);
        }

        timersSeconds.clear();
        SecondsofAll=0;

        secMassive = secMassive.substring(secMassive.indexOf('|')+1);
        for (Period p : periods)
        {
            timersSeconds.add(p.getSeconds());
            SecondsofAll+=p.getSeconds();
        }
        adapter.notifyDataSetChanged();
        try{
            timer.setText(String.valueOf(timersSeconds.get(0)));}
        catch (Exception e){timer.setText("0");}
    }
    void PeriodChange()
    {   periods.clear();
        periods.addAll(db.getPeriods(id));

        timersSeconds.clear();
        SecondsofAll=0;
      //  secMassive="";
        for (Period p : periods)
        {
            //secMassive+=String.valueOf(p.getSeconds())+"|";
            timersSeconds.add(p.getSeconds());
            SecondsofAll+=p.getSeconds();
        }
        myVM.setLiveDataPeriodList(periods);
       // adapter.notifyDataSetChanged();
        try{
            timer.setText(String.valueOf(timersSeconds.get(0)));}
        catch (Exception e){timer.setText("0");}
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {

            String str_time = mpref.getString("activeTimer","0");
            if(mpref.getString("secs","0").equals("finish"))
            {
                stopService();
                //mEditor.clear().commit();
                PeriodChange();
                pausebtn.setVisibility(View.INVISIBLE);
                playbtn.setVisibility(View.VISIBLE);
            }else{
                    if(mpref.getInt("secuence",0)>0) {
                        myVM.skipPeriod(context, periods.get(0));
                        mEditor.putInt("secuence",0).commit();
                    }

                timer.setText(str_time);
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startService()
    {

        setEditor();
        startService(intent);
    }
    private void pauseService(){

       // SecondsofAll= Integer.parseInt(timer.getText().toString());
      //  mEditor.putString("secs",""+SecondsofAll).commit();
        stopService(intent);
    }
    private void stopService(){
        try{
        timer.setText(String.valueOf(timersSeconds.get(0)));}catch (Exception e){timer.setText("0");}
        stopService(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setEditor(){
        mEditor.clear();
        secMassive ="";
        for(Period p : periods)
            secMassive+=String.valueOf(p.getSeconds())+"|";
        mEditor.putString("massive",secMassive).commit();
        mEditor.putInt("secuence",numberOfSecuence).commit();
        mEditor.putString("secs",""+SecondsofAll).commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        String str_time = mpref.getString("activeTimer","0");
        if(mpref.getString("secs","0").equals("finish"))
        {
            stopService();
            PeriodChange();
            //mEditor.putInt("secuence",0).commit();
            pausebtn.setVisibility(View.INVISIBLE);
            playbtn.setVisibility(View.VISIBLE);
        }else{
        for(int i=0;i<mpref.getInt("secuence",0);i++)
        {
            myVM.skipPeriod(this,periods.get(0));}
            mEditor.putInt("secuence",0).commit();
            timer.setText(str_time);
        }
        registerReceiver(broadcastReceiver,new IntentFilter(Timer_Service.str_receiver));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService();
    }
}














