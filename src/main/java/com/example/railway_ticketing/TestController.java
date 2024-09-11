package com.example.railway_ticketing;

import org.springframework.web.bind.annotation.GetMapping;

public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello123";
    }
}
