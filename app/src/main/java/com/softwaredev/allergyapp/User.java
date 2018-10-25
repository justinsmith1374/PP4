package com.softwaredev.allergyapp;

import java.util.ArrayList;

public class User {
    ArrayList<String> favoriteItems;

    User() {
        favoriteItems = new ArrayList<>();
    }

    public ArrayList<String> getFavoriteItems()
    {
        return favoriteItems;
    }

    public void addToFavoriteItems(String add)
    {
        favoriteItems.add(add);
    }
}
