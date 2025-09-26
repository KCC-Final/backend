package com.kcc.groo.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/api/test/ping")
    public String ping() {
        return "pong from backend 🚀";
    }
}
