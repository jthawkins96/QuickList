package com.example.johhawki.quicklist;

public class Recipe {
    private String User;
    private int RID;
    private String Name;
    private String url;

    public Recipe(String user, String n, String u) {
        User=user;
        Name=n;
        url=u;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        this.User = user;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
