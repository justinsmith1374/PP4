package com.softwaredev.allergyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ShowItem extends AppCompatActivity {

    String itemName;
    String allergens;
    String ingredients;
    TextView itemNameTV;
    TextView allergensTV;
    TextView ingredientsTV;

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

        itemNameTV = findViewById(R.id.itemNameTextView);
        allergensTV = findViewById(R.id.allergensTextView);
        ingredientsTV = findViewById(R.id.ingredientsTextView);

        itemNameTV.setText(itemName);
        allergensTV.setText(allergens);
        ingredientsTV.setText(ingredients);

    }

}
