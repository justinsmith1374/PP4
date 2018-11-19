package com.softwaredev.allergyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ShowItem extends AppCompatActivity {

    public static String itemName = "";
    public static String allergensString = "";
    public static String ingredients = "";
    public static String barcode = "";
    public static String imageUrl = "";
    static String[] allergens;
    static TextView itemNameTV;
    static TextView allergensTV;
    static TextView ingredientsTV;
    static ImageView imageView;
    static TextView noImageTV;
    static Button addToFavsButton;
    static User user;
    static ArrayList<String> conflicts;
    static Context context;
    static Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        Intent intent = getIntent();
        user = FavoriteItems.getUser();

        itemName = intent.getStringExtra("itemName");
        allergensString = intent.getStringExtra("allergens");
        ingredients = intent.getStringExtra("ingredients");
        barcode = intent.getStringExtra("barcode");
        isFavorite = intent.getBooleanExtra("isFavorite", false);

        itemNameTV = findViewById(R.id.itemNameTextView);
        allergensTV = findViewById(R.id.allergensTextView);
        ingredientsTV = findViewById(R.id.ingredientsTextView);
        imageView = findViewById(R.id.imageView);
        noImageTV = findViewById(R.id.noImageTV);
        addToFavsButton = findViewById(R.id.addToFavoritesButton);

        if(isFavorite)
        {
            addToFavsButton.setVisibility(View.INVISIBLE);
        }
        else
        {
            addToFavsButton.setVisibility(View.VISIBLE);
        }

        itemNameTV.setText(itemName);
        allergensTV.setText(allergensString);
        ingredientsTV.setText(ingredients);

    }

    public void sendAddToFavorites(View view) {
        user.addToFavoriteItems(itemName, barcode);
        sendFavorites();
    }

    public void sendFavorites() {
        Intent favoritesIntent = new Intent(this, FavoriteItems.class);
        startActivity(favoritesIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void setText(){
        itemNameTV.setText(itemName);
        allergensTV.setText(allergensString);
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
        else if (!isFavorite) {
            addToFavsButton.setVisibility(View.VISIBLE);
        }

        allergens = allergensString.split(",");
        Set<String> set = new HashSet<String>();

        allergens[0] = allergens[0].substring(10);

        for (int i = 0; i < allergens.length; ++i)
        {
            allergens[i] = allergens[i].substring(1);
            allergens[i] = allergens[i].toLowerCase();
            set.add(allergens[i]);
        }

       /* for (int i = allergens.length - 1; i >= 0; --i)
        {
            for (int j = 0; j < allergens.length; ++i)
            {
                if (allergens[i].toLowerCase().equals(allergens[j].toLowerCase()) && i != j)
                {
                    allergens[i] = "";
                }
            }
        }*/

       conflicts = new ArrayList<>();

       ArrayList<String> a = new ArrayList<>();

        Iterator it = set.iterator();
        while(it.hasNext()) {
            a.add((String)it.next());
        }

        allergensString = "Allergens: ";

       for (int i = 0; i < a.size(); ++i)
        {
            if (i != a.size() - 1)
            {
                allergensString = allergensString + a.get(i) + ", ";
            }
            else {
                allergensString = allergensString + a.get(i);
            }

            allergensTV.setText(allergensString);

            if (user.checkAllergies(a.get(i)))
            {
                conflicts.add(a.get(i));
            }
        }

        if (conflicts.size() != 0)
        {
            String c = "";

            for (int i = 0; i < conflicts.size(); ++i)
            {
                if (!c.equals(""))
                    c = c + ", ";

                c = c + conflicts.get(i);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setMessage("This products contains " + c);
            AlertDialog alert1 = builder.create();
            alert1.show();
        }
    }

}
