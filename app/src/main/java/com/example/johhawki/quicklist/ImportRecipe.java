package com.example.johhawki.quicklist;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

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

    public void onTestClick(View v) {
        Intent myInt = new Intent(this, TestDBpage.class);
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

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<String> ingredients = new ArrayList<String>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(url.getText().toString()).get();
                Elements ings = doc.getElementsByClass("recipe-ingred_txt added");
                Element url = doc.select("a[class$=video-play]").first();
                for (Element i : ings) {
                    ingredients.add(i.text());
                }
                if (url.attr("href").toString().contains("video")) {
                    ingredients.add(url.attr("href"));
                } else {
                    ingredients.add("");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            addIng(ingredients, name.getText().toString());
        }
    }

    public void addIng(ArrayList<String> ingredients, String n) {
        DBHandler h = new DBHandler(this);
        int rid = h.findRID(n);
        for (String i : ingredients) {
            Ingredient ing = new Ingredient(rid,i);
            h.addIngredient(ing);
        }

    }

    //Adds scraped data and recipe to the db
    public void add(View v) throws IOException {
        String n = name.getText().toString();
        String u = url.getText().toString();
        if (u.matches("")) {
            Toast.makeText(this, "You did not enter a url", Toast.LENGTH_SHORT).show();
            return;
        }
        Recipe r = new Recipe("johhawki",n,u);

        DBHandler h = new DBHandler(this);
        h.addRecipe(r);
        scrape();

//        Recipe tmpR = h.findRecipe(n);
//        int ri = tmpR.getRID();


//        for(String ingredient:ingredients) {
//            Ingredient in = new Ingredient(ri,ingredient);
//            h.addIngredient(in);
//        }
        Toast.makeText(this, n+" was added to the DB",Toast.LENGTH_LONG).show();
    }
}
