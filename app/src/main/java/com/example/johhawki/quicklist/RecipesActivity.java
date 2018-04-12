package com.example.johhawki.quicklist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {
    TextView recipename;
    LinearLayout ilayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        String r = getIntent().getStringExtra("RECIPE");
        recipename=findViewById(R.id.recipeName);
        ilayout=findViewById(R.id.ingredientContainer);

        recipename.setText(r);
        DBHandler h = new DBHandler(this);
        int recipeid = h.findrecipeid(r);
        ArrayList<String> ings = h.insertIngredients(recipeid);
        if(ings.size()==0) {
            Toast.makeText(this,"List is empty",Toast.LENGTH_SHORT).show();
        }
        else {
            for (String i : ings) {
                TextView tv=new TextView(this);
                tv.setText(i);
                this.ilayout.addView(tv);
            }
        }
    }
}
