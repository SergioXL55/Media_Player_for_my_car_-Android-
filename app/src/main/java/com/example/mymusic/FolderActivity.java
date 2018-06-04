package com.example.mymusic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mymusic.adapter.BoxAdapter;
import com.example.mymusic.adapter.Product;
import com.example.mymusic.folder.FolderClass;

import java.io.File;
import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {

    BoxAdapter boxAdapter;
    ListView lvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final FolderClass directoryList=new FolderClass();
        directoryList.createList(getString(R.string.defaultDirectory),getString(R.string.defaultFolder));
        boxAdapter = new BoxAdapter(this, FolderClass.products);

        lvMain = (ListView) findViewById(R.id.listFolder);
        lvMain.setAdapter(boxAdapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (FolderClass.products.get(i).type) {
                    case ("1"):
                        Intent in = new Intent();
                        in.putExtra("id", directoryList.findId(FolderClass.products.get(i).name));
                        setResult(RESULT_OK, in);
                        finish();
                        break;
                    case ("0"):
                        directoryList.createList(FolderClass.products.get(i).path,FolderClass.products.get(i).name);
                        boxAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }




}

