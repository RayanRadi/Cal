package com.cal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class CalorieController {
    private CalorieManager manager = new CalorieManager();

    // This runs automatically at midnight (00:00:00) every day
    @Scheduled(cron = "0 0 0 * * *")
    public void resetAtMidnight() {
        // Resets the current session
        manager.eatenToday = 0;
        manager.burnedToday = 0;
        manager.saveProfile();
        System.out.println("Midnight reset complete.");
    }

    @GetMapping("/")
    public String index(@RequestParam(required = false) String name, 
                        @RequestParam(required = false) String action, Model model) {
        if (name == null || name.isEmpty()) return "index";

        manager.loadProfile(name);
        if (manager.user == null) {
            model.addAttribute("name", name);
            return "index";
        }

        double remaining = manager.user.dailyGoal - manager.eatenToday + manager.burnedToday;
        double progress = (manager.user.dailyGoal > 0) ? (manager.eatenToday / manager.user.dailyGoal) * 100 : 0;

        model.addAttribute("user", manager.user);
        model.addAttribute("eaten", manager.eatenToday);
        model.addAttribute("burned", manager.burnedToday);
        model.addAttribute("remaining", Math.round(remaining));
        model.addAttribute("progress", progress > 100 ? 100 : progress);
        model.addAttribute("action", action); 
        
        return "index";
    }

    @PostMapping("/setup")
    public String setupUser(@RequestParam String name, @RequestParam double weightLbs, @RequestParam int feet,
                            @RequestParam int inches, @RequestParam int age, @RequestParam String gender,
                            @RequestParam double poundsPerWeek) {
        try {
            double weightKg = weightLbs * 0.453592;
            double heightCm = ((feet * 12) + inches) * 2.54;
            double bmr = (gender.equalsIgnoreCase("male")) ? 
                (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5 :
                (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161;
            
            double calculatedGoal = (bmr * 1.2) - (poundsPerWeek * 500);
            if (calculatedGoal < 1200) calculatedGoal = 1200;
            
            manager.user = new User(name, weightKg, heightCm, age, gender, calculatedGoal, poundsPerWeek);
            manager.saveProfile();
            return "redirect:/?name=" + URLEncoder.encode(name, "UTF-8");
        } catch (Exception e) { return "redirect:/"; }
    }

    @PostMapping("/addEntry")
    public String addEntry(@RequestParam String name, @RequestParam String type, 
                           @RequestParam String note, @RequestParam double calories) {
        manager.loadProfile(name);
        if ("food".equals(type)) {
            manager.eatenToday += calories;
        } else {
            manager.burnedToday += calories;
        }
        manager.saveProfile();
        return "redirect:/?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
    }

    @PostMapping("/deleteProfile")
    public String deleteProfile(@RequestParam String name) {
        File file = new File(name + ".dat");
        if (file.exists()) file.delete();
        return "redirect:/";
    }
}