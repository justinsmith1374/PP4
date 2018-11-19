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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoriteItems extends AppCompatActivity {
    Context context;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static User user;
    Boolean firstTime;
    static SharedPreferences sharedPref;
    ListView favoritesLV;
    ArrayList<String> favoriteItemsBarcodes;
    ArrayList<String> favoriteItemsNames;
    ArrayAdapter<String> favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_items);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        actionbar.setTitle("Favorite Products");

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

        user = new User(this);

        favoriteItemsNames = getUser().getFavoriteItemsNames();
        favoriteItemsBarcodes = getUser().getFavoriteItemsBarcodes();

        context = this;
        favoritesLV = findViewById(R.id.favoriteItemsLV);
        favoritesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteItemsNames);
        favoritesLV.setAdapter(favoritesAdapter);

        favoritesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FavoriteItems.this, ShowItem.class);

                String url = "https://world.openfoodfacts.org/api/v0/product/" + favoriteItemsBarcodes.get(position) + ".json";
                fetchData process = new fetchData(url);
                process.execute();

                intent.putExtra("position", position);
                intent.putExtra("itemName", ProductSearch.itemName);
                intent.putExtra("allergens", ProductSearch.allergens);
                intent.putExtra("ingredients", ProductSearch.ingredients);
                intent.putExtra("barcode", ProductSearch.barcode);
                intent.putExtra("isFavorite", true);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        sharedPref = this.getSharedPreferences("com.softwaredev.allergyapp.favoriteItems", Context.MODE_PRIVATE);

        firstTime = sharedPref.getBoolean("firstTime", true);

        if(firstTime) {
            firstTime = false;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();

            Intent allergyIntent = new Intent(this, Allergies.class);
            startActivity(allergyIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        registerForContextMenu(favoritesLV);
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
        String selectedItem = favoriteItemsNames.get(acmi.position);
        if (!selectedItem.isEmpty()) {
            removeItemFromFavorites(acmi.position);
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

    @Override
    public void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
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

    /*public void sendSettings() {
        Intent settingsIntent = new Intent(this, Login.class);
        startActivity(settingsIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void sendAbout() {
        Intent aboutIntent = new Intent(this, HelpUI.class);
        startActivity(aboutIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }*/

    public static User getUser()
    {
        return user;
    }

    public void removeItemFromFavorites(int position)
    {
        user.removeItemFromFavorites(position);
    }
}