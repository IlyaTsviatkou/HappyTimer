package com.example.happytimer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Fragment_List extends Fragment {
    ListView timerList;
    ArrayList<Timer> timers2 = new ArrayList<Timer>();

    TimerAdapter timerAdapter;
    Intent intent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_list,null);

        DataBaseHelper db = new DataBaseHelper(getActivity());
        timers2 = db.getAllTimers();
        timerList =(ListView)v.findViewById(R.id.timerList);
        timerAdapter = new TimerAdapter(getActivity(),R.layout.timer_listview,timers2,getActivity());
        //arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.timer_listview,R.id.textView,timers);
        timerList.setAdapter(timerAdapter);
        timerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // TextView text = view.findViewById(R.id.textView);
       // TextView text2 = view.findViewById(R.id.textView2);
        LogicVM myVM = ViewModelProviders.of(getActivity()).get(LogicVM.class);
        myVM.getList().observe(requireActivity(), new Observer<ArrayList<Timer>>() {
            @Override
            public void onChanged(ArrayList<Timer> s) {
                timers2.clear();
                timers2.addAll(s);
                timerAdapter.notifyDataSetChanged();
                //timerList.setAdapter(arrayAdapter);
            }
        });
        //myVM.getConverted().observe(requireActivity(),s->text2.setText(s));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


}
