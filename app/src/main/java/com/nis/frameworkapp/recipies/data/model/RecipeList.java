package com.nis.frameworkapp.recipies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeList {
    public String title;
    public String version;
    @SerializedName("href")
    public String link;
    @SerializedName("results")
    public ArrayList<Recipe> recipes;
}
