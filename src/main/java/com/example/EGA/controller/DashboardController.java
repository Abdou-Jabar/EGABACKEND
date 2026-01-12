package com.example.EGA.controller;

import com.example.EGA.dto.DashboardDTO;
import com.example.EGA.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardDTO getStats() {
        return dashboardService.getDashboardData();
    }
}