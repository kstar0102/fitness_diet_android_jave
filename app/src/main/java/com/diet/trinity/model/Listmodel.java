package com.diet.trinity.model;

import android.util.Log;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class Listmodel {
    Integer item_id;
    String item_name;
    String item_carbon;
    String item_protein;
    String item_fat;
    String item_gram;
    String item_point;
    public Listmodel(Integer id, String foodname, String carbon_m, String protein_m, String fat_m, String gram_m) throws JSONException {
        this.item_id = id;
        this.item_name = foodname;
        this.item_carbon = carbon_m;
        this.item_protein = protein_m;
        this.item_fat = fat_m;
        this.item_gram = "1 μερίδα (" + gram_m + "γρ.)";
        this.item_point = "1";
    }

    public String getListTitle() throws JSONException {
        return item_name;
    }
    public String getListGram() throws JSONException {
        return item_gram;
    }
    public String getListPoint() throws JSONException {
        item_point = (Float.parseFloat(this.item_carbon)/15 + Float.parseFloat(this.item_protein)/15 + Float.parseFloat(this.item_fat)/15)+"point";
        return item_point;
    }
    public Integer getListId() throws JSONException{
        return item_id;
    }
}
