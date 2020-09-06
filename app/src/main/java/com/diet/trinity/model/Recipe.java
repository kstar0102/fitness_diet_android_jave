package com.diet.trinity.model;

public class Recipe extends Food{
    int categoryID;
    String photoUrl;
    int photoResId = 0;
    String howToMake = "";

    public Recipe(int categoryID, String name, int resId, int points){
        this.categoryID = categoryID;
        this.setName(name);
        this.photoResId = resId;
        this.setPoints(points);
    }

    public int getPhotoResId() {
        return photoResId;
    }

}
