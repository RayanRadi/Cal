import java.io.*;
import java.util.*;

public class FoodLibrary {
    private Map<String, Double> database = new HashMap<>();
    private final String FILE_NAME = "foods.json";

    public FoodLibrary() {
        loadLibrary();
    }

    public void addFood(String name, double cals) {
        database.put(name.toLowerCase(), cals);
        saveLibrary();
    }

    public Double getCalories(String foodName) {
        return database.get(foodName.toLowerCase());
    }

    private void saveLibrary() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            out.println("{");
            int i = 0;
            for (Map.Entry<String, Double> entry : database.entrySet()) {
                String comma = (i < database.size() - 1) ? "," : "";
                out.printf("  \"%s\": %.2f%s%n", entry.getKey(), entry.getValue(), comma);
                i++;
            }
            out.println("}");
        } catch (IOException e) {
            System.out.println("Error writing JSON.");
        }
    }

    private void loadLibrary() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Manual parsing: look for lines containing "key": value
                if (line.contains(":") && !line.trim().equals("{") && !line.trim().equals("}")) {
                    String[] parts = line.replace("\"", "").replace(",", "").trim().split(":");
                    if (parts.length == 2) {
                        database.put(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading JSON file.");
        }
    }
}