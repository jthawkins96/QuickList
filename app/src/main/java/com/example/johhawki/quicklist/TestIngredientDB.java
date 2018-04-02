package com.example.johhawki.quicklist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestIngredientDB extends AppCompatActivity {
    EditText term;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ingredient_db);

        term = (EditText)findViewById(R.id.term);
    }

    public void search(View view) {
        String te = term.getText().toString();
        DBHandler h = new DBHandler(this);

        Ingredient r = h.findIngredient(te);
        Log.d("find","Finding Recipe");
        if(r==null) {
            Toast.makeText(this, "Unable to find Recipe",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "The id for "+te+" is "+r.getRID(),Toast.LENGTH_LONG).show();
    }
    public void delete(View view) {
        String te = term.getText().toString();
        DBHandler h = new DBHandler(this);
        Boolean r = h.deleteIngredient(te);
        if(r) {

            Toast.makeText(this, "Deleted Recipe",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Unable to find Recipe",Toast.LENGTH_LONG).show();

    }

    public void fill(View view) {
        Ingredient i1 = new Ingredient(1,"Flour");
        Ingredient i2 = new Ingredient(2,"Onion");
        Ingredient i3 = new Ingredient(3,"Lemon");
        DBHandler h = new DBHandler(this);
        h.addIngredient(i1);
        h.addIngredient(i2);
        h.addIngredient(i3);
        Toast.makeText(this, "Recipes added",Toast.LENGTH_LONG).show();
    }

}
