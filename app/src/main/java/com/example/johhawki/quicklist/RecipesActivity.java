package com.example.johhawki.quicklist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {
    TextView recipename;
    LinearLayout ilayout;
    Button vBtn;
    String vUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        int SMS_PERMISSION_REQ_CODE_SUBMIT=101;

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RecipesActivity.this, new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, SMS_PERMISSION_REQ_CODE_SUBMIT);
        }

        String r = getIntent().getStringExtra("RECIPE");
        recipename=findViewById(R.id.recipeName);
        ilayout=findViewById(R.id.ingredientContainer);
        vBtn = findViewById(R.id.videoBtn);

        recipename.setText(r);

        DBHandler h = new DBHandler(this);
        vUrl = h.findURL(r);

        //setting the video button to disable if the url is null
        if(vUrl.matches("")) {
            vBtn.setEnabled(false);
        }

        int recipeid = h.findrecipeid(r);
        ArrayList<String> ings = h.insertIngredients(recipeid);
        if(ings.size()==0) {
            Toast.makeText(this,"List is empty",Toast.LENGTH_SHORT).show();
        }
        else {
            for (String i : ings) {
                TextView tv=new TextView(this);
                tv.setText(i);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                this.ilayout.addView(tv);
            }
        }
    }

    //When the user clicks the add ingredients button on the RecipeActivity page it takes all the ingredients on the page and puts it into the list table
    public  void onAddIngClick(View v) {
        DBHandler h = new DBHandler(this);
        if (ilayout != null)
        {
            for (int x = 0; x < ilayout.getChildCount(); x++)
            {
                View kid = ilayout.getChildAt(x);
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

    //Opens up gmail and autofills the subject and body with the recipe name and the ingredients
    public void onShareClick(View v) {
        String rname = recipename.getText().toString();
        String msgBody="";

        //going through each ingredient and adding to the body
        if (ilayout != null)
        {
            for (int x = 0; x < ilayout.getChildCount(); x++)
            {
                View kid = ilayout.getChildAt(x);
                TextView ing = (TextView) kid;
                String i = ing.getText().toString();
                msgBody+=(i+"\n");
            }
        }

        if(msgBody.length()!=0 && rname.length()!=0) {
            Intent emailInt = new Intent(Intent.ACTION_SEND);
            emailInt.setData(Uri.parse("mailto:"));
            emailInt.setType("text/plain");

            emailInt.putExtra(Intent.EXTRA_TEXT, msgBody);
            emailInt.putExtra(Intent.EXTRA_SUBJECT,"Check out this "+rname+" recipe!");
            try {
                startActivity(Intent.createChooser(emailInt,"Send email..."));
            } catch(Exception e) {
                Toast.makeText(getApplicationContext(), "Problem sending email",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Recipe has no ingredients", Toast.LENGTH_SHORT).show();
        }
    }

    //Goes to video activity and sends the video url
    public void onVideoClick(View v) {
        Intent intent = new Intent(getBaseContext(), VideoActivity.class);
        intent.putExtra("VIDEO", vUrl);
        startActivity(intent);
    }
}
