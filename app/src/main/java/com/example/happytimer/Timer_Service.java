package com.example.happytimer;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Timer_Service extends Service {

    public static String str_receiver = "com.example.myapplication.receiver";

    private Handler mHandler = new Handler();
    private SoundPool mSoundPool;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    private int mStreamId;

    private Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 1000;
    Intent intent;
    int secs,activeTimer,secuence;
    String massive;
   // Context context;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mSoundPool.load(this, R.raw.sound, 1);

        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 1, NOTIFY_INTERVAL);
        intent = new Intent(str_receiver);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        secs= Integer.parseInt(mpref.getString("secs","0"));
        massive = mpref.getString("massive",null);
        secuence = mpref.getInt("secuence",0);
        activeTimer = Integer.parseInt(massive.substring(0,massive.indexOf('|')));
        massive = massive.substring(massive.indexOf('|')+1);
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    if(secs==0) {
                        mEditor.putString("secs","finish").commit();
                        Toast.makeText( getApplicationContext(), "FINISHED",
                                Toast.LENGTH_SHORT).show();
                        mTimer.cancel();
                       // sendBroadcast(intent);
                    }else if(activeTimer<=0)
                    {
                        Toast.makeText( getApplicationContext(), "started next",
                                Toast.LENGTH_SHORT).show();
                        activeTimer = Integer.parseInt(massive.substring(0,massive.indexOf('|')));
                        massive = massive.substring(massive.indexOf('|')+1);
                        secuence=mpref.getInt("secuence",0);
                        secuence++;
                        mEditor.putInt("secuence",secuence);



                    }
                    else {

                        // mEditor.putString("secs",String.valueOf(a));

                        mEditor.putString("secs", "" + secs).commit();
                        mEditor.putString("activeTimer", "" + activeTimer).commit();

                        if (activeTimer <=3 && activeTimer!=0) {

                            /*
                            Toast.makeText(getApplicationContext(), "Таймер тикает: " +
                                            activeTimer,
                                    Toast.LENGTH_SHORT).show();*/
                            mStreamId = mSoundPool.play(1,1,1,0,0,1);

                        }
                    }
                    secs--;
                    activeTimer--;
                    sendBroadcast(intent);

                }

            });
        }

    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();

    }

}
