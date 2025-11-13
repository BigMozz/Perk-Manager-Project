package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    // ===== ROOT REDIRECT TO PERKS =====
    @GetMapping("/")
    public String rootRedirect(Model model) {
        // model.addAttribute("loginUser", new User());
        // return "login"; // templates/login.html
        List<Perk> perks = new ArrayList<>();
        model.addAttribute("perks", perks);
        return "perks"; // templates/perks.html
    }

    // ===== SIGNUP =====
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // templates/signup.html
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        String result = userService.registerUser(user);
        model.addAttribute("message", result);
        model.addAttribute("user", new User()); // reset form
        return "signup";
    }

    // ===== LOGIN =====
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginUser", new User());
        return "login"; // templates/login.html
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("loginUser") User loginRequest, Model model) {
        boolean valid = userService.validateLogin(loginRequest.getEmail(), loginRequest.getPassword());
        if (valid) {
            // Redirect to the perks page on successful login
            return "redirect:/perks";
        } else {
            model.addAttribute("message", "Invalid email or password");
            return "login";
        }
    }

    // ===== PERKS DASHBOARD =====
    @GetMapping("/perks")
    public String showPerks(Model model) {
        List<Perk> perks = new ArrayList<>();
        model.addAttribute("perks", perks);
        return "perks"; // templates/perks.html
    }
}
