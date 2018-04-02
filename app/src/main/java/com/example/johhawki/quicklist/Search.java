package com.example.johhawki.quicklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends AppCompatActivity {
    private int step;
    private String kstep="stepcount";
    private static final boolean USE_FLAG=true;
    private static final int flag = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
    EditText term;
    TextView user;
    TextView recipeid;
    TextView recipename;
    TextView recipeurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        term=findViewById(R.id.term);
        user=findViewById(R.id.username);
        recipeid=findViewById(R.id.rid);
        recipename=findViewById(R.id.rname);
        recipeurl=findViewById(R.id.url);
        Bundle myData = getIntent().getExtras();
        if(myData==null) {
            step=0;
        }
        else myData.getInt(kstep);
    }
    public void search(View view) {
        String te = term.getText().toString();
        DBHandler h = new DBHandler(this);

        Recipe r = h.findRecipe(te);
        Log.d("find","Finding Recipe");
        if(r==null) {
            Toast.makeText(this, "Unable to find Recipe",Toast.LENGTH_LONG).show();
        }
        else {
            if(r.getUser()=="") {
                user.setText("None");
            }
            else {
                user.setText(r.getUser());
            }
            recipeid.setText(String.valueOf(r.getRID()));
            recipename.setText(r.getName());
            recipeurl.setText(r.getUrl());
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
