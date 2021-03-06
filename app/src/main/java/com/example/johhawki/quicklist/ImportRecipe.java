package com.example.johhawki.quicklist;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

public class ImportRecipe extends AppCompatActivity {
    EditText url;
    EditText name;
    TextView debugger;

    private int step;
    private String kstep="stepcount";
    private static final boolean USE_FLAG=true;
    private static final int flag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_recipe);

        Bundle myData = getIntent().getExtras();
        if(myData==null) {
            step=0;
        }
        else myData.getInt(kstep);
        url = (EditText)findViewById(R.id.url);
        name = (EditText)findViewById(R.id.name);
        debugger = (TextView)findViewById(R.id.debug);
    }

    public void onHomeClick(View v) {
        Intent myInt = new Intent(this, Home.class);
        if(USE_FLAG) {
            myInt.addFlags(flag);
        }
        myInt.putExtra(kstep,step+1);
        startActivity(myInt);
    }

    public void onSearchClick(View v) {
        Intent myInt = new Intent(this, Search.class);
        if(USE_FLAG) {
            myInt.addFlags(flag);
        }
        myInt.putExtra(kstep,step+1);
        startActivity(myInt);
    }

    public void onListClick(View v) {
        Intent myInt = new Intent(this, MainActivity.class);
        if(USE_FLAG) {
            myInt.addFlags(flag);
        }
        myInt.putExtra(kstep,step+1);
        startActivity(myInt);
    }

    // uses JSOUP to get the ingredients from Allrecipes.com
    public void scrape() {
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, String> {
        ArrayList<String> ingredients = new ArrayList<String>();
        String error= "There was a problem parsing the URL";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            //JSOUP code to scrape the data
            try {
                Document doc = Jsoup.connect(url.getText().toString()).get();
                Elements ings = doc.getElementsByClass("recipe-ingred_txt added");
                Element videourl = doc.select("a[class$=video-play]").first();
                for (Element i : ings) {
                    ingredients.add(i.text());
                }
                if (videourl.attr("href").contains("video")) {
                    ingredients.add(videourl.attr("href"));
                } else {
                    ingredients.add("");
                }
            } catch (Exception e) {
                return error;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String err) {
            if(err==null) {
                //running addIng function to insert each ingredient into the list db
                addIng(ingredients, name.getText().toString());

                //moving to the list page after inserting ingredients
                Intent myInt = new Intent(getApplicationContext(), MainActivity.class);
                if(USE_FLAG) {
                    myInt.addFlags(flag);
                }
                myInt.putExtra(kstep,step+1);
                startActivity(myInt);
            }
            else {
                //setting the debugger to let the user know there was an error
                debugger.setText(err);
            }
        }
    }

    //function to insert each ingredient into the list and ingredients dbs
    public void addIng(ArrayList<String> ingredients, String n) {
        DBHandler h = new DBHandler(this);

        //taking the last element of the ingredients array which is the url and inserting the recipe
        Recipe r = new Recipe("johhawki",n,ingredients.get(ingredients.size()-1));
        h.addRecipe(r);

        //finding RID to insert ingredients
        int rid = h.findRID(n);
        for (String i : ingredients) {
            if(ingredients.indexOf(i) == (ingredients.size() -1)) {
                continue;
            }
            else {
                Ingredient ing = new Ingredient(rid,i);
                h.addIngredient(ing);
                h.addListIng(ing);
            }
        }

    }

    //Adds the recipe and then runs the scraper function
    public void add(View v) {
        String n = name.getText().toString();
        String u = url.getText().toString();
        debugger.setText("");
        if (u.matches("") || n.matches("")) {
            Toast.makeText(this, "You did not enter a Recipe Name/URL", Toast.LENGTH_SHORT).show();
        }
        else {
            scrape();
        }
    }
}
