package com.softwaredev.allergyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class Allergies extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    static ArrayList<String> allergyList  = new ArrayList<>(1);
    ArrayAdapter<String> arrAdapter;
    static SharedPreferences sharedPref;
    int mSize;
    Spinner dropdown;
    String allergyString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionbar.setTitle("Allergies");

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        if (menuItem.toString().equals("Favorite Products")) {
                            sendFavorites();
                        //} else if (menuItem.toString().equals("Enter Barcode")) {
                          //  sendSearch();
                        } else if (menuItem.toString().equals("Barcode Scanner")) {
                            sendBarcode();
                        } else if (menuItem.toString().equals("Allergies")) {
                            sendAllergies();
                        } /*else if (menuItem.toString().equals("Settings")) {
                            sendSettings();
                        } else if (menuItem.toString().equals("About")) {
                            sendAbout();
                        }*/

                        return true;
                    }
                }
        );

        arrAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allergyList);
        ListView listView = findViewById(R.id.allergyListView);
        listView.setAdapter(arrAdapter);

        sharedPref = this.getSharedPreferences("com.softwaredev.allergyapp.allergies", Context.MODE_PRIVATE);
        mSize = sharedPref.getInt("size", 0);
        allergyList.clear();
        String temp;

        for (int i = 1; i < mSize + 1; ++i) {
            temp = sharedPref.getString("allergy" + i, "!null!");
            if (!temp.equals("!null!")) {
                allergyList.add(temp);
            }
        }

        dropdown = findViewById(R.id.allergySpinner);

       String[] allergies = new String[]{"Select an allergy", "Alcohol", "Apples", "Cinnamon", "Citrus", "Dairy", "Eggs", "Fish", "Garlic", "Peanuts", "Shellfish", "Soy", "Tree Nuts", "Wheat"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allergies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object allergy = adapter.getItem(position);
                if (allergy != null && allergy.toString() != allergyString && allergy.toString() != "Select an allergy")
                {
                    allergyString = allergy.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registerForContextMenu(listView);
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPref = this.getSharedPreferences("com.softwaredev.allergyapp.allergies", Context.MODE_PRIVATE);

        mSize = sharedPref.getInt("size", 0);
        allergyList.clear();
        String temp;

        for (int i = 1; i < mSize + 1; ++i) {
            temp = sharedPref.getString("allergy" + i, "!null!");
            if (!temp.equals("!null!")) {
                allergyList.add(temp);
            }
        }

        arrAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allergyList);
        ListView listView = findViewById(R.id.allergyListView);
        listView.setAdapter(arrAdapter);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.allergy_context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedItem = allergyList.get(acmi.position);
        if (!selectedItem.isEmpty()) {
            removeItemFromAllergy(acmi.position);
            recreate();
            return true;
        }
        else
            return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendFavorites() {
        Intent favoritesIntent = new Intent(this, FavoriteItems.class);
        startActivity(favoritesIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void sendSearch() {
        Intent searchIntent = new Intent(this, ProductSearch.class);
        startActivity(searchIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void sendBarcode() {
        Intent barcodeIntent = new Intent(this, BarcodeScanner.class);
        startActivity(barcodeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void sendAllergies() {
        Intent allergyIntent = new Intent(this, Allergies.class);
        startActivity(allergyIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
/*
    public void sendSettings() {
        Intent settingsIntent = new Intent(this, Login.class);
        startActivity(settingsIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void sendAbout() {
        Intent aboutIntent = new Intent(this, HelpUI.class);
        startActivity(aboutIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
    */

    public void addButtonPressed(View view)
    {
        if (allergyString != "Select an allergy") {
            for (int i = 0; i < allergyList.size(); ++i) {
                if (allergyList.get(i) == allergyString)
                    return;
            }
            addAllergy(allergyString);
            recreate();
        }
    }

    public static void addAllergy(String allergy)
    {
        allergyList.add(allergy);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("allergy" + Integer.toString(allergyList.size()), allergy);
        editor.putInt("size", allergyList.size());
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
            editor.putInt("size", allergyList.size());
            editor.commit();
        }
    }
}
