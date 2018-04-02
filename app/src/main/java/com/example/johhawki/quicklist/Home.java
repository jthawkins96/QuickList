package com.example.johhawki.quicklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private int step;
    private TextView rotd;
    private String kstep="stepcount";
    private static final boolean USE_FLAG=true;
    private static final int flag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
    TextView []tv=new TextView[3];
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

        tv[0]=(TextView)findViewById(R.id.ing1);
        tv[1]=(TextView)findViewById(R.id.ing2);
        tv[2]=(TextView)findViewById(R.id.ing3);

        //populating the db with recipes
        Recipe r1 = new Recipe("Jack",1,"Chicken","www.chicken.com");
        Recipe r2 = new Recipe("Jack",2,"Beef","www.beef.com");
        Recipe r3 = new Recipe("Jack",3,"Bread","www.bread.com");

        DBHandler h2 = new DBHandler(this);
        h2.addRecipe(r1);
        h2.addRecipe(r2);
        h2.addRecipe(r3);

        //Selecting a random recipe to display as the recipe of the day
        String s = h2.selectRandomRecipe();
        int recipeid = h2.findrecipeid(s);
        ArrayList<String> ings = h2.insertIngredients(recipeid);
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
