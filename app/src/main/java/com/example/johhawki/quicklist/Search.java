package com.example.johhawki.quicklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private int step;
    private String kstep="stepcount";
    private static final boolean USE_FLAG=true;
    private static final int flag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
    EditText term;
    private LinearLayout lout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        term=findViewById(R.id.term);
        lout=findViewById(R.id.recipelayout);
        Bundle myData = getIntent().getExtras();
        if(myData==null) {
            step=0;
        }
        else myData.getInt(kstep);
    }

    public void search(View view) {
        lout.removeAllViews();
        String te = term.getText().toString();
        DBHandler h = new DBHandler(this);

        ArrayList<String> rs = h.listRecipes(te);
        for(String r: rs) {
            final String tmpR = r;
            TextView tv=new TextView(this);
            tv.setTextSize(20);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), RecipesActivity.class);
                    intent.putExtra("RECIPE", tmpR);
                    startActivity(intent);
                }
            });
            tv.setText(r);
            this.lout.addView(tv);
        }
    }

    public void onHomeClick(View v) {
        Intent myInt = new Intent(this, Home.class);
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
    public void onImportClick(View v) {
        Intent myInt = new Intent(this, ImportRecipe.class);
        if(USE_FLAG) {
            myInt.addFlags(flag);
        }
        myInt.putExtra(kstep,step+1);
        startActivity(myInt);
    }
}
