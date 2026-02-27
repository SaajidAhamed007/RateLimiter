package com.example.ratelimiter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class AdminController {

    @GetMapping("/admin")
    public String admin() {
        return "Admin endpoint";
    }
}