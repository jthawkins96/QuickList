package com.example.johhawki.quicklist;

public class Ingredient {
    private int IID;
    private int RID;
    private String Name;

    public Ingredient(int id, int rid, String n) {
        IID=id;
        RID=rid;
        Name=n;
    }

    public Ingredient(int rid, String n) {
        RID=rid;
        Name=n;
    }

    public int getIID() {
        return IID;
    }

    public void setIID(int IID) {
        this.IID = IID;
    }

    public int getRID() {
        return RID;
    }

    public void setRID(int RID) {
        this.RID = RID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
