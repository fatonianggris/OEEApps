package com.project.oee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class ICTCalculation extends AppCompatActivity  {

    private Vibrator vib;
    Animation animShake;
    private EditText downTime, planDowntime, totalDowntime, loadingTime, totalDelay, workingHours;
    private TextView mach, pa, tt, dnm, pdnm, tdnm, ltnm, tdlnm, whnm;
    private String ict;

    private static final int REQUEST_OK = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ict_calculation);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        downTime = (EditText) findViewById(R.id.downtime_time);
        planDowntime = (EditText) findViewById(R.id.plan_down_time);
        totalDowntime = (EditText) findViewById(R.id.total_down_time);
        loadingTime = (EditText) findViewById(R.id.loading_time);
        totalDelay = (EditText) findViewById(R.id.total_delay);
        workingHours = (EditText) findViewById(R.id.working_hours);

        dnm =(TextView) findViewById(R.id.dnm);
        pdnm =(TextView) findViewById(R.id.pdnm);
        tdnm =(TextView) findViewById(R.id.tdnm);
        ltnm =(TextView) findViewById(R.id.ltnm);
        tdlnm =(TextView) findViewById(R.id.tdlnm);
        whnm = (TextView) findViewById(R.id.whnm);

        mach =(TextView) findViewById(R.id.machine);
        pa =(TextView) findViewById(R.id.pamount);
        tt =(TextView) findViewById(R.id.ttime);

        Intent intent = getIntent();
        String Equip = intent.getStringExtra("equip");
        String Pamount = intent.getStringExtra("pa");
        String Ttime = intent.getStringExtra("tt");
        String ict = intent.getStringExtra("ict");
        String dt = intent.getStringExtra("dt");
        String tdt = intent.getStringExtra("tdt");
        String pd = intent.getStringExtra("pd");
        String lt = intent.getStringExtra("lt");
        String tdel = intent.getStringExtra("tdel");
        String wh = intent.getStringExtra("wh");

        if(ict.toString().equals("") || ict.toString().isEmpty()) {
            mach.setText(Equip);
            pa.setText(Pamount);
            tt.setText(Ttime);
        } else {
            mach.setText(Equip);
            pa.setText(Pamount);
            tt.setText(Ttime);
            downTime.setText(dt.toString());
            totalDowntime.setText(tdt.toString());
            planDowntime.setText(pd.toString());
            loadingTime.setText(lt.toString());
            totalDelay.setText(tdel.toString());
            workingHours.setText(wh.toString());
        }

        LinearLayout count = (LinearLayout) findViewById (R.id.count);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationForm()){
                    sendICT();
                } else {
                    Toast.makeText(getApplicationContext(), "Please input data first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        downTime.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                planDowntime.setText(planDowntime());
                totalDelay.setText(totalDelay());
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        totalDowntime.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                totalDelay.setText(totalDelay());
                workingHours.setText(workingHours());
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        planDowntime.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                loadingTime.setText(loadingTime());
                workingHours.setText(workingHours());
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        //dialog
        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Processed Amount",R.string.panm_c,R.drawable.ic_proses);
            }
        });

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Total Time",R.string.ttnm_c,R.drawable.ic_time);
            }
        });

        dnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("% Downtime",R.string.dnm_c,R.drawable.ic_down);
            }
        });

        pdnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Planned Downtime",R.string.pdnm_c,R.drawable.ic_plan);
            }
        });

        tdnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Total Downtime",R.string.tdnm_c,R.drawable.ic_sum);
            }
        });
        ltnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Loading Downtime",R.string.ltnm_c,R.drawable.ic_operating);
            }
        });
        tdlnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Total Delay",R.string.tdlnm_c,R.drawable.ic_sum);
            }
        });
        whnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("% Working Hours",R.string.whnm_c,R.drawable.ic_time);
            }
        });

    }

    private String planDowntime(){
        double result;
        double lastResult;
        String results;
        if(downTime.getText().toString().equals("") || downTime.getText().toString().equals(".")) {
            result = 0;
        } else {
            result= Double.parseDouble(tt.getText().toString())*Double.parseDouble(downTime.getText().toString())/100;
        }
        lastResult = DecimalUtils.round(result, 2);
        if (String.valueOf(lastResult).length() >= 7){
            results = String.valueOf(lastResult).substring(0,7);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    private String totalDelay(){
        double result;
        double lastResult;
        String results;
        if(totalDowntime.getText().toString().equals("") || totalDowntime.getText().toString().equals(".")) {
            result = 0;
        } else {
            result = Double.parseDouble(totalDowntime.getText().toString());
        }
        if(planDowntime.getText().toString().equals("") || planDowntime.getText().toString().equals(".")) {
            result = 0;
        } else {
            result+= Double.parseDouble(planDowntime.getText().toString());
        }
        lastResult = DecimalUtils.round(result, 2);
        if (String.valueOf(lastResult).length() >= 7){
            results = String.valueOf(lastResult).substring(0,7);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    private String ICTResult(){
        double result;
        double lastResult;
        String results;
        if(loadingTime.getText().toString().equals("") || loadingTime.getText().toString().equals(".")) {
            result = 0;
        } else {
            result = Double.parseDouble(loadingTime.getText().toString())/Double.parseDouble(pa.getText().toString());
        }
        if(workingHours.getText().toString().equals("") || workingHours.getText().toString().equals(".")) {
            result = 0;
        } else {
            result*= Double.parseDouble(workingHours.getText().toString());
            result/=100;
        }
        lastResult = DecimalUtils.round(result, 8);
        if (String.valueOf(lastResult).length() >= 10){
            results = String.valueOf(lastResult).substring(0,10);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    private void sendICT(){
        ict=ICTResult();
        Intent ictIntent = new Intent(ICTCalculation.this, OEECalculation.class);
        ictIntent.putExtra("downtime", downTime.getText().toString());
        ictIntent.putExtra("pdowntime", planDowntime.getText().toString());
        ictIntent.putExtra("tdowntime", totalDowntime.getText().toString());
        ictIntent.putExtra("ltime", loadingTime.getText().toString());
        ictIntent.putExtra("tdelay", totalDelay.getText().toString());
        ictIntent.putExtra("workhours", workingHours.getText().toString());
        ictIntent.putExtra("ICTResult",ict.toString());
        setResult(REQUEST_OK, ictIntent);
        finish();
    }

    private String workingHours(){
        double result;
        double lastResult;
        String results;
        if(totalDelay.getText().toString().equals("") || totalDelay.getText().toString().equals(".")) {
            result = 0;
        } else {
            result= (1-(Double.parseDouble(totalDelay.getText().toString())/Double.parseDouble(tt.getText().toString())))*100;
        }
        lastResult = DecimalUtils.round(result, 2);
        if (String.valueOf(lastResult).length() >= 7){
            results = String.valueOf(lastResult).substring(0,7);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    private String loadingTime(){
        double result;
        double lastResult;
        String results;
        if(planDowntime.getText().toString().equals("") || planDowntime.getText().toString().equals(".")) {
            result = 0;
        } else {
            result= Double.parseDouble(tt.getText().toString())-Double.parseDouble(planDowntime.getText().toString());
        }
        lastResult = DecimalUtils.round(result, 2);
        if (String.valueOf(lastResult).length() >= 7){
            results = String.valueOf(lastResult).substring(0,7);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    /**
     * Validating form
     */
    private boolean validationForm() {
        boolean set = true;

        if (downTime.getText().toString().trim().isEmpty()) {
            //set error
            downTime.setError("Input machine or equipment");
            //set anim
            downTime.setAnimation(animShake);
            downTime.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            downTime.clearAnimation();
        }
        //sisi jalan
        if (planDowntime.getText().toString().trim().isEmpty()) {
            //set error
            planDowntime.setError("Input operation time");
            //set anim
            planDowntime.setAnimation(animShake);
            planDowntime.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            planDowntime.clearAnimation();
        }
        if (totalDowntime.getText().toString().trim().isEmpty()) {
            //set error
            totalDowntime.setError("Input total time");
            //set anim
            totalDowntime.setAnimation(animShake);
            totalDowntime.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            totalDowntime.clearAnimation();
        }
        if (loadingTime.getText().toString().trim().isEmpty()) {
            //set error
            loadingTime.setError("Input process amount");
            //set anim
            loadingTime.setAnimation(animShake);
            loadingTime.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            loadingTime.clearAnimation();
        }
        if (totalDelay.getText().toString().trim().isEmpty()) {
            //set error
            totalDelay.setError("Input ideal cycle");
            //set anim
            totalDelay.setAnimation(animShake);
            totalDelay.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            totalDelay.clearAnimation();
        }
        if (workingHours.getText().toString().trim().isEmpty()) {
            //set error
            workingHours.setError("Input good count");
            //set anim
            workingHours.setAnimation(animShake);
            workingHours.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            workingHours.clearAnimation();
        }
        return set;
    }

    private void DialogForm(String title, int content, int img) {
        AlertDialog.Builder  dialog = new AlertDialog.Builder(ICTCalculation.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        TextView dnm  = (TextView) dialogView.findViewById(R.id.dialog_nm);
        TextView dct  = (TextView) dialogView.findViewById(R.id.dialog_ct);
        ImageView dimg =(ImageView) dialogView.findViewById(R.id.dialog_pic);

        dnm.setText(title);
        dct.setText(content);
        dimg.setImageResource(img);


        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}