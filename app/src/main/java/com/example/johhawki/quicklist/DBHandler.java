package com.example.johhawki.quicklist;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DBHandler extends SQLiteOpenHelper{
    private static final int DBV = 1;
    private static final String DBName = "recipe.db";

    private static final String Tname = "Recipes";
    private static final String User = "username";
    private static final String RID = "ID";
    private static final String name = "name";
    private static final String url = "url";

    private static final String Tname2 = "Ingredients";
    private static final String IID = "IID";
    private static final String RID2 = "RID";
    private static final String name2 = "name";

    public DBHandler(Context context) {
        super(context,DBName,null,DBV);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "Create table "+Tname+"("+User+" Varchar(255) NOT NULL, "+
                RID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                name+" TEXT NOT NULL, "+
                url+" VARCHAR(255))";
        String CREATE_INGREDIENTS_TABLE = "Create table "+Tname2+"("+IID+" INTEGER NOT NULL, "+
                RID2+" INTEGER NOT NULL, "+
                name2+" TEXT NOT NULL)";

        db.execSQL(CREATE_INGREDIENTS_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int  oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS "+Tname);
        db.execSQL("DROP TABLE IF EXISTS "+Tname2);

        onCreate(db);
    }
    //selects a random recipe from the db
    public String selectRandomRecipe() {
        ArrayList<String> list = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        String query ="SELECT * from "+Tname;
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            while (!cur.isAfterLast()) {
                String name = cur.getString(2);
                list.add(name);
                cur.moveToNext();
            }
        }
        Random rand = new Random();
        String n = list.get(rand.nextInt(list.size()));
        return n;
    }
    //finds the recipe id to get the ingredients for the Recipe of the Day
    public int findrecipeid(String rname) {
        int[] list= new int[1];
        SQLiteDatabase db = this.getWritableDatabase();
        int rid;
        String q1 = "Select * from "+Tname+" WHERE "+name+"='"+rname+"'";
        Cursor cur = db.rawQuery(q1, null);
        if (cur.moveToFirst()) {
            rid = cur.getInt(1);
            list[0]=rid;
        }
        return list[0];
    }
    //Uses the RID to get the ingredients for the recipe of the day
    public ArrayList<String> insertIngredients(int recipeid) {
        ArrayList<String> list = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        String query ="SELECT * from "+Tname2+" WHERE "+RID2+"="+recipeid;
        Cursor cur = db.rawQuery(query, null);
        if (cur.moveToFirst()) {
            while (!cur.isAfterLast()) {
                String iname = cur.getString(2);
                list.add(iname);
                cur.moveToNext();
            }
        }
        return list;
    }
    //Adds recipes
    public void addRecipe(Recipe r) {
        ContentValues vals = new ContentValues();
        vals.put(User,r.getUser());
        vals.put(name,r.getName());
        vals.put(url,r.getUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(Tname, null, vals);
        db.close();
    }
    //Finds Recipes
    public Recipe findRecipe(String n) {
        String query = "SELECT * FROM "+Tname+" WHERE "+name+"= \""+n+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);

        Recipe myR = null;

        if(cur.moveToFirst()) {
            String tmpUser = cur.getString(0);
            int tmpID = cur.getInt(1);
            String tmpName = cur.getString(2);
            String tmpUrl = cur.getString(3);
            cur.close();

            myR = new Recipe(tmpUser,tmpID,tmpName,tmpUrl);
        }
        db.close();

        return myR;
    }
    //updates the recipe name
    public Boolean updateRecipe(String n, String n2) {
        String query = "UPDATE "+Tname+" SET "+name+"='"+n2+"' WHERE "+name+"= \""+n+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);
        Boolean result = false;
        if(cur.moveToFirst()) {
            result = true;
        }
        db.close();

        return result;
    }
    //deletes the recipe with a certain name
    public boolean deleteRecipe(String rname) {
        boolean result = false;

        String query = "SELECT * FROM "+Tname+" WHERE "+name+"=\""+rname+"\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cur = db.rawQuery(query,null);

        if(cur.moveToFirst()){
            int tmpID = cur.getInt(1);

            db.delete(Tname,RID+"= ?",new String[]{String.valueOf(tmpID)});

            cur.close();
            result=true;
        }
        db.close();
        return result;
    }

    //used to get the Recipe ID and used later in the scraping function
    public int findRID(String n) {
        String query = "SELECT * FROM "+Tname+" WHERE "+name+"= \""+n+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);

        if(cur.moveToFirst()) {
            int tmpRID = cur.getInt(1);
            cur.close();
            return tmpRID;
        }
        db.close();
        return 0;
    }

    //adds ingredient
    public void addIngredient(Ingredient i) {
        ContentValues vals = new ContentValues();
        vals.put(IID,i.getIID());
        vals.put(RID2,i.getRID());
        vals.put(name2,i.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(Tname2, null, vals);
        db.close();
    }

    public Ingredient findIngredient(String n) {
        String query = "SELECT * FROM "+Tname2+" WHERE "+name2+"= \""+n+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);

        Ingredient myI = null;

        if(cur.moveToFirst()) {
            int tmpID = cur.getInt(0);
            int tmpRID = cur.getInt(1);
            String tmpName = cur.getString(2);
            cur.close();
            myI = new Ingredient(tmpID,tmpRID,tmpName);
        }
        db.close();

        return myI;
    }
    //deletes ingredient
    public boolean deleteIngredient(String iname) {
        boolean result = false;

        String query = "SELECT * FROM "+Tname2+" WHERE "+name2+"=\""+iname+"\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cur = db.rawQuery(query,null);

        if(cur.moveToFirst()){
            int tmpID = cur.getInt(0);

            db.delete(Tname2,IID+"= ?",new String[]{String.valueOf(tmpID)});

            cur.close();
            result=true;
        }
        db.close();
        return result;
    }


}
