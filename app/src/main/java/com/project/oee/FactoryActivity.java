package com.project.oee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class FactoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout listSwipeRefreshLayout,emptySwipeRefreshLayout;
    private ListView listView;
    private FactoryAdapter adapter;
    private List<Factory> factoryList;
    private SQLiteHandler db;
    private FloatingActionButton fab;

    private static final int REQUEST_ACTION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab_factory);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyStateContentTextView));

        listSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.listSwipeRefreshLayout);
        emptySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.emptySwipeRefreshLayout);

        factoryList = new ArrayList<>();
        adapter = new FactoryAdapter(this, factoryList);
        listView.setAdapter(adapter);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        // SQLite database handler
        db = new SQLiteHandler(FactoryActivity.this);

        listView.setLongClickable(true);
        listView.setEmptyView(emptySwipeRefreshLayout);

        // Configure SwipeRefreshLayout
        onCreateSwipeToRefresh(listSwipeRefreshLayout);
        onCreateSwipeToRefresh(emptySwipeRefreshLayout);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        listSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    factoryList.clear();
                    listSwipeRefreshLayout.setRefreshing(true);
                    fetchFact();
                }
            }
        );

        //onClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String idf = getFacList().get(position).getIdFactory();
                Intent intent = new Intent(FactoryActivity.this, RecordActivity.class);
                intent.putExtra("idf", idf);
                startActivityForResult(intent, REQUEST_ACTION);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(FactoryActivity.this, FileActivity.class);
                startActivity(intent);
            }
        });
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

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        factoryList.clear();
        fetchFact();
    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchFact() {
        db.getFacDetail();
            // Print the name from the list....
            for(Factory model : db.getListFac()) {
                factoryList.add(model);
            }
        adapter.notifyDataSetChanged();
        db.getListFac().clear();
        listSwipeRefreshLayout.setRefreshing(false);
    }

    //get list job data
    public List<Factory> getFacList() {
        return factoryList;
    }

}
