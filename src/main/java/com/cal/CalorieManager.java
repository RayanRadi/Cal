package com.cal;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CalorieManager {
    public User user;
    public double eatenToday = 0;
    public double burnedToday = 0;
    public Map<String, Double> foodDatabase = new HashMap<>();

    public void saveProfile() {
        if (user == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(user.name + ".dat"))) {
            oos.writeObject(user);
            oos.writeDouble(eatenToday);
            oos.writeDouble(burnedToday);
            oos.writeObject(foodDatabase);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void loadProfile(String name) {
        File file = new File(name + ".dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                user = (User) ois.readObject();
                eatenToday = ois.readDouble();
                burnedToday = ois.readDouble();
                foodDatabase = (Map<String, Double>) ois.readObject();
            } catch (Exception e) { user = null; }
        } else { user = null; }
    }
}