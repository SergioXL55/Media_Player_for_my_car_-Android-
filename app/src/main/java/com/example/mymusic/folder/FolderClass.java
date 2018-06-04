package com.example.mymusic.folder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.adapter.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class FolderClass {

    public static ArrayList<Product> products = new ArrayList<>();
    private final String FILEFILTER = ".mp3";

    public int createList(String dir,String fileName) {
        int id =0;
        MainActivity.myMysicList.clear();
        products.clear();
        File imagesPath = new File(dir);
        File[] images = imagesPath.listFiles();
        for (File i : images) {
            if (i.isDirectory())
                products.add(new Product(i.getPath(), R.drawable.fold, "0"));
            else if (getType(i.getName()) == 1) {
                products.add(new Product(i.getPath(), R.drawable.playgreen, "1"));
                MainActivity.myMysicList.add(i.getPath());
                if(i.getName().equals(fileName)) id =MainActivity.myMysicList.size()-1;
            }
        }
        Collections.sort(products);
        return id;
    }

    private int getType(String mystr) {
        String sub = mystr.substring(mystr.length() - 4);
        if (sub.equals(FILEFILTER)) return 1;
        else return -1;
    }

    public int findId(String source) {
        //TODO пока что ничего умнее не придумал
        int id = -1;
        for (int i = 0; i < MainActivity.myMysicList.size(); i++)
            if (MainActivity.myMysicList.get(i).equals(source)) {
                id = i;
                break;
            }
        return id;
    }

    @NonNull
    public static String getFileName(String path) {
        File fileName = new File(path);
        return fileName.getName();
    }

    @NonNull
    public static String getFolderName(String path) {
        File inputPath = new File(path);
        File fileName = new File(inputPath.getParent());
        return fileName.getName();
    }

    public static void fileError(Context c) {
        Toast ts = Toast.makeText(c, "Нет файла для воспроизведения. Выберите новый", Toast.LENGTH_SHORT);
        ts.show();
    }

    //TODO переделать на автоматический поиск музыки на устройстве
    public void getRealPathFromURI(Context con) {
        String[] STAR = {"*"};
        Uri allaudiosong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String audioselection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor;
        cursor = con.getContentResolver().query(allaudiosong, STAR, audioselection, null, null);

        if (cursor != null)
            if (cursor.moveToFirst())
                do {
                    String song_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    Log.d("My", "Audio Song Name= " + song_name);

                    int song_id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    Log.d("My", "Audio Song Name= " + song_id);

                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Log.d("My", "Audio Song Name= " + fullpath);

                    File track = new File(fullpath);
                    Log.d("My", "Audio Parent= " + track.getParent());
                    File foldername = new File(track.getParent());
                    Log.d("My", "Audio Parent= " + foldername.getName());

                } while (cursor.moveToNext());
        cursor.close();
    }
}
