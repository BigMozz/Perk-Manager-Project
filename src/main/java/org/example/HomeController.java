package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    // User Registration/Signup
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        // Check if user already exists
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return "User already exists with this email";
        }

        // Save new user
        userRepository.save(user);
        return "User registered successfully";
    }

    // User Login
    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            return "Login successful";
        } else {
            return "Invalid email or password";
        }
    }

    // Get all users (for testing)
    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}