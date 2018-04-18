package com.example.johhawki.quicklist;

import android.content.Intent;
import android.graphics.Color;
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

        listlayout.removeAllViews();

        //clearing the test data each time its loaded
        DBHandler h2 = new DBHandler(this);

        //populating the db with recipes and ingredients

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
                tv.setTextColor(Color.parseColor("#FFFFFF"));
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

    //function used to add ingredients to list from the home page if the user clicks "ADD INGREDIENTS"
    public  void onAddIngClick(View v) {
        DBHandler h = new DBHandler(this);
        if (listlayout != null)
        {
            for (int x = 0; x < listlayout.getChildCount(); x++)
            {
                View kid = listlayout.getChildAt(x);
                TextView ing = (TextView) kid;
                String i = ing.getText().toString();
                int iid = h.findIID(i);
                Ingredient ingred = new Ingredient(iid,i);
                h.addListIng(ingred);
            }
        }
        Intent myInt = new Intent(this, MainActivity.class);
        startActivity(myInt);
    }
}
