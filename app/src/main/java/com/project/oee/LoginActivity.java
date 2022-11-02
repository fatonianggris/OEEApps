package com.project.oee;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class LoginActivity extends AppCompatActivity {

    BlurView blurView;
    private EditText inputFname, inputPname, inputDate;
    private DatePickerFragment date;
    private SessionManager session;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        blurView = (BlurView)findViewById(R.id.blurView);
        inputFname = (EditText) findViewById(R.id.factory_name);
        inputPname = (EditText) findViewById(R.id.person_name);
        inputDate = (EditText) findViewById(R.id.date);
        date = new DatePickerFragment(LoginActivity.this, R.id.date);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final float radius = 2f;
        final View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout
        final View rootView = decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(this, true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);

        // SQLite database handler
        db = new SQLiteHandler(LoginActivity.this);
        // Session manager
        session = new SessionManager(getApplicationContext());

        LinearLayout proceed = (LinearLayout) findViewById (R.id.proceed);
        LinearLayout list = (LinearLayout) findViewById (R.id.list);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String factory = inputFname.getText().toString().trim();
                String username = inputPname.getText().toString().trim();
                String date = inputDate.getText().toString().trim();

                // Check for empty data in the form
                if (!factory.isEmpty() && !username.isEmpty() && !date.isEmpty()) {
                    // login user
                    db.addFactory(pDialog, session, factory, username, date);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),"Please fill content first!", Toast.LENGTH_LONG).show();
                }
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FactoryActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.cancel();
        }
    }
}
