package org.example;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email already registered";
        }
        userRepository.save(user);
        return "Signup successful!";
    }

    public boolean validateLogin(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
