package com.softwaredev.allergyapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class fetchData extends AsyncTask<Void, Void, Void> {
    String data = "";
    String itemName = "";
    String allergens = "";
    String ingredients = "";
    String barcode = "";
    String imageUrl = "";
    String brand = "";
    String urlText = "";

    public fetchData (String u) {
        urlText = u;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(urlText);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject JO = new JSONObject(data);
            JSONObject JO1 = JO.getJSONObject("product");
            itemName = JO1.getString("product_name");

            allergens = JO1.getString("allergens_from_ingredients");
            if (!allergens.equals("")) {
                allergens = "Allergens: " + allergens;
            }
            else {
                allergens = "Allergens: None";
            }

            ingredients = "Ingredients: " + JO1.getString("ingredients_text_en");
            barcode = JO1.getString("id");
            imageUrl = JO1.getString("image_url");
            brand = JO1.getString("brands");

            for (int i = 0; i < brand.length(); ++i)
            {
                if (brand.charAt(i) == ',')
                {
                    brand = brand.substring(0, i);
                }
            }

            itemName = brand + " " + itemName;



        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (!itemName.equals("")) {
            ProductSearch.itemName = itemName;
            ProductSearch.allergens = allergens;
            ProductSearch.ingredients = ingredients;
            ProductSearch.barcode = barcode;

            ShowItem.itemName = itemName;
            ShowItem.allergensString = allergens;
            ShowItem.ingredients = ingredients;
            ShowItem.barcode = barcode;
            ShowItem.imageUrl = imageUrl;

        }
        else {
            ProductSearch.itemName = "Item Not Found";
            ProductSearch.allergens = "";
            ProductSearch.ingredients = "";
            ProductSearch.barcode = barcode;

            ShowItem.itemName = "Item Not Found";
            ShowItem.allergensString = "";
            ShowItem.ingredients = "";
            ShowItem.barcode = barcode;
            ShowItem.imageUrl = imageUrl;
        }

        ShowItem.setText();
    }
}
