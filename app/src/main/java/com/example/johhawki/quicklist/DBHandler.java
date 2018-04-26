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

    private static final String Tname3 = "List";

    public DBHandler(Context context) {
        super(context,DBName,null,DBV);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPES_TABLE = "Create table "+Tname+"("+User+" Varchar(255) NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                name+" TEXT NOT NULL, "+
                url+" VARCHAR(255))";
        String CREATE_INGREDIENTS_TABLE = "Create table "+Tname2+"(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                RID2+" INTEGER NOT NULL, "+
                name2+" TEXT NOT NULL)";
        String CREATE_LIST_TABLE = "Create table "+Tname3+"("+IID+" INTEGER NOT NULL, "+
                name2+" TEXT NOT NULL)";
        String INSERT_RECIPE = "INSERT INTO "+Tname+" (username, name, url) "+"VALUES('USER','Chicken Parm','')";
        String INSERT_ING1 = "INSERT INTO "+Tname2+" (RID, name) "+"VALUES(1,'1/2 lb Chicken')";
        String INSERT_ING2 = "INSERT INTO "+Tname2+" (RID, name) "+"VALUES(1,'Mozzarella Cheese')";
        String INSERT_ING3 = "INSERT INTO "+Tname2+" (RID, name) "+"VALUES(1,'Marinara Sauce')";

        db.execSQL(CREATE_INGREDIENTS_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_LIST_TABLE);
        db.execSQL(INSERT_RECIPE);
        db.execSQL(INSERT_ING1);
        db.execSQL(INSERT_ING2);
        db.execSQL(INSERT_ING3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int  oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS "+Tname);
        db.execSQL("DROP TABLE IF EXISTS "+Tname2);
        db.execSQL("DROP TABLE IF EXISTS "+Tname3);

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
        db.close();
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
        db.close();
        return list[0];
    }

    //Uses the RID to get the ingredients for the recipe of the day
    public ArrayList<String> insertIngredients(int recipeid) {
        ArrayList<String> list = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        String query ="SELECT * from "+Tname2+" WHERE "+RID2+"="+recipeid;
        System.out.println(query);
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

    //takes a search term and returns a list of recipes like the search term
    public ArrayList<String> listRecipes(String n) {
        String query = "SELECT * FROM "+Tname+" WHERE "+name+" LIKE '%"+n+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);
        ArrayList<String> rs=new ArrayList();
        if(cur.moveToFirst()) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    String tmpName = cur.getString(2);
                    rs.add(tmpName);
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        db.close();

        return rs;
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

    public String findURL(String n) {
        String query = "SELECT * FROM "+Tname+" WHERE "+name+"= \""+n+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);

        if(cur.moveToFirst()) {
            String tmpURL = cur.getString(3);
            cur.close();
            return tmpURL;
        }
        db.close();
        return "";
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

    //finds and returns the id of an ingredient
    public int findIID(String ing) {
        String query = "SELECT * FROM "+Tname2+" WHERE "+name2+"= \""+ing+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);
        if(cur.moveToFirst()) {
            int tmpIID = cur.getInt(0);
            cur.close();
            return tmpIID;
        }
        db.close();
        return 0;
    }

    //adds ingredient
    public void addIngredient(Ingredient i) {
        ContentValues vals = new ContentValues();
        vals.put(RID2,i.getRID());
        vals.put(name2,i.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(Tname2, null, vals);
        db.close();
    }

    //adds an ingredient to the shopping list
    public void addListIng(Ingredient ing) {
        ContentValues vals = new ContentValues();
        vals.put(IID,ing.getIID());
        vals.put(name2,ing.getName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Tname3, null, vals);
        db.close();
    }

    //selects all the ingredients in the list table, called on the MainActivity page
    public ArrayList<String> getListIng() {
        String query = "SELECT * FROM "+Tname3;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(query,null);
        ArrayList<String> is=new ArrayList();
        if(cur.moveToFirst()) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    String tmpName = cur.getString(1);
                    is.add(tmpName);
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        db.close();

        return is;
    }

    //clears shopping list table
    public void clearList() {
        String query = "DELETE FROM "+Tname3;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
}
