package com.diet.trinity.model;

public class DailyState {
    private float weight, weight_defference;
    private int mode, point, unit;
    private Meal[] meal_list = new Meal[6];
    private int water_drink, fruit_eat, oil_eat;

    public int getServedPoints(){

        return 0;
    }

    public int getServedUnits(){
        return 0;
    }

    public Composition getComposition(){
        return new Composition();
    }

}
