package com.example.mymusic.folder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.example.mymusic.adapter.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class FolderClass {

    public static ArrayList<Product> products = new ArrayList<>();
    private final static String FILEFILTER = ".mp3";
    private final static String FOLDER_TYPE = "0", FILE_TYPE = "1";

    public int createList(String dir,String fileName) {
        int id =0;
        MainActivity.myMysicList.clear();
        products.clear();
        File imagesPath = new File(dir);
        File[] images = imagesPath.listFiles();
        for (File i : images) {
            if (i.isDirectory())
                products.add(new Product(i.getPath(), R.drawable.fold, FOLDER_TYPE));
            else if (getType(i.getName()) == 1) {
                products.add(new Product(i.getPath(), R.drawable.playgreen, FILE_TYPE));
                MainActivity.myMysicList.add(i.getPath());
                if(i.getName().equals(fileName)) id =MainActivity.myMysicList.size()-1;
            }
        }
        Collections.sort(products);
        return id;
    }

    private void createFolderList(String inputFile) {
        boolean needAddFolder = true;
        if (products.size() == 0)
            products.add(new Product(inputFile, R.drawable.fold, FOLDER_TYPE));
        else {
            for (Product p : products)
                if (p.path.equals(inputFile)) {
                    needAddFolder = false;
                    break;
                }
            if (needAddFolder) products.add(new Product(inputFile, R.drawable.fold, FOLDER_TYPE));
        }
    }

    public void getRealPathFromURI(Context con) {
        products.clear();
        Uri allaudiosong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String audioselection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = con.getContentResolver().query(allaudiosong, new String[]{"*"}, audioselection, null, null);
        if (cursor != null)
            if (cursor.moveToFirst())
                do {
                    String fullpath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    File track = new File(fullpath);
                    createFolderList(track.getParent());
                } while (cursor.moveToNext());
        cursor.close();
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

}
