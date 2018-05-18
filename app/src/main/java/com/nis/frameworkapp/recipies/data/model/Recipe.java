package com.nis.frameworkapp.recipies.data.model;

import com.google.gson.annotations.SerializedName;

public class Recipe {
    public int id;
    public String title;
    @SerializedName("href")
    public String link;
    public String ingredients;
    public String thumbnail;
    public int position;
}
