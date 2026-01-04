import java.util.Scanner;

public class CalorieMain {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        CalorieManager manager = new CalorieManager();

        System.out.println("Welcome to Calorie Tracker!");
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        
        manager.loadProfile(name);

        if (manager.user == null) {
            System.out.println("No profile found for " + name + ". Let's create one.");
            setupUser(name, manager);
        }

        while (true) {
            System.out.println("\n--- USER: " + manager.user.name.toUpperCase() + " ---");
            System.out.println("Status: " + (int)manager.eatenToday + " eaten | " + (int)manager.burnedToday + " burned");
            System.out.println("Remaining: " + (int)(manager.user.dailyGoal - manager.eatenToday + manager.burnedToday) + " kcal");
            System.out.println("1. Log Food  2. Log Workout  3. Delete Profile  4. Exit");
            
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                logFoodUI(manager);
            } else if (choice.equals("2")) {
                System.out.print("Cals burned: ");
                manager.burnedToday += getSafeDouble();
            } else if (choice.equals("3")) {
                System.out.print("Are you sure? (Y/N): ");
                if(sc.nextLine().equalsIgnoreCase("Y")) {
                    manager.deleteProfile();
                    return; // Exit program after deletion
                }
            } else if (choice.equals("4")) {
                manager.saveProfile();
                break;
            }
            manager.saveProfile();
        }
    }

    private static void logFoodUI(CalorieManager manager) {
        System.out.print("Food name: ");
        String food = sc.nextLine().toLowerCase();
        if (manager.foodDatabase.containsKey(food)) {
            double c = manager.foodDatabase.get(food);
            manager.eatenToday += c;
            System.out.println("Logged " + food + " (" + c + " cals)");
        } else {
            System.out.print("New food! Calories for " + food + ": ");
            double c = getSafeDouble();
            manager.foodDatabase.put(food, c);
            manager.eatenToday += c;
        }
    }

    private static void setupUser(String name, CalorieManager manager) {
        System.out.print("Weight (lbs): ");
        double w = getSafeDouble();
        System.out.print("Age: ");
        int a = getSafeInt();
        
        String g = "";
        while(!g.equals("M") && !g.equals("F")) {
            System.out.print("Gender (M/F): ");
            g = sc.nextLine().toUpperCase();
            if(!g.equals("M") && !g.equals("F")) System.out.println("Error: Please type M or F.");
        }

        System.out.print("Lose 1 or 2 lbs per week? ");
        int loss = getSafeInt();

        double bmr = (g.equals("M")) 
            ? (10 * (w/2.2) + 6.25 * 177 - 5 * a + 5) 
            : (10 * (w/2.2) + 6.25 * 160 - 5 * a - 161);
        
        manager.user = new User(name, w, a, g, (bmr * 1.2) - (loss * 500));
        manager.saveProfile();
    }

    // Helper methods to catch "Bad" inputs
    private static double getSafeDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static int getSafeInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a whole number: ");
            }
        }
    }
}