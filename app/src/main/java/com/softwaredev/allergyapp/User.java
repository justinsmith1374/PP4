package com.softwaredev.allergyapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class User {
    ArrayList<String> favoriteItemsBarcodes;
    ArrayList<String> favoriteItemsNames;
    static ArrayList<String> allergyList;
    int favoritesSize;
    int allergiesSize;
    Context mContext;
    SharedPreferences sharedPref;

    User(Context context) {
        favoriteItemsBarcodes = new ArrayList<>();
        favoriteItemsNames = new ArrayList<>();
        allergyList = new ArrayList<>();

        favoritesSize = favoriteItemsBarcodes.size();
        allergiesSize = allergyList.size();

        mContext = context;
        sharedPref = context.getSharedPreferences("com.softwaredev.allergyapp.user", Context.MODE_PRIVATE);

        favoritesSize = sharedPref.getInt("favoritesSize", 0);
        allergiesSize = sharedPref.getInt("allergiesSize", 0);

        for (int i = 1; i < favoritesSize + 1; ++i)
        {
            favoriteItemsBarcodes.add(sharedPref.getString("favoriteBarcode" + i, ""));
            favoriteItemsNames.add(sharedPref.getString("favoriteName" + i, ""));
        }

        for (int i = 1; i < allergiesSize + 1; ++i)
        {
            allergyList.add(sharedPref.getString("allergy" + i, ""));
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

    public ArrayList<String> getAllergyList()
    {
        return allergyList;
    }

    public void addToFavoriteItems(String name, String barcode)
    {
        favoriteItemsNames.add(name);
        favoriteItemsBarcodes.add(barcode);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("favoriteName" + Integer.toString(favoriteItemsNames.size()), name);
        editor.putString("favoriteBarcode" + Integer.toString(favoriteItemsBarcodes.size()), barcode);
        editor.putInt("favoritesSize", favoriteItemsBarcodes.size());
        editor.apply();
    }

    public void addToAllergies(String allergy)
    {
        allergyList.add(allergy);
        allergiesSize = allergyList.size();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("allergy" + Integer.toString(allergyList.size()), allergy);
        editor.putInt("allergiesSize", allergyList.size());
        editor.apply();
    }

    public void removeItemFromAllergy(int position)
    {
        if (position > -1) {
            allergyList.remove(position);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();

            for (int i = 1; i < allergyList.size() + 1; ++i) {
                editor.putString("allergy" + Integer.toString(i), allergyList.get(i - 1));
            }
            editor.putInt("allergiesSize", allergyList.size());
            editor.commit();
        }
    }
}
