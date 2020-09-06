package com.diet.trinity.model;

import java.util.ArrayList;

public class Meal {
    ArrayList<Food> foods = new ArrayList<>();

    public Composition getComposition(){
        Composition returnValue = new Composition();
        for(Food theFood : foods){
            returnValue.proteins += theFood.getComposition().proteins;
            returnValue.carbs += theFood.getComposition().carbs;
            returnValue.fat += theFood.getComposition().fat;
        }
        return returnValue;
    }

    public int getUnits(){
        int sum = 0;
        for(Food theFood : foods){
            sum += theFood.getUnits();
        }
        return sum;
    }

    public int getPoints(){
        int sum = 0;
        for(Food theFood : foods){
            sum += theFood.getPoints();
        }
        return sum;
    }
}
