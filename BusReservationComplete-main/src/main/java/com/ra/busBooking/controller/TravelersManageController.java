package com.ra.busBooking.controller;

import com.ra.busBooking.model.User;
import com.ra.busBooking.repository.UserRepository;
import com.ra.busBooking.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/travelers")
public class TravelersManageController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showTravelers(Model model) {
        List<User> travelers = userRepository.findUsersByRoleIdTwo();
        model.addAttribute("travelers", travelers);
        return "travelers";
    }

    @PostMapping("/deleteTraveler")
    public String deleteTraveler(@RequestParam("id") Integer id) {
        // Delete traveler using the id parameter
        userRepository.deleteById(id);
        return "redirect:/travelers";
    }
    @PostMapping("/updateTraveler")
    public String updateTraveler(@RequestParam("id") Integer id, @RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("password") String password) {
        User traveler = userRepository.findById(id).orElse(null);
        if (traveler != null) {
            traveler.setEmail(email);
            traveler.setName(name);
            traveler.setPassword(passwordEncoder.encode(password));
            userRepository.save(traveler);
        }
        return "redirect:/travelers"; // Redirect to travelers page after updating
    }

}

