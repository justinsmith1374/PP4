package com.softwaredev.allergyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;

public class ShowItem extends AppCompatActivity {

    public static String itemName = "";
    public static String allergens = "";
    public static String ingredients = "";
    public static String barcode = "";
    public static String imageUrl = "";
    static TextView itemNameTV;
    static TextView allergensTV;
    static TextView ingredientsTV;
    static ImageView imageView;
    static TextView noImageTV;
    static Button addToFavsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        itemName = intent.getStringExtra("itemName");
        allergens = intent.getStringExtra("allergens");
        ingredients = intent.getStringExtra("ingredients");
        barcode = intent.getStringExtra("barcode");

        itemNameTV = findViewById(R.id.itemNameTextView);
        allergensTV = findViewById(R.id.allergensTextView);
        ingredientsTV = findViewById(R.id.ingredientsTextView);
        imageView = findViewById(R.id.imageView);
        noImageTV = findViewById(R.id.noImageTV);
        addToFavsButton = findViewById(R.id.addToFavoritesButton);

        itemNameTV.setText(itemName);
        allergensTV.setText(allergens);
        ingredientsTV.setText(ingredients);

    }

    public void sendAddToFavorites(View view) {
        ProductSearch.getUser().addToFavoriteItems(itemName, barcode);
    }

    public static void setText(){
        itemNameTV.setText(itemName);
        allergensTV.setText(allergens);
        ingredientsTV.setText(ingredients);

        if (!imageUrl.equals("")) {
            noImageTV.setVisibility(View.INVISIBLE);
            Picasso.get().load(imageUrl).into(imageView);
        }
        else {
            noImageTV.setVisibility(View.VISIBLE);
        }

        if (itemName.equals("Item Not Found")) {
            addToFavsButton.setVisibility(View.INVISIBLE);
        }
        else {
            addToFavsButton.setVisibility(View.VISIBLE);
        }
    }

}
