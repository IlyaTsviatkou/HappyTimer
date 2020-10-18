package com.example.happytimer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

public class AddDialog extends Dialog{

   public ArrayAdapter<?> adapter1;
    private LogicVM myVM;
    public String colour;

    public AddDialog(@NonNull Context context) {
        super(context);
        myVM = ViewModelProviders.of((FragmentActivity) context).get(LogicVM.class);
        adapter1= ArrayAdapter.createFromResource(context, R.array.Colors, android.R.layout.simple_spinner_item);
    }

    public void showDialog(Activity activity, String msg){

    final Dialog dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.add_dialog);

    Button btn = (Button) dialog.findViewById(R.id.Button1);
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText txt = (EditText)dialog.findViewById(R.id.TextTitle) ;
            myVM.setList(getContext(),txt.getText().toString());
            dialog.dismiss();
        }
    });
    btn = (Button) dialog.findViewById(R.id.Button2);
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });
    dialog.show();

}
    public void showDialog(final Activity activity, final int timerId){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_period_dialog);
        Spinner spinner1 = dialog.findViewById(R.id.colour);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                colour=parent.getItemAtPosition(selectedItemPosition).toString();}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                colour="Green";
            }
        });
        spinner1.setAdapter(adapter1);
        Button btn = (Button) dialog.findViewById(R.id.Button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txt = (EditText)dialog.findViewById(R.id.TextTitle) ;
                EditText txt2 = (EditText)dialog.findViewById(R.id.TextSeconds) ;
                if(txt.getText().toString().equals("") || txt2.getText().toString().equals("")) {
                    Toast.makeText(activity.getApplicationContext(),
                            "Please enter title & seconds", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    try{
                    myVM.addPeriodList(getContext(),txt.getText().toString(), Integer.parseInt(txt2.getText().toString()),colour,timerId);
                    txt.setText("");
                    txt2.setText("");}
                    catch (Exception e)
                    {
                        Toast.makeText(activity.getApplicationContext(),
                                "Incorrect field of seconds", Toast.LENGTH_LONG).show();
                        txt2.setText("");
                        return;
                    }
                }
                //dialog.dismiss();
            }
        });
        btn = (Button) dialog.findViewById(R.id.Button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

}



