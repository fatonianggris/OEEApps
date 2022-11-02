package com.project.oee;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout listSwipeRefreshLayout,emptySwipeRefreshLayout;
    private ListView listView;
    private FileAdapter adapter;
    private List<Files> fileList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.emptyStateContentTextView));

        listSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.listSwipeRefreshLayout);
        emptySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.emptySwipeRefreshLayout);

        fileList = new ArrayList<>();
        adapter = new FileAdapter(this, fileList);
        listView.setAdapter(adapter);

        listView.setLongClickable(true);
        listView.setEmptyView(emptySwipeRefreshLayout);

        // Configure SwipeRefreshLayout
        onCreateSwipeToRefresh(listSwipeRefreshLayout);
        onCreateSwipeToRefresh(emptySwipeRefreshLayout);

        listSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        fileList.clear();
                        listSwipeRefreshLayout.setRefreshing(true);
                        listDir();
                    }
                }
        );
        //onClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String nmf = fileList.get(position).getNameFile();
                openFile(nmf);
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

    @Override
    public void onRefresh() {
        listDir();
    }

    private void listDir() {
        File sd = new File(Environment.getExternalStorageDirectory() + File.separator + "OEECalculator");
        File root = new File(sd.getAbsolutePath());
        File[] files = root.listFiles();
        fileList.clear();
        for (File fl : files) {
            Files fs = new Files(fl.getName(),String.valueOf(fl.length()),"",fl.getPath());
            Date ls = new Date(fl.lastModified());
            fs.setDate(ls.toString());
            fileList.add(fs);
        }
        adapter.notifyDataSetChanged();
        listSwipeRefreshLayout.setRefreshing(false);
    }

    private void openFile(String filename){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/OEECalculator/", filename);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent1 = Intent.createChooser(intent, "Open With");
        try {
            startActivity(intent1);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    public void delFile(String filename){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/OEECalculator/", filename);
        boolean deleted = file.delete();
        if(deleted){
            Toast.makeText(getApplicationContext(), filename+" has been deleted" , Toast.LENGTH_LONG).show();
        }
    }

}
