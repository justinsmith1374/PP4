package com.softwaredev.allergyapp;

import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProductSearch extends AppCompatActivity {

        private DrawerLayout mDrawerLayout;
        private ActionBarDrawerToggle mToggle;
        Button searchButton;
        public static TextView results;
        public static String url = "https://world.openfoodfacts.org/api/v0/product/737628064502.json";
        EditText searchQuery;
        public static String itemName = "";
        public static String allergens = "";
        public static String ingredients = "";
        public static String barcode = "";
        public static User user;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_search);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionbar.setTitle("Barcode Search");

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
                            } else if (menuItem.toString().equals("Product Search")) {
                                sendSearch();
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

            searchButton = (Button) findViewById(R.id.searchButton);
            results = (TextView) findViewById(R.id.textView2);
            searchQuery = findViewById(R.id.editText);

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = searchQuery.getText().toString();
                    if (!searchQuery.getText().toString().equals("Enter Barcode")) {
                        url = "https://world.openfoodfacts.org/api/v0/product/" + searchQuery.getText().toString() + ".json";
                    }
                    fetchData process = new fetchData();
                    process.execute();
                    if (itemName != "")
                    {
                        Intent infoIntent = new Intent(ProductSearch.this, ShowItem.class);
                        infoIntent.putExtra("itemName", itemName);
                        infoIntent.putExtra("allergens", allergens);
                        infoIntent.putExtra("ingredients", ingredients);
                        infoIntent.putExtra("barcode", barcode);
                        startActivity(infoIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });

            user = new User(this);
        }

        @Override
        public void onResume() {
            super.onResume();

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

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
            Intent barcodeIntent = new Intent(this, ProductSearch.class);
            startActivity(barcodeIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        public void sendAllergies() {
            Intent allergyIntent = new Intent(this, Allergies.class);
            startActivity(allergyIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

       /* public void sendSettings() {
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
}