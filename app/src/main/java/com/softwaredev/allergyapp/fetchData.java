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

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(ProductSearch.url);
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
            allergens = "Allergens: " + JO1.getString("allergens_from_ingredients") + "\n" + "\n";
            ingredients = "Ingredients: " + JO1.getString("ingredients_text_en");


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

        ProductSearch.itemName = itemName;
        ProductSearch.allergens = allergens;
        ProductSearch.ingredients = ingredients;

    }
}
