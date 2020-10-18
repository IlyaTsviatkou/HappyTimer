package com.example.happytimer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.sql.Time;
import java.util.ArrayList;


public class LogicVM extends ViewModel {
    private final MutableLiveData<ArrayList<Timer>> liveDataList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Period>> liveDataPeriodList = new MutableLiveData<>();

    public LiveData<ArrayList<Timer>> getList(){
        return liveDataList;
    }

    public LiveData<ArrayList<Period>> getPeriodList(){

        return liveDataPeriodList;
    }

    public void addPeriodList(Context context, String t,int s,String colour, int tId) {
        DataBaseHelper db = new DataBaseHelper(context);

        String title = t;
        int seconds = s;
        int timerId = tId;


        Period period = new Period(title,seconds,colour,timerId);
        db.addPeriod(period);
        ArrayList<Period> b=db.getPeriods(timerId);
        liveDataPeriodList.postValue(b);
    }

    public void setLiveDataPeriodList(ArrayList<Period> periods){
        liveDataPeriodList.postValue(periods);
    }

    public void setList(Context context,String title) {
        DataBaseHelper db = new DataBaseHelper(context);
        addDbTimer(context,title);
        ArrayList<Timer>  b=db.getAllTimers();
        liveDataList.postValue(b);
    }

    public void deleteTimer(Context context,Timer timer) {
        DataBaseHelper db = new DataBaseHelper(context);
        db.deleteTimer(timer);
        ArrayList<Timer> b=db.getAllTimers();
        liveDataList.postValue(b);
    }

    public void addDbTimer(Context context,String t){
        DataBaseHelper db = new DataBaseHelper(context);

        String title = t;


        if(title.equals("")) {
            Toast.makeText(context.getApplicationContext(),
                    "Please enter title", Toast.LENGTH_LONG).show();
            return;
        }

        Timer timer = new Timer(title);
        db.addTimer(timer);
    }

    public void skipPeriod(Context context,Period period) {
        try{
        ArrayList<Period> b = liveDataPeriodList.getValue();
        if(b==null)
        {
            DataBaseHelper db = new DataBaseHelper(context);
            b = db.getPeriods(period.getTimerID());
            for(Period p : b)
            {
                if(period.getId()==p.getId())
                    period = p;
            }
        }
        b.remove(period);
        liveDataPeriodList.postValue(b);}catch (Exception e){}
    }

    public void deletePeriod(Context  context,Period period) {
        DataBaseHelper db = new DataBaseHelper(context);
        int id = period.getTimerID();
        db.deletePeriod(period);
        ArrayList<Period> b = db.getPeriods(id);
        liveDataPeriodList.postValue(b);
    }
}
