package com.example.mymusic.adapter;

import android.support.annotation.NonNull;

public class Product implements Comparable<Product> {

    int image;
    public String type;
    public String path;

    public Product(String _path, int _image,String _type) {

        image = _image;
        type=_type;
        path=_path;
    }

    @Override
    public int compareTo(@NonNull Product product) {
        return type.compareTo(product.type);
    }
}