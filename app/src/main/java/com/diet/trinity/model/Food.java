package com.diet.trinity.model;

public class Food {
    private int id;
    private String name;
    private int points, units;
    private Composition composition;

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public int getUnits() {
        return units;
    }

    public Composition getComposition() {
        return composition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }
}
