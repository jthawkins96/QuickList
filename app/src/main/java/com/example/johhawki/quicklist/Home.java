package com.example.johhawki.quicklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private int step;
    private TextView rotd;
    private String kstep="stepcount";
    private static final boolean USE_FLAG=true;
    private static final int flag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
    private LinearLayout listlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle myData = getIntent().getExtras();
        if(myData==null) {
            step=0;
        }
        else myData.getInt(kstep);

        rotd=findViewById(R.id.rod);
        listlayout=findViewById(R.id.listlayout);
        //populating the db with recipes
        Recipe r1 = new Recipe("Jack",1,"Chicken","www.chicken.com");
        Recipe r2 = new Recipe("Jack",2,"Beef","www.beef.com");
        Recipe r3 = new Recipe("Jack",3,"Bread","www.bread.com");

        Ingredient i1 = new Ingredient(2,"1/2 lb Beef");
        Ingredient i2 = new Ingredient(2,"1/2 tsp Salt");
        Ingredient i3 = new Ingredient(2,"1 tbsp Lemon Juice");

        Ingredient i4 = new Ingredient(3,"1 cup Flour");
        Ingredient i5 = new Ingredient(3,"1 tsp Salt");
        Ingredient i6 = new Ingredient(3,"1/2 cup Water");

        Ingredient i7 = new Ingredient(1,"1 tsp Salt");
        Ingredient i8 = new Ingredient(1,"1 tsp Black Pepper");
        Ingredient i9 = new Ingredient(1,"1 tsp Baking Soda");

        DBHandler h2 = new DBHandler(this);

        h2.addRecipe(r1);
        h2.addRecipe(r2);
        h2.addRecipe(r3);

        h2.addIngredient(i1);
        h2.addIngredient(i2);
        h2.addIngredient(i3);
        h2.addIngredient(i4);
        h2.addIngredient(i5);
        h2.addIngredient(i6);
        h2.addIngredient(i7);
        h2.addIngredient(i8);
        h2.addIngredient(i9);

        //Selecting a random recipe to display as the recipe of the day
        String s = h2.selectRandomRecipe();
        int recipeid = h2.findrecipeid(s);
        ArrayList<String> ings = h2.insertIngredients(recipeid);
        if(ings.size()==0) {
            Toast.makeText(this,"List is empty",Toast.LENGTH_SHORT).show();
        }
        else {
            for (String i : ings) {
                TextView tv=new TextView(this);
                tv.setText(i);
                this.listlayout.addView(tv);
            }
        }
        rotd.setText(s);

    }

    public void onListClick(View v) {
        Intent myInt = new Intent(this, MainActivity.class);
        if(USE_FLAG) {
            myInt.addFlags(flag);
        }
        myInt.putExtra(kstep,step+1);
        startActivity(myInt);
    }
    public void onImportClick(View v) {
        Intent myInt = new Intent(this, ImportRecipe.class);
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
}
