package com.diet.trinity.model;

public class BMI {
    float value;
    String state;

    public BMI(float value, String state){
        this.value = value;
        this.state = state;
    }

    public float getValue() {
        return value;
    }

    public String getState() {
        return state;
    }
}
