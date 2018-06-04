package com.example.mymusic.adapter;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Я on 31.05.2018.
 */

public class Product implements Comparable<Product> {

    public String name;
    int image;
    public String type;
    public String path;

    public Product(String _describe, int _image,String _type,String _path) {
        name = _describe;
        image = _image;
        type=_type;
        path=_path;
    }

    @Override
    public int compareTo(@NonNull Product product) {
        return type.compareTo(product.type);
    }
}