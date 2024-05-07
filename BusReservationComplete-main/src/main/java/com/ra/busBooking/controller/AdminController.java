package com.ra.busBooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ra.busBooking.model.BusData;
import com.ra.busBooking.model.User;
import com.ra.busBooking.repository.BusDataRepository;
import com.ra.busBooking.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/adminScreen")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusDataRepository busDataRepository;

    @ModelAttribute("busDetails")
    public BusData busData() {
        return new BusData();
    }

    @GetMapping
    public String displayDashboard(Model model){
        String user = returnUsername();
        model.addAttribute("userDetails", user);

        // Fetch all buses from the repository
        List<BusData> buses = busDataRepository.findAll();
        model.addAttribute("buses", buses);

        return "adminScreen";
    }

    private String returnUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User users = userRepository.findByEmail(user.getUsername());
        return users.getName();
    }

    @PostMapping("/add")
    public String addBus(@ModelAttribute("busDetails") BusData bus, Model model){
        String user = returnUsername();
        model.addAttribute("userDetails", user);

        busDataRepository.save(bus);

        return "redirect:/adminScreen";
    }


    @PostMapping("/update")
    public String updateBus(@ModelAttribute("bus") BusData updatedBus, Model model){
        // Get the existing bus from the database based on the ID
        BusData existingBus = busDataRepository.findById(updatedBus.getId()).orElse(null);

        // Update the existing bus with the new details
        if(existingBus != null) {
            existingBus.setFromDestination(updatedBus.getFromDestination());
            existingBus.setToDestination(updatedBus.getToDestination());
            existingBus.setFilterDate(updatedBus.getFilterDate());
            existingBus.setPrice(updatedBus.getPrice());
            existingBus.setBusName(updatedBus.getBusName());
            existingBus.setTime(updatedBus.getTime());

            // Save the updated bus to the database
            busDataRepository.save(existingBus);
        }

        return "redirect:/adminScreen";
    }


    @PostMapping("/delete")
    public String deleteBus(@RequestParam("id") Integer busId) {
        // Check if the bus exists in the database
        Optional<BusData> busOptional = busDataRepository.findById(busId);
        if (busOptional.isPresent()) {
            // If the bus exists, delete it from the database
            busDataRepository.deleteById(busId);
        } else {
            // If the bus does not exist, handle the error accordingly
            // For example, you can log an error message or display a notification to the user
            // You can redirect the user back to the admin screen with an error message
        }
        // Redirect the user back to the admin screen
        return "redirect:/adminScreen";
    }


}
