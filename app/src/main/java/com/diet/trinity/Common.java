package com.diet.trinity;

import android.content.Context;

import com.diet.trinity.model.DailyState;
import com.diet.trinity.model.WeeklyState;

import java.util.ArrayList;

public class Common {

    private String baseURL="http://saudiclean.com.sa/test/skillflect_admin/";
    //private String baseURL="http://10.0.2.2/skillflect_admin/";

    private ArrayList<DailyState> dailyStates = new ArrayList<>();
    private ArrayList<WeeklyState> weeklyStates = new ArrayList<>();

    private String[] sports = {"Αντισφαίριση (επιτραπέζια προπόνηση)", "Αντισφαίριση (επιτραπέζια αγώνας)"};
    private int[] membershipPrice = {0, 100, 150, 200, 230, 260, 290, 310, 320, 340, 350, 360, 370, 500};

    private static Common instance = new Common();

    public void Common(){

    }

    public static Common getInstance()
    {
        return instance;
    }

    public String getBaseURL() {
        return baseURL;
    }

}
