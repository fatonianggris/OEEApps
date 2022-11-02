package com.project.oee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class OEECalculation extends AppCompatActivity implements View.OnClickListener {

    private Vibrator vib;
    Animation animShake;
    private EditText inputEname, inputOtime, inputTtime, inputPamount, inputIcycle, inputGcount, inputTcount;
    private TextView ROee, RAvailability, RPeformance, RRoq, factoryName, personName, date, eqnm, otnm, ttnm, panm,ictnm, gcnm,tcnm;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab0,fab1,fab2,fab3;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private ShareController share;
    private ImageView image;
    private static final int REQUEST_OK = 100;
    private static final int REQUEST_COUNT = 200;

    private SQLiteHandler db;
    private SessionManager session;
    private static final String KEY_ID = "id_factory";
    private static final String KEY_FACTORY = "factory";
    private static final String KEY_USER = "username";
    private static final String KEY_DATE = "date";
    private String idF, pdtime, ltime, tdelay, whours;
    private String dtime ="";
    private String tdtime ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_oee);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_account_balance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab0 = (FloatingActionButton)findViewById(R.id.fab0);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        image = (ImageView) findViewById(R.id.oee_img);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab0.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        inputEname = (EditText) findViewById(R.id.eqipment_name);
        inputOtime = (EditText) findViewById(R.id.operating_time);
        inputTtime = (EditText) findViewById(R.id.total_time);
        inputPamount = (EditText) findViewById(R.id.processed_amount);
        inputIcycle = (EditText) findViewById(R.id.ideal_cycle_time);
        inputGcount = (EditText) findViewById(R.id.good_count);
        inputTcount = (EditText) findViewById(R.id.total_count);
        RAvailability = (TextView) findViewById(R.id.availability);
        RPeformance = (TextView) findViewById(R.id.peformance);
        RRoq = (TextView) findViewById(R.id.roq);
        ROee = (TextView) findViewById(R.id.oee);
        factoryName =(TextView) findViewById(R.id.fac);
        personName =(TextView) findViewById(R.id.person);
        date =(TextView) findViewById(R.id.dates);

        eqnm = (TextView) findViewById(R.id.eqnm);
        otnm = (TextView) findViewById(R.id.otnm);
        ttnm = (TextView) findViewById(R.id.ttnm);
        panm = (TextView) findViewById(R.id.panm);
        ictnm = (TextView) findViewById(R.id.ictnm);
        gcnm = (TextView) findViewById(R.id.gcnm);
        tcnm = (TextView) findViewById(R.id.tcnm);


        share = new ShareController(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());

        HashMap<String, String> fac = db.getFacDetailLast();
        String id = fac.get(KEY_ID);
        String fn = fac.get(KEY_FACTORY).toUpperCase();
        String p = fac.get(KEY_USER).substring(0, 1).toUpperCase() + fac.get(KEY_USER).substring(1);
        String d = fac.get(KEY_DATE);

        factoryName.setText(fn);
        personName.setText(p);
        date.setText(d);
        idF=id;

        // check session
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        inputOtime.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                changeColorAvail();
                changeColorOEE();
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        inputTtime.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                inputIcycle.setText("");
                changeColorAvail();
                changeColorOEE();
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        inputPamount.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                inputIcycle.setText("");
                changeColorPeform();
                changeColorOEE();
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        inputIcycle.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                changeColorPeform();
                changeColorOEE();
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        inputGcount.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                changeColorROQ();
                changeColorOEE();
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        inputTcount.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                changeColorROQ();
                changeColorOEE();
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        inputIcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationICT()){
                    ictCount();
                } else {
                    Toast.makeText(getApplicationContext(), "Please input PA, TT & Equip first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //dialog
        eqnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Equipment Name",R.string.eqnm_c,R.drawable.ic_machine);
            }
        });
        otnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Operation Time",R.string.otnm_c,R.drawable.ic_operating);
            }
        });
        ttnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Total Time",R.string.ttnm_c,R.drawable.ic_time);
            }
        });
        panm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Processed Amount",R.string.panm_c,R.drawable.ic_proses);
            }
        });
        ictnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Ideal Cycle Time",R.string.ictnm_c,R.drawable.ic_cycle);
            }
        });
        gcnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Good Count",R.string.gcnm_c,R.drawable.ic_good);
            }
        });
        tcnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("Total Count",R.string.tcnm_c,R.drawable.ic_sum);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab0:
                animateFAB();
                logoutDialog();
                break;
            case R.id.fab1:
                animateFAB();
                if(validationForm()){
                    addDtCal();
                } else {
                    Toast.makeText(getApplicationContext(), "Please input data correctly.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fab2:
                animateFAB();
                doHistory();
                break;
            case R.id.fab3:
                animateFAB();
                if(validationForm()){
                    shareTxt();
                } else {
                    Toast.makeText(getApplicationContext(), "Please input data first.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void doHistory(){
        Intent intent = new Intent(OEECalculation.this, FactoryActivity.class);
        startActivity(intent);
    }

    public void ictCount(){
        if(inputIcycle.getText().toString().equals("")||inputIcycle.getText().toString().isEmpty()){
            Intent intent = new Intent(OEECalculation.this, ICTCalculation.class);
            intent.putExtra("equip",inputEname.getText().toString());
            intent.putExtra("pa",inputPamount.getText().toString());
            intent.putExtra("tt",inputTtime.getText().toString());
            intent.putExtra("ict",inputIcycle.getText().toString());
            startActivityForResult(intent, REQUEST_COUNT);
        } else {
            Intent intent = new Intent(OEECalculation.this, ICTCalculation.class);
            intent.putExtra("equip",inputEname.getText().toString());
            intent.putExtra("pa",inputPamount.getText().toString());
            intent.putExtra("tt",inputTtime.getText().toString());
            intent.putExtra("ict",inputIcycle.getText().toString());
            intent.putExtra("dt",dtime.toString());
            intent.putExtra("tdt",tdtime.toString());
            intent.putExtra("pd", pdtime.toString());
            intent.putExtra("lt", ltime.toString());
            intent.putExtra("tdel", tdelay.toString());
            intent.putExtra("wh", whours.toString());
            intent.putExtra("ict", inputIcycle.toString());
            startActivityForResult(intent, REQUEST_COUNT);
        }

    }
    public void doAbout(){
        Intent intent = new Intent(OEECalculation.this, AboutUsActivity.class);
        startActivity(intent);
    }

    public void logoutDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Logout.");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want logout this project?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                logoutUser();
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,	int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab0.startAnimation(fab_close);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab0.setClickable(false);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab0.startAnimation(fab_open);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab0.setClickable(true);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;

        }
    }

    private String availabilityCount() {
        double result;
        double lastResult;
        String results;
        if(inputOtime.getText().toString().equals("") || inputOtime.getText().toString().equals(".")) {
            result = 0;
        } else {
            result = Double.parseDouble(inputOtime.getText().toString());
        }
        if(inputTtime.getText().toString().equals("") || inputTtime.getText().toString().equals(".")) {
            result = 0;
        } else {
            if(Double.parseDouble(inputTtime.getText().toString())>0) {
                result /= Double.parseDouble(inputTtime.getText().toString());
            } else {
                result = 0;
            }
        }
        result *=100;
        if(result>0 || result<0){
            lastResult = DecimalUtils.round(result, 2);
        }else {
            lastResult =0;
        }
        if (String.valueOf(lastResult).length() >= 5){
            results = String.valueOf(lastResult).substring(0,5);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    private String peformanceCount() {
        double result;
        double lastResult;
        String results;
        if(inputPamount.getText().toString().equals("") || inputPamount.getText().toString().equals(".")) {
            result = 0;
        } else {
            result = Double.parseDouble(inputPamount.getText().toString());
        }
        if(inputIcycle.getText().toString().equals("") || inputIcycle.getText().toString().equals(".")) {
            result = 0;
        } else {
            result *= Double.parseDouble(inputIcycle.getText().toString());
        }
        if(inputOtime.getText().toString().equals("") || inputOtime.getText().toString().equals(".")) {
            result = 0;
        } else {
            result /= Double.parseDouble(inputOtime.getText().toString());
        }
        result *=100;
        if(result>0 || result<0){
            lastResult = DecimalUtils.round(result, 2);
        }else {
            lastResult =0;
        }
        if (String.valueOf(lastResult).length() >= 5){
            results = String.valueOf(lastResult).substring(0,5);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    private String roqCount() {
        double result;
        double lastResult;
        String results;
        if(inputGcount.getText().toString().equals("") || inputGcount.getText().toString().equals(".")) {
            result = 0;
        } else {
            result = Double.parseDouble(inputGcount.getText().toString());
        }
        if(inputTcount.getText().toString().equals("") || inputTcount.getText().toString().equals(".")) {
            result = 0;
        } else {
            if(Double.parseDouble(inputTcount.getText().toString())>0) {
                result /= Double.parseDouble(inputTcount.getText().toString());
            } else {
                result = 0;
            }
        }
        result *=100;
        if(result>0 || result<0){
            lastResult = DecimalUtils.round(result, 2);
        }else {
            lastResult =0;
        }
        if (String.valueOf(lastResult).length() >= 5){
            results = String.valueOf(lastResult).substring(0,5);
        } else {
            results = String.valueOf(lastResult);
        }
        return results;
    }

    private String oeeCount() {
        double result;
        double result1;
        double result2;
        double result3;
        double lastResult;
        String results;
        if(RAvailability.getText().equals("-")) {
            result1= 0;
        } else {
            result1= Double.parseDouble(RAvailability.getText().toString())/100;
        }
        if(RPeformance.getText().equals("-") ) {
            result2= 0;
        } else {
            result2= Double.parseDouble(RPeformance.getText().toString())/100;
        }
        if(RRoq.getText().equals("-")) {
            result3= 0;
        } else {
            result3= Double.parseDouble(RRoq.getText().toString())/100;
        }
        result =result1*result2*result3*100;
        if(result>0 || result<0){
            lastResult = DecimalUtils.round(result, 3);
        }else {
            lastResult =0;
        }
        if (String.valueOf(lastResult).length() >= 5){
            results = String.valueOf(lastResult).substring(0,5);
        } else {
            results = String.valueOf(lastResult);
        }
        return results+"%";
    }

    public void addDtCal(){
        String Ename = inputEname.getText().toString();
        String Otime = inputOtime.getText().toString();
        String Ttime = inputTtime.getText().toString();
        String Pamount = inputPamount.getText().toString();
        String Icycle = inputIcycle.getText().toString();
        String Gcount = inputGcount.getText().toString();
        String Tcount = inputTcount.getText().toString();
        String Ava = RAvailability.getText().toString();
        String Per = RPeformance.getText().toString();
        String Roq = RRoq.getText().toString();
        String OEE = ROee.getText().toString();
        String dates = date.getText().toString();
        vib.vibrate(50);
        db.addCalculation(getApplicationContext(), idF, Ename, Otime, Ttime, Pamount, Icycle, Gcount, Tcount, Ava, Per, Roq, OEE, dtime,
                 tdtime, pdtime, ltime, tdelay, whours, dates);
     }

     public void shareTxt(){
         String Fname = factoryName.getText().toString();
         String Pname = personName.getText().toString();
         String Ename = inputEname.getText().toString();
         String Otime = inputOtime.getText().toString();
         String Ttime = inputTtime.getText().toString();
         String Pamount = inputPamount.getText().toString();
         String Icycle = inputIcycle.getText().toString();
         String Gcount = inputGcount.getText().toString();
         String Tcount = inputTcount.getText().toString();
         String Ava = RAvailability.getText().toString();
         String Per = RPeformance.getText().toString();
         String Roq = RRoq.getText().toString();
         String OEE = ROee.getText().toString();
         String dates = date.getText().toString();

         share.shareText(Fname, Pname, Ename, Otime, Ttime, Pamount, Icycle, Gcount, Tcount, Ava, Per, Roq, OEE, dtime,
                  tdtime, pdtime, ltime, tdelay, whours, dates);
     }


     private void changeColorOEE(){
         if(Double.parseDouble(oeeCount().replace("%",""))<85){
             ROee.setTextColor(Color.parseColor("#D50000"));
             image.setColorFilter(image.getContext().getResources().getColor(R.color.redw), PorterDuff.Mode.SRC_ATOP);
         } else if(Double.parseDouble(oeeCount().replace("%",""))>85 && Double.parseDouble(oeeCount().replace("%",""))<89.99){
             ROee.setTextColor(Color.parseColor("#FF6D00"));
             image.setColorFilter(image.getContext().getResources().getColor(R.color.yelw), PorterDuff.Mode.SRC_ATOP);
         } else {
             ROee.setTextColor(Color.parseColor("#64DD17"));
             image.setColorFilter(image.getContext().getResources().getColor(R.color.grew), PorterDuff.Mode.SRC_ATOP);
         }
         ROee.setText(oeeCount());
     }

    private void changeColorAvail(){
        if(Double.parseDouble(availabilityCount())<85){
            RAvailability.setTextColor(Color.parseColor("#D50000"));
        } else if(Double.parseDouble(availabilityCount())>85 && Double.parseDouble(availabilityCount())<89.99){
            RAvailability.setTextColor(Color.parseColor("#FF6D00"));
        } else {
            RAvailability.setTextColor(Color.parseColor("#64DD17"));
        }
        RAvailability.setText(availabilityCount());
    }

    private void changeColorPeform(){
        if(Double.parseDouble(peformanceCount())<85){
            RPeformance.setTextColor(Color.parseColor("#D50000"));
        } else if(Double.parseDouble(peformanceCount())>85 && Double.parseDouble(peformanceCount())<89.99){
            RPeformance.setTextColor(Color.parseColor("#FF6D00"));
        } else {
            RPeformance.setTextColor(Color.parseColor("#64DD17"));
        }
        RPeformance.setText(peformanceCount());
    }

    private void changeColorROQ(){
        if(Double.parseDouble(roqCount())<85){
            RRoq.setTextColor(Color.parseColor("#D50000"));
        } else if(Double.parseDouble(roqCount())>85 && Double.parseDouble(roqCount())<89.99){
            RRoq.setTextColor(Color.parseColor("#FF6D00"));
        } else {
            RRoq.setTextColor(Color.parseColor("#64DD17"));
        }
        RRoq.setText(roqCount());
    }

    /**
     * Validating form
     */
    private boolean validationForm() {
        boolean set = true;

        if (inputEname.getText().toString().trim().isEmpty()) {
            //set error
            inputEname.setError("Input machine or equipment");
            //set anim
            inputEname.setAnimation(animShake);
            inputEname.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputEname.clearAnimation();
        }
        //sisi jalan
        if (inputOtime.getText().toString().trim().isEmpty()) {
            //set error
            inputOtime.setError("Input operation time");
            //set anim
            inputOtime.setAnimation(animShake);
            inputOtime.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputOtime.clearAnimation();
        }
        if (inputTtime.getText().toString().trim().isEmpty()) {
            //set error
            inputTtime.setError("Input total time");
            //set anim
            inputTtime.setAnimation(animShake);
            inputTtime.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputTtime.clearAnimation();
        }
        if (inputPamount.getText().toString().trim().isEmpty()) {
            //set error
            inputPamount.setError("Input process amount");
            //set anim
            inputPamount.setAnimation(animShake);
            inputPamount.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputPamount.clearAnimation();
        }
        if (inputIcycle.getText().toString().trim().isEmpty()) {
            //set error
            inputIcycle.setError("Input ideal cycle");
            //set anim
            inputIcycle.setAnimation(animShake);
            inputIcycle.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputIcycle.clearAnimation();
        }
        if (inputGcount.getText().toString().trim().isEmpty()) {
            //set error
            inputGcount.setError("Input good count");
            //set anim
            inputGcount.setAnimation(animShake);
            inputGcount.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputGcount.clearAnimation();
        }
        if (inputTcount.getText().toString().trim().isEmpty()) {
            //set error
            inputTcount.setError("Input total count");
            //set anim
            inputTcount.setAnimation(animShake);
            inputTcount.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputTcount.clearAnimation();
        }
        return set;
    }

    private boolean validationICT() {
        boolean set = true;

        if (inputEname.getText().toString().trim().isEmpty()) {
            //set error
            inputEname.setError("Input machine or equipment");
            //set anim
            inputEname.setAnimation(animShake);
            inputEname.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputEname.clearAnimation();
        }
        if (inputTtime.getText().toString().trim().isEmpty()) {
            //set error
            inputTtime.setError("Input total time");
            //set anim
            inputTtime.setAnimation(animShake);
            inputTtime.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputTtime.clearAnimation();
        }
        if (inputPamount.getText().toString().trim().isEmpty()) {
            //set error
            inputPamount.setError("Input process amount");
            //set anim
            inputPamount.setAnimation(animShake);
            inputPamount.startAnimation(animShake);
            vib.vibrate(120);
            set = false;
        } else {
            inputPamount.clearAnimation();
        }
        return set;
    }


    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        startActivity(new Intent(OEECalculation.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the result is capturing
        if (requestCode == REQUEST_COUNT) {
            if (resultCode == REQUEST_OK) {
                // set text lat/long
                String ict = data.getStringExtra("ICTResult");
                dtime = data.getStringExtra("downtime");
                pdtime = data.getStringExtra("pdowntime");
                tdtime = data.getStringExtra("tdowntime");
                ltime = data.getStringExtra("ltime");
                tdelay = data.getStringExtra("tdelay");
                whours = data.getStringExtra("workhours");
                inputIcycle.setText(ict.toString());
                changeColorPeform();
                changeColorOEE();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "not counting ICT", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            // failed to record video
            Toast.makeText(getApplicationContext(),
                    "Sorry! something wrong", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void DialogForm(String title, int content, int img) {
        AlertDialog.Builder  dialog = new AlertDialog.Builder(OEECalculation.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                doAbout();
            case R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}