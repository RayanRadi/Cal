import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class CalorieManager {
    public User user;
    public double eatenToday = 0;
    public double burnedToday = 0;
    public String lastSavedDate;
    public Map<String, Double> foodDatabase = new HashMap<>();

    public CalorieManager() {
        this.lastSavedDate = LocalDate.now().toString();
    }

    public void loadProfile(String name) {
        File file = new File(name.toLowerCase() + ".json");
        if (!file.exists()) {
            this.user = null;
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            double w=0, dg=0, et=0, bt=0; int a=0; String g="M", d="";
            foodDatabase.clear();

            while ((line = br.readLine()) != null) {
                String clean = line.replace("\"", "").replace(",", "").trim();
                if (!clean.contains(":")) continue;
                String[] p = clean.split(":", 2);
                String key = p[0].trim();
                String val = p[1].trim();

                switch(key) {
                    case "weight": w = Double.parseDouble(val); break;
                    case "age": a = Integer.parseInt(val); break;
                    case "gender": g = val; break;
                    case "dailyGoal": dg = Double.parseDouble(val); break;
                    case "eatenToday": et = Double.parseDouble(val); break;
                    case "burnedToday": bt = Double.parseDouble(val); break;
                    case "lastDate": d = val; break;
                    default: 
                        // If it's not a standard field, it's a food item
                        if (!key.equals("{") && !key.equals("}")) {
                            foodDatabase.put(key, Double.parseDouble(val));
                        }
                        break;
                }
            }
            this.user = new User(name, w, a, g, dg);
            this.eatenToday = et;
            this.burnedToday = bt;
            this.lastSavedDate = d;
            checkDayReset();
        } catch (Exception e) {
            System.out.println("Error loading profile.");
        }
    }

    private void checkDayReset() {
        String today = LocalDate.now().toString();
        if (!today.equals(lastSavedDate)) {
            eatenToday = 0;
            burnedToday = 0;
            lastSavedDate = today;
            saveProfile();
        }
    }

    public void saveProfile() {
        if (user == null) return;
        try (PrintWriter out = new PrintWriter(new FileWriter(user.name.toLowerCase() + ".json"))) {
            out.println("{");
            out.printf("  \"weight\": %.2f,%n", user.weight);
            out.printf("  \"age\": %d,%n", user.age);
            out.printf("  \"gender\": \"%s\",%n", user.gender);
            out.printf("  \"dailyGoal\": %.2f,%n", user.dailyGoal);
            out.printf("  \"eatenToday\": %.2f,%n", eatenToday);
            out.printf("  \"burnedToday\": %.2f,%n", burnedToday);
            out.printf("  \"lastDate\": \"%s\",%n", lastSavedDate);
            
            int i = 0;
            for (Map.Entry<String, Double> entry : foodDatabase.entrySet()) {
                out.printf("  \"%s\": %.2f%s%n", entry.getKey(), entry.getValue(), 
                           (i < foodDatabase.size() - 1) ? "," : "");
                i++;
            }
            out.println("}");
        } catch (IOException e) { System.out.println("Save failed."); }
    }

    public void deleteProfile() {
        if (user == null) return;
        File file = new File(user.name.toLowerCase() + ".json");
        if (file.delete()) {
            System.out.println("Profile " + user.name + " deleted.");
            user = null;
        }
    }
}