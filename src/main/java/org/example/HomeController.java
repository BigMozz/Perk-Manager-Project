package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserService userService;
    private final MembershipRepository membershipRepository;
    private final PerkRepository perkRepository;
    private final UserRepository userRepository;

    public HomeController(UserService userService, MembershipRepository membershipRepository,
            PerkRepository perkRepository, UserRepository userRepository) {
        this.userService = userService;
        this.membershipRepository = membershipRepository;
        this.perkRepository = perkRepository;
        this.userRepository = userRepository;
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
    public String loginUser(@ModelAttribute("loginUser") User loginRequest, Model model, HttpSession session) {
        boolean valid = userService.validateLogin(loginRequest.getEmail(), loginRequest.getPassword());
        if (valid) {
            // store user in session
            User user = userService.getUserByEmail(loginRequest.getEmail());
            session.setAttribute("loggedInUser", user);

            // Redirect to the perks page on successful login
            return "redirect:/perks";
        } else {
            model.addAttribute("message", "Invalid email or password");
            return "login";
        }
    }

    // ===== PERKS DASHBOARD =====
    @GetMapping("/perks")
    public String showPerks(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        boolean isLoggedIn = loggedInUser != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("loggedInUser", loggedInUser);

        // Get all perks
        List<Perk> perks = perkRepository.findAll();

        // Get user's membership types for highlighting (if logged in)
        List<String> userMembershipTypes = new ArrayList<>();
        if (isLoggedIn && loggedInUser != null && loggedInUser.getUid() != null) {
            List<Membership> memberships = membershipRepository.findByUserUid(loggedInUser.getUid());
            userMembershipTypes = memberships.stream()
                    .map(Membership::getType)
                    .collect(Collectors.toList());
        }

        // Convert perks to simple maps for JavaScript
        List<Map<String, Object>> perksData = perks.stream().map(perk -> {
            Map<String, Object> perkMap = new HashMap<>();
            perkMap.put("pid", perk.getPid());
            perkMap.put("title", perk.getTitle());
            perkMap.put("discount", perk.getDiscount());
            perkMap.put("product", perk.getProduct());
            perkMap.put("membership", perk.getMembership() != null ? perk.getMembership().getType() : "");
            perkMap.put("expiryDate",
                    perk.getExpiryDate() != null ? perk.getExpiryDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "");
            perkMap.put("upvotes", 0);
            perkMap.put("downvotes", 0);
            return perkMap;
        }).collect(Collectors.toList());

        model.addAttribute("perks", perks);
        model.addAttribute("perksData", perksData);
        model.addAttribute("userMembershipTypes", userMembershipTypes);

        return "perks"; // templates/perks.html
    }

    // ===== MANAGE MEMBERSHIPS PAGE =====
    @GetMapping("/memberships")
    public String showMembershipsPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null || loggedInUser.getUid() == null) {
            model.addAttribute("error", "Please log in to manage your memberships.");
            model.addAttribute("loginUser", new User());
            return "login"; // Redirect to login page with message
        }

        List<Membership> memberships = membershipRepository.findByUserUid(loggedInUser.getUid());
        model.addAttribute("user", loggedInUser);
        model.addAttribute("memberships", memberships);

        // Available membership types
        List<String> availableTypes = List.of("Visa", "Mastercard", "Air Miles", "CAA");
        model.addAttribute("availableTypes", availableTypes);

        return "memberships"; // templates/memberships.html
    }

    // ===== ADD MEMBERSHIP =====
    @PostMapping("/memberships/add")
    public String addMembership(@RequestParam String type, @RequestParam int number, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.getUid() == null) {
            return "redirect:/login";
        }

        // Get fresh user from database
        User user = userRepository.findById(loggedInUser.getUid()).orElse(null);
        if (user != null) {
            Membership membership = new Membership(user, type, number);
            membershipRepository.save(membership);
        }

        return "redirect:/memberships";
    }

    // ===== REMOVE MEMBERSHIP =====
    @PostMapping("/memberships/remove")
    public String removeMembership(@RequestParam Integer mid, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || loggedInUser.getUid() == null) {
            return "redirect:/login";
        }

        Membership membership = membershipRepository.findById(mid).orElse(null);
        if (membership != null && membership.getUser().getUid().equals(loggedInUser.getUid())) {
            membershipRepository.delete(membership);
        }

        return "redirect:/memberships";
    }

    // ===== LOGOUT =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/perks";
    }
}
