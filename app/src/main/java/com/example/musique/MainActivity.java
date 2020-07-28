package com.example.musique;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mySongList;
    String[] item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySongList = (ListView)findViewById(R.id.mySongListView);



        runtimePermission();
    }

    public void runtimePermission(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                                  @Override
                                  public void onPermissionGranted(PermissionGrantedResponse response) {
                                      display();

                                  }

                                  @Override
                                  public void onPermissionDenied(PermissionDeniedResponse response) {

                                  }

                                  @Override
                                  public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                                        token.continuePermissionRequest();
                                  }
                              }
                ).check();
    }

    public ArrayList<File> findMySong(File file){

        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singleFile: files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findMySong(singleFile));
            }
            else if (singleFile.getName().endsWith(".mp3")){
                arrayList.add(singleFile);
            }
        }
        return arrayList;
    }

    void display(){

        final ArrayList<File> mySongs = findMySong(Environment.getExternalStorageDirectory());
        item = new String[mySongs.size()];

        for(int i = 0;i<mySongs.size();i++){
            item[i] = mySongs.get(i).getName().toString().replace(".mp3","");
        }
        ArrayAdapter<String> theAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        mySongList.setAdapter(theAdapter);

        mySongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                String songname = mySongList.getItemAtPosition(i).toString();

                startActivity(new Intent(getApplicationContext(),PlayerLayout.class)
                .putExtra("songs",mySongs).putExtra("songname",songname)
                .putExtra("pos",i));
            }
        });
    }
}
