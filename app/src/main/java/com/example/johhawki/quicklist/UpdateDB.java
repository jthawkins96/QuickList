package com.example.johhawki.quicklist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateDB extends AppCompatActivity {
    EditText t1;
    EditText t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_db);

        t1=findViewById(R.id.name);
        t2=findViewById(R.id.name2);
    }
    public void update(View view) {
        String term1 = t1.getText().toString();
        String term2 = t2.getText().toString();
        DBHandler h = new DBHandler(this);
        Boolean r = h.updateRecipe(term1,term2);
        Toast.makeText(this, "Updated Recipe",Toast.LENGTH_LONG).show();
    }
}
