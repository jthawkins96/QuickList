package com.example.johhawki.quicklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.util.Log;

public class TestDBpage extends AppCompatActivity {
    EditText term;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dbpage);

        term = (EditText)findViewById(R.id.term);
    }
    // lets the user know the id of the Recipe they're looking up
    public void search(View view) {
        String te = term.getText().toString();
        DBHandler h = new DBHandler(this);

        Recipe r = h.findRecipe(te);
        Log.d("find","Finding Recipe");
        if(r==null) {
            Toast.makeText(this, "Unable to find Recipe",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "The id for "+te+" is "+r.getRID(),Toast.LENGTH_LONG).show();
    }
    // lets the user know if the delete was successful
    public void delete(View view) {
        String te = term.getText().toString();
        DBHandler h = new DBHandler(this);
        Boolean r = h.deleteRecipe(te);
        if(r) {

            Toast.makeText(this, "Deleted Recipe",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Unable to find Recipe",Toast.LENGTH_LONG).show();

    }
    //fills the db with test data
    public void fill(View view) {
        Recipe r1 = new Recipe("Jack","Chicken","www.chicken.com");
        Recipe r2 = new Recipe("Jack","Beef","www.beef.com");
        Recipe r3 = new Recipe("Jack","Bread","www.bread.com");
        DBHandler h = new DBHandler(this);
        h.addRecipe(r1);
        h.addRecipe(r2);
        h.addRecipe(r3);
        Toast.makeText(this, "Recipes added",Toast.LENGTH_LONG).show();
    }

    public void onDbClick(View v) {
        Intent myInt = new Intent(this, UpdateDB.class);
        startActivity(myInt);
    }

}
