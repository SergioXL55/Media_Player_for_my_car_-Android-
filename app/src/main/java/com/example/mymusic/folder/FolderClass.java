package com.example.mymusic.folder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mymusic.MainActivity;
import com.example.mymusic.MusicList;
import com.example.mymusic.R;
import com.example.mymusic.adapter.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Я on 01.06.2018.
 */

public class FolderClass {

    public static ArrayList<Product> products = new ArrayList<Product>();
    final String FILEFILTER = ".mp3";

    public void createList(String dir, String folderName) {
        MainActivity.myMysicList.clear();
        products.clear();
        File imagesPath = new File(dir);
        File[] images = imagesPath.listFiles();
        for (File i : images) {
            if (i.isDirectory())
                products.add(new Product(i.getName(), R.drawable.fold, "0", i.getPath()));
            else if (getType(i.getName()) == 1) {
                products.add(new Product(i.getName(), R.drawable.playgreen, "1", i.getPath()));
                MainActivity.myMysicList.add(new MusicList(i.getName(), i.getPath(), folderName));
            }
        }
        Collections.sort(products);
    }

    private int getType(String mystr) {
        String sub = mystr.substring(mystr.length() - 4);
        if (sub.equals(FILEFILTER)) return 1;
        else return -1;
    }

    public int findId(String source) {
        int id = -1;
        for (int i = 0; i < MainActivity.myMysicList.size(); i++)
            if (MainActivity.myMysicList.get(i).name.equals(source)) {
                id = i;
                break;
            }
        return id;
    }

    //TODO переделать на автоматический поиск музыки на устройстве
    public void getRealPathFromURI(Context con) {
        String[] STAR = { "*" };
        Uri allaudiosong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String audioselection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor;
        cursor = con.getContentResolver().query(allaudiosong, STAR, audioselection, null, null);

        if (cursor != null)
            if (cursor.moveToFirst())
                do {
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    Log.d("My","Audio Song Name= "+song_name);

                    int song_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    Log.d("My","Audio Song Name= "+song_id);

                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Log.d("My","Audio Song Name= "+fullpath);

                    File track=new File(fullpath);
                    Log.d("My","Audio Parent= "+track.getParent());
                    File foldername=new File(track.getParent());
                    Log.d("My","Audio Parent= "+foldername.getName());

                } while (cursor.moveToNext());
    }
}
