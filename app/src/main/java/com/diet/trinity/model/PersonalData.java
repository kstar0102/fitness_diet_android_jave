package com.diet.trinity.model;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import static java.lang.Math.log;

public class PersonalData {
    private Goal goal;
    private float initial_weight, weight, height;
    private float waist_perimeter=0, neck_perimeter=0, thigh_perimeter=0;
    private Gender gender;
    private Date birthday, start_date;
    private int age;
    private int gymType;
    private int[] gymDurationPerWeek;
    private float ideal_weight, goal_weight, weekly_reduce;
    private int points, units;
    private DietMode dietMode;
    private int membership;

    private float[] gymCoeff = {1.2f, 1.375f, 1.55f, 1.725f, 1.9f};

    private static PersonalData instance = new PersonalData();

    public static PersonalData getInstance()
    {
        return instance;
    }

    public static void setInstance(PersonalData ins){
        instance = ins;
    }

    public boolean writeData(Context context){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        //Log.d("class data", json);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("personal.db", Context.MODE_APPEND));
            outputStreamWriter.write(json);
            outputStreamWriter.append("\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public float getInitial_weight() {
        return initial_weight;
    }

    public void setInitial_weight(float initial_weight) {
        this.initial_weight = initial_weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGymType() {
        return gymType;
    }

    public void setGymType(int gymType) {
        this.gymType = gymType;
    }

    public int[] getGymDurationPerWeek() {
        return gymDurationPerWeek;
    }

    public void setGymDurationPerWeek(int[] gymDurationPerWeek) {
        this.gymDurationPerWeek = gymDurationPerWeek;
    }

    public float getIdeal_weight() {
        if(gender == Gender.MALE){
            ideal_weight = (int ) (48 + (height - 150) * 1.1);
        }
        else{
            ideal_weight = (int ) (45 + (height - 150) * 0.9);
        }

        return ideal_weight;
    }

    public float getGoal_weight() {
        return goal_weight;
    }

    public void setGoal_weight(float goal_weight) {
        this.goal_weight = goal_weight;
    }

    public float getWeekly_reduce() {
        return weekly_reduce;
    }

    public void setWeekly_reduce(float weekly_reduce) {
        this.weekly_reduce = weekly_reduce;
    }

    public int getUnits() {
        int BMR;
        if(gender == Gender.MALE){
            BMR = (int ) (655 + 9.6 * weight + 1.8 * height - 4.7 * age);
        }
        else{
            BMR = (int ) (66 + 13.7 * weight + 5 * height - 6.8 * age);
        }
        float AMR = BMR * gymCoeff[gymType];

        units = (int) (AMR / 100);

        return units;
    }

    public int getPoints() {
        points = (int) (units * 1.19394);
        return points;
    }

    public DietMode getDietMode() {
        return dietMode;
    }

    public void setDietMode(DietMode dietMode) {
        this.dietMode = dietMode;
    }

    public int getMembership() {
        return membership;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public float getWaist_perimeter() {
        return waist_perimeter;
    }

    public void setWaist_perimeter(float waist_perimeter) {
        this.waist_perimeter = waist_perimeter;
    }

    public float getNeck_perimeter() {
        return neck_perimeter;
    }

    public void setNeck_perimeter(float neck_perimeter) {
        this.neck_perimeter = neck_perimeter;
    }

    public float getThigh_perimeter() {
        return thigh_perimeter;
    }

    public void setThigh_perimeter(float thigh_perimeter) {
        this.thigh_perimeter = thigh_perimeter;
    }

    public float getBFP(){
        float bfp = 0;
        if(gender == Gender.MALE){
            bfp = (float) (495 / (1.0324 - 0.19077 * log(waist_perimeter - neck_perimeter) + 0.15456 * log(height)) - 450);
        }
        else{
            bfp = (float) (163205 * log(waist_perimeter+thigh_perimeter-neck_perimeter) - 97684 * log(height) - 104912);
        }
        return bfp;
    }

    public BMI getBMI(){
        float bmi = 0;
        String BMIState;
        bmi = weight / (height * height / 10000);
        if (bmi < 18.5){
            BMIState = "Ελλιποβαρής";
        }
        else if(bmi < 24.9){
            BMIState = "Φυσιολογικός";
        }
        else if(bmi < 29.9){
            BMIState = "Υπέρβαρος";
        }
        else if(bmi < 35){
            BMIState = "Παχύσαρκος (1ου βαθμού)";
        }
        else if(bmi < 40){
            BMIState = "Παχύσαρκος (2ου βαθμού)";
        }
        else{
            BMIState = "Παχύσαρκος (3ου βαθμού)";
        }
        return new BMI(bmi, BMIState);
    }

}
