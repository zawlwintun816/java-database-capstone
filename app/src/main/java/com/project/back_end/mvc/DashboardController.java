package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.back_end.services.Service;

@Controller
public class DashboardController {

    @Autowired
    private Service service;

    // Admin Dashboard Route
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        String response = service.validateToken(token, "admin");

        if (response == null || response.isEmpty()) {
            return "admin/adminDashboard";
        }

        return "redirect:/";
    }

    // Doctor Dashboard Route
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        String response = service.validateToken(token, "doctor");

        if (response == null || response.isEmpty()) {
            return "doctor/doctorDashboard";
        }

        return "redirect:/";
    }
}