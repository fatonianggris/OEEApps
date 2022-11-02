package com.project.oee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RecordActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout listSwipeRefreshLayout, emptySwipeRefreshLayout;
    private ListView listView;
    private RecordAdapter adapter;
    private List<Record> recordList;
    private SQLiteHandler db;
    private String idF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        listView = (ListView) findViewById(R.id.listView);

        listSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.listSwipeRefreshLayout);
        emptySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.emptySwipeRefreshLayout);

        recordList = new ArrayList<>();
        adapter = new RecordAdapter(this, recordList,listSwipeRefreshLayout);
        listView.setAdapter(adapter);

        // SQLite database handler
        db = new SQLiteHandler(RecordActivity.this);

        listView.setEmptyView(emptySwipeRefreshLayout);

        // Configure SwipeRefreshLayout
        onCreateSwipeToRefresh(listSwipeRefreshLayout);
        onCreateSwipeToRefresh(emptySwipeRefreshLayout);

        Intent get = getIntent();
        idF = get.getStringExtra("idf");
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        listSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    recordList.clear();
                    listSwipeRefreshLayout.setRefreshing(true);
                    fetchMovies(idF);
                }
            }
        );
    }

    @SuppressLint("ResourceAsColor")
    private void onCreateSwipeToRefresh(SwipeRefreshLayout refreshLayout) {

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorScheme(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onRefresh() {
        recordList.clear();
        fetchMovies(idF);
    }
    /**
     * Fetching movies json by making http call
     */
    private void fetchMovies(String idfac) {
        db.getCalDetail(idfac);
        // Print the name from the list....
            for(Record model : db.getListRec()) {
            recordList.add(model);
        }
        adapter.notifyDataSetChanged();
        db.getListRec().clear();
        listSwipeRefreshLayout.setRefreshing(false);
    }

}
