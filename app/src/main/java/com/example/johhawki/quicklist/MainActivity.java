package com.example.johhawki.quicklist;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int step;
    private String kstep="stepcount";
    private static final boolean USE_FLAG=true;
    private static final int flag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
    LinearLayout listC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listC=findViewById(R.id.listContainer);
        Bundle myData = getIntent().getExtras();
        if(myData==null) {
            step=0;
        }
        else myData.getInt(kstep);

        //Getting all the ingredients and listing them
        DBHandler h = new DBHandler(this);
        ArrayList<String> ings = h.getListIng();
        for (String i : ings) {
            TextView tv=new TextView(this);
            tv.setText(i);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            this.listC.addView(tv);
        }
    }

    public void onClearListClick(View v) {
        listC.removeAllViews();
        DBHandler h = new DBHandler(this);
        h.clearList();
    }

    public void onHomeClick(View v) {
        Intent myInt = new Intent(this, Home.class);
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
