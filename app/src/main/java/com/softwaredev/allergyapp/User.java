package com.softwaredev.allergyapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class User {
    ArrayList<String> favoriteItemsBarcodes;
    ArrayList<String> favoriteItemsNames;
    int mSize;
    Context mContext;
    SharedPreferences sharedPref;

    User(Context context) {
        favoriteItemsBarcodes = new ArrayList<>();
        favoriteItemsNames = new ArrayList<>();
        mSize = favoriteItemsBarcodes.size();
        mContext = context;
        sharedPref = context.getSharedPreferences("com.softwaredav.allergyapp.user", Context.MODE_PRIVATE);

        mSize = sharedPref.getInt("size", 0);

        for (int i = 0; i < mSize; ++i)
        {
            favoriteItemsBarcodes.add(sharedPref.getString("favoriteBarcode" + i, ""));
            favoriteItemsNames.add(sharedPref.getString("favoriteName" + i, ""));
        }
    }

    public ArrayList<String> getFavoriteItemsBarcodes()
    {
        return favoriteItemsBarcodes;
    }

    public ArrayList<String> getFavoriteItemsNames()
    {
        return favoriteItemsNames;
    }

    public void addToFavoriteItems(String name, String barcode)
    {
        favoriteItemsNames.add(name);
        favoriteItemsBarcodes.add(barcode);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("favoriteName" + Integer.toString(favoriteItemsNames.size()), name);
        editor.putString("favoriteBarcode" + Integer.toString(favoriteItemsBarcodes.size()), barcode);
        editor.putInt("size", favoriteItemsBarcodes.size());
        editor.apply();
    }
}
