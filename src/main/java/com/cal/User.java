package com.cal;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public double weight;
    public double height;
    public int age;
    public String gender;
    public double dailyGoal;
    public double poundsPerWeek;

    public User(String name, double weight, double height, int age, String gender, double dailyGoal, double poundsPerWeek) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.dailyGoal = dailyGoal;
        this.poundsPerWeek = poundsPerWeek;
    }
}